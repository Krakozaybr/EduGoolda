package ru.itmo.edugoolda.data.auth

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.itmo.edugoolda.core.network.NetworkApiFactory
import ru.itmo.edugoolda.data.auth.api.AuthRepository
import ru.itmo.edugoolda.data.auth.internal.AuthInterceptor
import ru.itmo.edugoolda.data.auth.internal.AuthRepositoryImpl
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensProvider
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensRefresher
import ru.itmo.edugoolda.data.auth.internal.tokens.AuthTokensStorage

val dataAuthModule = module {
    singleOf(AuthTokensStorage::Base) binds arrayOf(
        AuthTokensStorage::class,
        AuthTokensProvider::class
    )
    singleOf(::AuthRepositoryImpl) binds arrayOf(
        AuthRepository::class,
        AuthTokensRefresher::class
    )
    singleOf(::AuthInterceptor) bind NetworkApiFactory.Interceptor::class
}
