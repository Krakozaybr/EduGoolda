package ru.itmo.edugoolda.data.lesson.lesson_create.internal

import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_create.api.LessonCreateRepository
import ru.itmo.edugoolda.data.lesson.lesson_create.internal.dto.CreateLessonRequest
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonFullDetails
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.toDomain
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfoRepository

internal class LessonCreateRepositoryImpl(
    private val createLessonApi: LessonCreateApi,
    private val lessonInfoRepository: LessonInfoRepository
) : LessonCreateRepository {
    override suspend fun createLesson(
        name: String,
        description: String?,
        groupIdList: List<GroupId>,
        isEstimatable: Boolean,
        deadline: Long?,
        opensAt: Long?,
    ): LessonFullDetails {
        val actionRequest = CreateLessonRequest(
            name = name,
            description = description,
            groupIds = groupIdList.map { it.value },
            isEstimatable = isEstimatable,
            deadline = deadline,
            opensAt = opensAt
        )
        val createdLesson = createLessonApi.createLesson(actionRequest).toDomain()
        lessonInfoRepository.lessonInfoListReplica.refresh()
        return createdLesson
    }
}