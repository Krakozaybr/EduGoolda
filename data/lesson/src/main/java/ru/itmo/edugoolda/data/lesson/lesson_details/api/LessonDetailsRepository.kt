package ru.itmo.edugoolda.data.lesson.lesson_details.api

import me.aartikov.replica.keyed.KeyedReplica
import ru.itmo.edugoolda.data.solutions.api.SolutionId

interface LessonDetailsRepository {
    val lessonStudentDetailsReplica: KeyedReplica<LessonId, LessonStudentDetails>
    val lessonTeacherDetailsReplica: KeyedReplica<LessonId, LessonFullDetails>
    val solutionTeacherDetailsReplica: KeyedReplica<SolutionId, SolutionDetails>

    suspend fun sendMessageByStudent(lessonId: LessonId, message: String)
    suspend fun sendMessageByTeacher(solutionId: SolutionId, message: String)
    suspend fun setSolutionStatus(solutionId: SolutionId, status: LessonStatus)
}