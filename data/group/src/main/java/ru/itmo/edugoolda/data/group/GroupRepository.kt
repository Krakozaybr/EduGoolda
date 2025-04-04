package ru.itmo.edugoolda.data.group

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class GroupRepository {
    val subjects = MutableStateFlow(listOf("1","2","3"))
    suspend fun createGroup(selectedSubject: String, name: String, description: String): String {
        delay(5000)
        return "hello"
    }
}