package ru.itmo.edugoolda.data.group.group_list.internal

import me.aartikov.replica.algebra.paged.map
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.keyed_paged.KeyedPagedFetcher
import me.aartikov.replica.keyed_paged.KeyedPagedReplicaSettings
import me.aartikov.replica.paged.PagedData
import me.aartikov.replica.paged.PagedReplicaSettings
import ru.itmo.edugoolda.core.utils.PageWithTotalAmount
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.group.group_list.api.GroupInfoList
import ru.itmo.edugoolda.data.group.group_list.api.GroupListRepository
import ru.itmo.edugoolda.data.group.group_list.internal.dto.ChangeFavouriteRequest
import ru.itmo.edugoolda.data.group.group_list.internal.dto.toDomain
import kotlin.time.Duration.Companion.minutes

internal class GroupsListRepositoryImpl(
    replicaClient: ReplicaClient,
    private val groupListApi: GroupListApi,
) : GroupListRepository {
    companion object {
        private const val PAGE_SIZE = 20
    }

    val _groupInfoListReplica = replicaClient.createKeyedPagedReplica(
        name = "group list replica",
        childName = {
            "child $it"
        },
        settings = KeyedPagedReplicaSettings(maxCount = 10),
        idExtractor = { it.id },
        childSettings = {
            PagedReplicaSettings(staleTime = 5.minutes)
        },
        fetcher = object : KeyedPagedFetcher<String, GroupInfo, PageWithTotalAmount<GroupInfo>> {
            override suspend fun fetchFirstPage(key: String): PageWithTotalAmount<GroupInfo> {
                val groups =
                    groupListApi.getGroupsList(
                        query = key,
                        subjectId = null,
                        pageSize = PAGE_SIZE,
                        page = 1
                    ).toDomain()

                return PageWithTotalAmount(
                    hasNextPage = groups.total > PAGE_SIZE,
                    hasPreviousPage = false,
                    items = groups.groups,
                    total = groups.total
                )
            }

            override suspend fun fetchNextPage(
                key: String,
                currentData: PagedData<GroupInfo, PageWithTotalAmount<GroupInfo>>,
            ): PageWithTotalAmount<GroupInfo> {
                val groups = groupListApi.getGroupsList(
                    query = key,
                    subjectId = null,
                    pageSize = PAGE_SIZE,
                    page = currentData.pages.size + 1
                ).toDomain()

                return PageWithTotalAmount(
                    hasNextPage = groups.total > PAGE_SIZE * (currentData.pages.size + 1),
                    hasPreviousPage = false,
                    items = groups.groups,
                    total = groups.total
                )
            }
        }
    )

    override val groupInfoListReplica = _groupInfoListReplica.map { _, data ->
        GroupInfoList(data.items, data.hasNextPage)
    }

    override suspend fun deleteGroup(groupId: GroupId) {
        groupListApi.deleteGroup(groupId.value)
        _groupInfoListReplica.onEachPagedReplica {
            mutateData { pages ->
                pages.map { page: PageWithTotalAmount<GroupInfo> ->
                    page.copy(
                        hasNextPage = page.hasNextPage,
                        hasPreviousPage = page.hasPreviousPage,
                        items = page.items.filter { it.id != groupId },
                        total = page.total
                    )
                }
            }
        }
    }

    override suspend fun changeFavouriteStatus(id: GroupId, isFavourite: Boolean) {
        groupListApi.changeGroupFavouriteStatus(id.value, ChangeFavouriteRequest(isFavourite))
        _groupInfoListReplica.onEachPagedReplica {
            mutateData { pages ->
                pages.map { page: PageWithTotalAmount<GroupInfo> ->
                    page.copy(
                        hasNextPage = page.hasNextPage,
                        hasPreviousPage = page.hasPreviousPage,
                        items = page.items.map { item ->
                            if (item.id == id) {
                                item.copy(isFavourite = isFavourite)
                            } else {
                                item
                            }
                        },
                        total = page.total
                    )
                }
            }
        }
    }
}
