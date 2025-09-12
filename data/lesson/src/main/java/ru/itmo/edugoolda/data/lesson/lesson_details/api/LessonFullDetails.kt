package ru.itmo.edugoolda.data.lesson.lesson_details.api

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.data.user.api.UserInfo
import ru.itmo.edugoolda.data.user.api.UserRole

data class LessonFullDetails(
    val id: LessonId,
    val name: String,
    val description: String,
    val teacher: UserInfo,
    val deadline: Instant?,
    val opensAt: Instant?,
    val groups: List<GroupInfo>,
    val solutionsCount: Int,
    val isEstimatable: Boolean,
) {
    companion object {
        val MOCK = LessonFullDetails(
            id = LessonId("1"),
            name = "Lesson 1",
            description = "DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS",
            teacher = UserInfo(
                id = UserId("1"),
                name = "Pavel",
                email = "pavel@mail.ru",
                role = UserRole.Teacher,
                isDeleted = false
            ),
            deadline = Clock.System.now(),
            opensAt = Clock.System.now(),
            groups = listOf(
                GroupInfo(
                    id = GroupId("1"),
                    name = "Group 1",
                    subjectName = "Math",
                    ownerName = "Pavel",
                    isFavourite = false
                )
            ),
            solutionsCount = 5,
            isEstimatable = true,
        )
    }
}
