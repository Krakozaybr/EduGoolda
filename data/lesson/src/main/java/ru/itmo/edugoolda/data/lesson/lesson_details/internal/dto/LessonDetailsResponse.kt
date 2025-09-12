package ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.group.group_list.internal.dto.GroupInfoDTO
import ru.itmo.edugoolda.data.group.group_list.internal.dto.toDomain
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonFullDetails
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonGeneralDetails
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonStatus
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonStudentDetails
import ru.itmo.edugoolda.data.lesson.lesson_details.api.SolutionDetails
import ru.itmo.edugoolda.data.lesson.lesson_details.api.SolutionMessage
import ru.itmo.edugoolda.data.lesson.lesson_details.api.SolutionMessageId
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.user.internal.dto.UserInfoDTO
import ru.itmo.edugoolda.data.user.internal.dto.toDomain
import java.time.format.DateTimeFormatter

@Serializable
internal data class LessonStudentDetailsDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("teacher") val teacher: UserInfoDTO,
    @SerialName("deadline") val deadline: Instant?,
    @SerialName("groups") val groups: List<GroupInfoDTO>,
    @SerialName("messages") val messages: List<SolutionMessageDTO>,
    @SerialName("status") val status: String,
    @SerialName("is_estimatable") val isEstimatable: Boolean
)

internal fun LessonStudentDetailsDTO.toDomain(): LessonStudentDetails = LessonStudentDetails(
    id = LessonId(id),
    name = name,
    description = description,
    teacher = teacher.toDomain(),
    deadline = deadline,
    groups = groups.map { it.toDomain() },
    messages = messages.map { it.toDomain() },
    status = when (status) {
        "reviewed" -> LessonStatus.Reviewed
        else -> LessonStatus.Pending
    },
    isEstimatable = isEstimatable,
)

fun Instant.toCurrentLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.defaultFormat(): String = toJavaLocalDateTime().format(
    DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")
)

fun LocalDateTime.onlyTimeFormat(): String = toJavaLocalDateTime().format(
    DateTimeFormatter.ofPattern("HH:mm")
)

@Serializable
internal data class SolutionMessageDTO(
    @SerialName("id") val id: String,
    @SerialName("sent_at") val sentAt: Instant,
    @SerialName("message") val message: String,
    @SerialName("author") val author: UserInfoDTO
)

internal fun SolutionMessageDTO.toDomain(): SolutionMessage = SolutionMessage(
    id = SolutionMessageId(id),
    sentAt = sentAt,
    message = message,
    author = author.toDomain()
)

@Serializable
internal data class SendMessageRequest(
    @SerialName("message") val message: String
)

@Serializable
internal data class SolutionDetailsDTO(
    @SerialName("id") val id: String,
    @SerialName("lesson") val lesson: LessonGeneralDetailsDTO,
    @SerialName("messages") val messages: List<SolutionMessageDTO>,
    @SerialName("status") val status: String,
)

@Serializable
internal data class LessonGeneralDetailsDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("teacher") val teacher: UserInfoDTO,
    @SerialName("deadline") val deadline: Instant?,
    @SerialName("groups") val groups: List<GroupInfoDTO>,
    @SerialName("is_estimatable") val isEstimatable: Boolean,
)

internal fun SolutionDetailsDTO.toDomain(): SolutionDetails = SolutionDetails(
    id = SolutionId(id),
    lesson = lesson.toDomain(),
    messages = messages.map { it.toDomain() },
    status = when (status) {
        "reviewed" -> LessonStatus.Reviewed
        else -> LessonStatus.Pending
    }
)

internal fun LessonGeneralDetailsDTO.toDomain(): LessonGeneralDetails = LessonGeneralDetails(
    id = LessonId(id),
    name = name,
    description = description,
    teacher = teacher.toDomain(),
    deadline = deadline,
    groups = groups.map { it.toDomain() },
    isEstimatable = isEstimatable
)

@Serializable
internal data class SetSolutionStatusRequest(
    @SerialName("status") val status: String
)

@Serializable
internal data class LessonFullDetailsDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("teacher") val teacher: UserInfoDTO,
    @SerialName("deadline") val deadline: Instant?,
    @SerialName("opens_at") val opensAt: Instant?,
    @SerialName("groups") val groups: List<GroupInfoDTO>,
    @SerialName("solutions_count") val solutionsCount: Int,
    @SerialName("is_estimatable") val isEstimatable: Boolean
)

internal fun LessonFullDetailsDTO.toDomain(): LessonFullDetails = LessonFullDetails(
    id = LessonId(id),
    name = name,
    description = description,
    teacher = teacher.toDomain(),
    deadline = deadline,
    opensAt = opensAt,
    groups = groups.map { it.toDomain() },
    solutionsCount = solutionsCount,
    isEstimatable = isEstimatable
)