package ru.itmo.edugoolda.data.lesson.lesson_details.api

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.itmo.edugoolda.data.group.group_info.api.GroupInfo
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.data.user.api.UserId
import ru.itmo.edugoolda.data.user.api.UserInfo
import ru.itmo.edugoolda.data.user.api.UserRole

data class SolutionDetails(
    val id: SolutionId,
    val lesson: LessonGeneralDetails,
    val messages: List<SolutionMessage>,
    val status: LessonStatus,
) {
    companion object {
        val MOCK = SolutionDetails(
            id = SolutionId("1"),
            lesson = LessonGeneralDetails(
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
                isEstimatable = true
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
            status = LessonStatus.Pending
        )
    }
}