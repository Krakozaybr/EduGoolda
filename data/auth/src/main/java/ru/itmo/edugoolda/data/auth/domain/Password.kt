package ru.itmo.edugoolda.data.auth.domain

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Password(val value: String)
