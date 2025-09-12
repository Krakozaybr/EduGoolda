package ru.itmo.edugoolda.data.solutions.api

import ru.itmo.edugoolda.data.user.api.UserInfo

data class SolutionInfo(
    val id: SolutionId,
    val sentAt: String,
    val student: UserInfo,
    val status: String,
    val lessonInfo: LessonInfo
)