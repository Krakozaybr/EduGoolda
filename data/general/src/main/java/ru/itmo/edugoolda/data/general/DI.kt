package ru.itmo.edugoolda.data.general

import org.koin.dsl.module
import ru.itmo.edugoolda.data.auth.dataAuthModule

val dataModule = module {
    includes(
        dataAuthModule,
    )
}