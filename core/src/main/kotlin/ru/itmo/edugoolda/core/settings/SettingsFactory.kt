package ru.itmo.edugoolda.core.settings

interface SettingsFactory {

    fun createSettings(name: String): Settings

    fun createEncryptedSettings(name: String): Settings
}
