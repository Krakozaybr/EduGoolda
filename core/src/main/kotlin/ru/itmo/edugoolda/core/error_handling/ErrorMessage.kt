package ru.itmo.edugoolda.core.error_handling

import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.desc.plus
import ru.itmo.edugoolda.core.BuildConfig
import ru.itmo.edugoolda.core.R
import ru.itmo.edugoolda.core.error_handling.DeserializationException
import ru.itmo.edugoolda.core.error_handling.ExternalAppNotFoundException
import ru.itmo.edugoolda.core.error_handling.NoInternetException
import ru.itmo.edugoolda.core.error_handling.SSLHandshakeException
import ru.itmo.edugoolda.core.error_handling.ServerException
import ru.itmo.edugoolda.core.error_handling.ServerUnavailableException
import ru.itmo.edugoolda.core.error_handling.UnauthorizedException
import ru.itmo.edugoolda.core.utils.Resource
import ru.itmo.edugoolda.core.utils.ResourceFormatted

/**
 * Returns human readable messages for exceptions.
 */
val Exception.errorMessage: StringDesc
    get() = when (this) {

        is ExternalAppNotFoundException -> StringDesc.Resource(R.string.error_matching_application_not_found)

        is ServerUnavailableException -> StringDesc.Resource(R.string.error_server_unavailable)

        is NoInternetException -> StringDesc.Resource(R.string.error_no_internet_connection)

        is UnauthorizedException -> StringDesc.Resource(R.string.error_unauthorized)

        is SSLHandshakeException -> StringDesc.Resource(R.string.error_ssl_handshake)

        is ServerException -> message?.let { StringDesc.Raw(it) }
            ?: StringDesc.Resource(R.string.error_server)

        is DeserializationException -> {
            val description = this.message
            if (description != null && BuildConfig.DEBUG) {
                StringDesc.ResourceFormatted(R.string.error_deserialization) + "\n\n$description".desc()
            } else {
                StringDesc.Resource(R.string.error_deserialization)
            }
        }

        else -> {
            val description = this.message
            if (description != null && BuildConfig.DEBUG) {
                StringDesc.ResourceFormatted(R.string.error_unexpected) + "\n\n$description".desc()
            } else {
                StringDesc.Resource(R.string.error_unexpected)
            }
        }
    }