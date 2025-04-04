package ru.itmo.edugoolda.data.auth.api.domain

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Password(val value: String)
