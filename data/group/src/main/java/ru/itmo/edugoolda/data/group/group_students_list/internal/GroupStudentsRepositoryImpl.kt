package ru.itmo.edugoolda.data.group.group_students_list.internal

import me.aartikov.replica.algebra.paged.map
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.keyed_paged.KeyedPagedFetcher
import me.aartikov.replica.keyed_paged.KeyedPagedReplicaSettings
import me.aartikov.replica.paged.PagedData
import me.aartikov.replica.paged.PagedReplicaSettings
import ru.itmo.edugoolda.core.utils.PageWithTotalAmount
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.group.group_list.internal.GroupsListRepositoryImpl
import ru.itmo.edugoolda.data.group.group_students_list.api.GroupStudentsList
import ru.itmo.edugoolda.data.group.group_students_list.api.GroupStudentsRepository
import ru.itmo.edugoolda.data.group.group_students_list.api.KickType
import ru.itmo.edugoolda.data.group.group_students_list.internal.dto.KickStudentRequest
import ru.itmo.edugoolda.data.group.group_students_list.internal.dto.toDomain
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.data.user.api.UserInfo
import kotlin.time.Duration.Companion.minutes

internal class GroupStudentsRepositoryImpl(
    replicaClient: ReplicaClient,
    private val groupStudentsApi: GroupStudentsApi,
    private val groupsListRepositoryImpl: GroupsListRepositoryImpl
) : GroupStudentsRepository {
    companion object {
        private const val PAGE_SIZE = 20
    }

    private val _groupOfStudentsReplica = replicaClient.createKeyedPagedReplica(
        name = "students list replica",
        childName = {
            "child $it"
        },
        settings = KeyedPagedReplicaSettings(maxCount = 10),
        idExtractor = { it.id },
        childSettings = {
            PagedReplicaSettings(staleTime = 5.minutes)
        },
        fetcher = object : KeyedPagedFetcher<GroupId, UserInfo, PageWithTotalAmount<UserInfo>> {
            override suspend fun fetchFirstPage(key: GroupId): PageWithTotalAmount<UserInfo> {
                val users = groupStudentsApi.getStudentsList(
                    id = key.value,
                    pageSize = PAGE_SIZE,
                    page = 1,
                ).toDomain()

                return PageWithTotalAmount(
                    hasNextPage = users.total > PAGE_SIZE,
                    hasPreviousPage = false,
                    items = users.users,
                    total = users.total
                )
            }

            override suspend fun fetchNextPage(
                key: GroupId,
                currentData: PagedData<UserInfo, PageWithTotalAmount<UserInfo>>,
            ): PageWithTotalAmount<UserInfo> {
                val users = groupStudentsApi.getStudentsList(
                    id = key.value,
                    pageSize = PAGE_SIZE,
                    page = currentData.pages.size + 1
                ).toDomain()

                return PageWithTotalAmount(
                    hasNextPage = users.total > PAGE_SIZE * (currentData.pages.size + 1),
                    hasPreviousPage = false,
                    items = users.users,
                    total = users.total
                )
            }
        }
    )

    override val groupOfStudentsReplica = _groupOfStudentsReplica.map { _, data ->
        GroupStudentsList(data.items, data.hasNextPage)
    }

    override suspend fun kickStudentFromGroup(
        action: KickType,
        groupId: GroupId,
        studentId: UserId,
    ) {
        val actionType = when (action) {
            KickType.Kick -> "kick"
            KickType.KickAndBan -> "kick_and_ban"
        }
        val kickActionRequest = KickStudentRequest(
            actionType,
            groupId.value,
            studentId.value
        )
        groupStudentsApi.kickStudentFromGroup(kickActionRequest)
        _groupOfStudentsReplica.mutateData(
            key = groupId,
            transform = { pages ->
                pages.map { page ->
                    page.copy(
                        hasNextPage = page.hasNextPage,
                        hasPreviousPage = page.hasPreviousPage,
                        items = page.items.filter { it.id != studentId },
                        total = page.total
                    )
                }
            }
        )
    }

    override suspend fun leaveFromGroup(groupId: GroupId) {
        groupStudentsApi.leaveFromGroup(groupId.value)
        groupsListRepositoryImpl._groupInfoListReplica.onEachPagedReplica {
            refresh()
        }
    }
}