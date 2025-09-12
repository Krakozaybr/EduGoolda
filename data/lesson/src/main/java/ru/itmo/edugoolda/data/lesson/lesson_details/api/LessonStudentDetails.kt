package ru.itmo.edugoolda.data.lesson.lesson_details.api

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.data.user.api.UserInfo
import ru.itmo.edugoolda.data.user.api.UserRole

data class LessonStudentDetails(
    val id: LessonId,
    val name: String,
    val description: String?,
    val teacher: UserInfo,
    val deadline: Instant?,
    val groups: List<GroupInfo>,
    val messages: List<SolutionMessage>,
    val status: LessonStatus,
    val isEstimatable: Boolean,
) {
    companion object {
        val MOCK = LessonStudentDetails(
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
            groups = listOf(
                GroupInfo(
                    id = GroupId("1"),
                    name = "Group 1",
                    subjectName = "Math",
                    ownerName = "Pavel",
                    isFavourite = false
                )
            ),
            messages = listOf(
                SolutionMessage(
                    id = SolutionMessageId("1"),
                    sentAt = Instant.DISTANT_FUTURE,
                    message = "ПРИВЕТ!",
                    author = UserInfo(
                        id = UserId("1"),
                        name = "Pavel",
                        email = "pavel@mail.ru",
                        role = UserRole.Teacher,
                        isDeleted = false
                    ),
                ),
                SolutionMessage(
                    id = SolutionMessageId("1"),
                    sentAt = Instant.DISTANT_FUTURE,
                    message = "хаю хай",
                    author = UserInfo(
                        id = UserId("2"),
                        name = "Pavel2",
                        email = "pavel@mail.ru",
                        role = UserRole.Student,
                        isDeleted = false
                    ),
                ),
                SolutionMessage(
                    id = SolutionMessageId("1"),
                    sentAt = Instant.DISTANT_FUTURE,
                    message = "твое решение не очень если честно, перерешай быстро, я потом проверю или нет",
                    author = UserInfo(
                        id = UserId("1"),
                        name = "Pavel",
                        email = "pavel@mail.ru",
                        role = UserRole.Teacher,
                        isDeleted = false
                    ),
                )
            ),
            status = LessonStatus.Pending,
            isEstimatable = true
        )
    }
}