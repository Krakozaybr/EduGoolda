package ru.itmo.edugoolda.data.join_requests.api

import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.user.api.UserInfo

data class JoinRequest(
    val id: JoinRequestId,
    val sender: UserInfo,
    val groupInfo: GroupInfo,
    val createAt: String
)