package ru.itmo.edugoolda.data.auth

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.itmo.edugoolda.core.network.NetworkApiFactory
import ru.itmo.edugoolda.data.auth.data.AuthInterceptor
import ru.itmo.edugoolda.data.auth.data.AuthRepository
import ru.itmo.edugoolda.data.auth.data.AuthRepositoryImpl
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensProvider
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensRefresher
import ru.itmo.edugoolda.data.auth.data.tokens.AuthTokensStorage

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
