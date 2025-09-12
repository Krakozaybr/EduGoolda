package ru.itmo.edugoolda.data.lesson.lesson_details.internal

import me.aartikov.replica.algebra.normal.map
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.keyed.KeyedReplicaSettings
import me.aartikov.replica.single.ReplicaSettings
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonDetailsRepository
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonStatus
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.SendMessageRequest
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.SetSolutionStatusRequest
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.toDomain
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import kotlin.time.Duration.Companion.minutes

internal class LessonDetailsRepositoryImpl(
    replicaClient: ReplicaClient,
    private val lessonDetailsApi: LessonDetailsApi
) : LessonDetailsRepository {

    private val _lessonTeacherDetailsReplica = replicaClient.createKeyedReplica(
        name = "teacher lesson details replica",
        childName = {
            "child $it"
        },
        settings = KeyedReplicaSettings(maxCount = 10),
        childSettings = {
            ReplicaSettings(staleTime = 5.minutes)
        },
        fetcher = { key: LessonId ->
            lessonDetailsApi.getLessonTeacherDetails(key.value)
        }
    )

    override val lessonTeacherDetailsReplica = _lessonTeacherDetailsReplica.map { _, data -> data.toDomain() }

    private val _lessonStudentDetailsReplica = replicaClient.createKeyedReplica(
        name = "student lesson details replica",
        childName = {
            "child $it"
        },
        settings = KeyedReplicaSettings(maxCount = 10),
        childSettings = {
            ReplicaSettings(staleTime = 5.minutes)
        },
        fetcher = { key: LessonId ->
            lessonDetailsApi.getLessonStudentDetails(key.value)
        }
    )

    override val lessonStudentDetailsReplica = _lessonStudentDetailsReplica.map { _, data -> data.toDomain() }

    private val _solutionTeacherDetailsReplica = replicaClient.createKeyedReplica(
        name = "teacher solution details replica",
        childName = {
            "child $it"
        },
        settings = KeyedReplicaSettings(maxCount = 10),
        childSettings = {
            ReplicaSettings(staleTime = 5.minutes)
        },
        fetcher = { key: SolutionId ->
            lessonDetailsApi.getSolutionTeacherDetails(key.value)
        }
    )

    override val solutionTeacherDetailsReplica = _solutionTeacherDetailsReplica.map { _, data -> data.toDomain() }

    override suspend fun sendMessageByStudent(lessonId: LessonId, message: String) {
        lessonDetailsApi.sendMessageByStudent(lessonId.value, SendMessageRequest(message))
        _lessonStudentDetailsReplica.refresh(lessonId)
    }

    override suspend fun sendMessageByTeacher(solutionId: SolutionId, message: String) {
        lessonDetailsApi.sendMessageByTeacher(solutionId.value, SendMessageRequest(message))
        _solutionTeacherDetailsReplica.refresh(solutionId)
    }

    override suspend fun setSolutionStatus(solutionId: SolutionId, status: LessonStatus) {
        val actionRequest = SetSolutionStatusRequest(
            status = when (status) {
                LessonStatus.Reviewed -> "reviewed"
                LessonStatus.Pending -> "pending"
            }
        )
        lessonDetailsApi.setSolutionStatus(solutionId.value, actionRequest)
        _solutionTeacherDetailsReplica.refresh(solutionId)
    }
}