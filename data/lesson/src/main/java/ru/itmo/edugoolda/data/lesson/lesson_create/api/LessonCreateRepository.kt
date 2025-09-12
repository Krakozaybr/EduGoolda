package ru.itmo.edugoolda.data.lesson.lesson_create.api

import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonFullDetails

interface LessonCreateRepository {
    suspend fun createLesson(
        name: String,
        description: String?,
        groupIdList: List<GroupId>,
        isEstimatable: Boolean,
        deadline: Long?,
        opensAt: Long?
    ): LessonFullDetails
}