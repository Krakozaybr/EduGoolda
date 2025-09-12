package ru.itmo.edugoolda.data.lesson.lesson_info.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId

import ru.itmo.edugoolda.data.lesson.lesson_info.internal.domain.LessonInfoListWithTotal
import ru.itmo.edugoolda.data.lesson.lesson_info.api.LessonInfo
import ru.itmo.edugoolda.data.user.internal.dto.UserInfoDTO
import ru.itmo.edugoolda.data.user.internal.dto.toDomain

@Serializable
data class LessonInfoListResponse(
    @SerialName("lessons") val lessonInfoList: List<LessonInfoDTO>,
    @SerialName("total") val total: Int,
)

fun LessonInfoListResponse.toDomain(): LessonInfoListWithTotal = LessonInfoListWithTotal(
    lessonInfoList = lessonInfoList.map { it.toDomain() },
    total = total
)

@Serializable
data class LessonInfoDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("teacher") val teacher: UserInfoDTO,
    @SerialName("created_at") val createdAt: String
)

fun LessonInfoDTO.toDomain(): LessonInfo = LessonInfo(
    id = LessonId(id),
    name = name,
    description = description,
    teacher = teacher.toDomain(),
    createdAt = createdAt
)