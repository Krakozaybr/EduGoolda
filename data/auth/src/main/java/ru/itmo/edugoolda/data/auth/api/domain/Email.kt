package ru.itmo.edugoolda.data.auth.api.domain

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Email(val value: String)
