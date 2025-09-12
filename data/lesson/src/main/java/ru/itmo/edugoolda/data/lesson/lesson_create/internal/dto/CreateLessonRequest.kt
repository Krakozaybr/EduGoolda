package ru.itmo.edugoolda.data.lesson.lesson_create.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateLessonRequest(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("group_ids") val groupIds: List<String>,
    @SerialName("is_estimatable") val isEstimatable: Boolean,
    @SerialName("deadline") val deadline: Long?,
    @SerialName("opens_at") val opensAt: Long?,
)