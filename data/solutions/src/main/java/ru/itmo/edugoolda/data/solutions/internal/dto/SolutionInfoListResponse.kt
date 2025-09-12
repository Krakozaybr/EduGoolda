package ru.itmo.edugoolda.data.solutions.internal.dto

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.solutions.api.LessonId
import ru.itmo.edugoolda.data.solutions.api.LessonInfo
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.solutions.api.SolutionInfo
import ru.itmo.edugoolda.data.solutions.internal.domain.SolutionListWithTotal
import ru.itmo.edugoolda.data.user.internal.dto.UserInfoDTO
import ru.itmo.edugoolda.data.user.internal.dto.toDomain
import java.time.format.DateTimeFormatter

@Serializable
data class SolutionInfoListResponse(
    @SerialName("solutions") val solutionList: List<SolutionInfoDTO>,
    @SerialName("total") val total: Int,
)

fun SolutionInfoListResponse.toDomain(): SolutionListWithTotal = SolutionListWithTotal(
    solutionInfoList = solutionList.map { it.toDomain() },
    total = total
)

@Serializable
data class SolutionInfoDTO(
    @SerialName("id") val id: String,
    @SerialName("sent_at") val sentAt: Instant,
    @SerialName("student") val student: UserInfoDTO,
    @SerialName("status") val status: String,
    @SerialName("lesson_info") val lessonInfo: LessonInfoDTO,
)

fun Instant.toCurrentLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.defaultFormat() = toJavaLocalDateTime().format(
    DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")
)

fun SolutionInfoDTO.toDomain(): SolutionInfo = SolutionInfo(
    id = SolutionId(id),
    sentAt = sentAt.toCurrentLocalDateTime().defaultFormat(),
    student = student.toDomain(),
    status = status,
    lessonInfo = lessonInfo.toDomain()
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