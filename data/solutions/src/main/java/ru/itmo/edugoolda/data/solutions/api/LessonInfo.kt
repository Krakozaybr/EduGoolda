package ru.itmo.edugoolda.data.solutions.api

import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.user.api.UserInfo

data class LessonInfo(
    val id: LessonId,
    val name: String,
    val description: String?,
    val teacher: UserInfo,
    val createdAt: String
)

@JvmInline
@Serializable
value class LessonId(val value: String)