package ru.itmo.edugoolda.data.lesson.lesson_create.internal

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import ru.itmo.edugoolda.data.lesson.lesson_create.internal.dto.CreateLessonRequest
import ru.itmo.edugoolda.data.lesson.lesson_details.internal.dto.LessonFullDetailsDTO

internal interface LessonCreateApi {
    @POST("api/v1/lesson")
    suspend fun createLesson(
        @Body createLessonRequest: CreateLessonRequest
    ): LessonFullDetailsDTO
}