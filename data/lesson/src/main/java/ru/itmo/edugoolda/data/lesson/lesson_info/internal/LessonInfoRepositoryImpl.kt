package ru.itmo.edugoolda.data.lesson.lesson_info.internal

import me.aartikov.replica.algebra.paged.map
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.paged.PagedData
import me.aartikov.replica.paged.PagedFetcher
import me.aartikov.replica.paged.PagedReplicaSettings
import ru.itmo.edugoolda.core.utils.PageWithTotalAmount
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfo
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoList
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoRepository
import ru.itmo.edugoolda.data.lesson.lesson_info.internal.dto.toDomain
import ru.itmo.edugoolda.data.solutions.internal.SolutionRepositoryImpl
import kotlin.time.Duration.Companion.minutes

class LessonInfoRepositoryImpl(
    replicaClient: ReplicaClient,
    private val api: LessonInfoApi,
    private val solutionRepositoryImpl: SolutionRepositoryImpl
) : LessonInfoRepository {
    companion object {
        private const val PAGE_SIZE = 20
    }

    override suspend fun deleteLesson(lessonId: LessonId) {
        api.deleteLesson(lessonId.value)
        _lessonInfoListReplica.refresh()
        solutionRepositoryImpl.solutionListReplica.refresh()
    }

     private val _lessonInfoListReplica = replicaClient.createPagedReplica(
        name = "lessons list replica",
        settings = PagedReplicaSettings(staleTime = 5.minutes),
        idExtractor = { it.id },
        fetcher = object : PagedFetcher<LessonInfo, PageWithTotalAmount<LessonInfo>> {

            override suspend fun fetchFirstPage(): PageWithTotalAmount<LessonInfo> {
                val lessonInfoList = api.getLessonInfoList(pageSize = PAGE_SIZE, 1).toDomain()

                return PageWithTotalAmount(
                    hasNextPage = lessonInfoList.total > PAGE_SIZE,
                    hasPreviousPage = false,
                    items = lessonInfoList.lessonInfoList,
                    total = lessonInfoList.total

                )
            }

            override suspend fun fetchNextPage(currentData: PagedData<LessonInfo, PageWithTotalAmount<LessonInfo>>): PageWithTotalAmount<LessonInfo> {
                val lessonInfoList =
                    api.getLessonInfoList(pageSize = PAGE_SIZE, currentData.pages.size + 1)
                        .toDomain()

                return PageWithTotalAmount(
                    hasNextPage = lessonInfoList.total > PAGE_SIZE * (currentData.pages.size + 1),
                    hasPreviousPage = false,
                    items = lessonInfoList.lessonInfoList,
                    total = lessonInfoList.total

                )
            }
        }
    )

    override val lessonInfoListReplica = _lessonInfoListReplica.map {
        LessonInfoList(
            lessonInfoList = it.items,
            hasNextPage = it.hasNextPage
        )
    }
}