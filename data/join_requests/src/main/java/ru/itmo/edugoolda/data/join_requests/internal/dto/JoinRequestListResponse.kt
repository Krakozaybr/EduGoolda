package ru.itmo.edugoolda.data.join_requests.internal.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itmo.edugoolda.data.group.group_list.internal.dto.GroupInfoDTO
import ru.itmo.edugoolda.data.group.group_list.internal.dto.toDomain
import ru.itmo.edugoolda.data.join_requests.api.JoinRequest
import ru.itmo.edugoolda.data.join_requests.api.JoinRequestId
import ru.itmo.edugoolda.data.join_requests.internal.domain.JoinRequestListWithTotal
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.defaultFormat
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.toCurrentLocalDateTime
import ru.itmo.edugoolda.data.user.internal.dto.UserInfoDTO
import ru.itmo.edugoolda.data.user.internal.dto.toDomain

@Serializable
data class JoinRequestListResponse(
    @SerialName("requests") val joinRequestList: List<JoinRequestDTO>,
    @SerialName("total") val total: Int,
)

fun JoinRequestListResponse.toDomain(): JoinRequestListWithTotal = JoinRequestListWithTotal(
    joinRequestList = joinRequestList.map { it.toDomain() },
    total = total
)

@Serializable
data class JoinRequestDTO(
    @SerialName("id") val id: String,
    @SerialName("sender") val sender: UserInfoDTO,
    @SerialName("group") val groupInfo: GroupInfoDTO,
    @SerialName("created_at") val createdAt: Instant
)

fun JoinRequestDTO.toDomain(): JoinRequest = JoinRequest(
    id = JoinRequestId(id),
    sender = sender.toDomain(),
    groupInfo = groupInfo.toDomain(),
    createAt = createdAt.toCurrentLocalDateTime().defaultFormat()
)
