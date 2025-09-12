package ru.itmo.edugoolda.data.lesson.lesson_info.internal

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import ru.itmo.edugoolda.data.lesson.lesson_info.internal.dto.LessonInfoListResponse

interface LessonInfoApi {

    @GET("api/v1/lessons")
    suspend fun getLessonInfoList(
        @Query("page_size") pageSize: Int,
        @Query("page") page: Int,
        @Query("group_id") groupId: String? = null,
        @Query("query") query: String? = null,
    ): LessonInfoListResponse

    @DELETE("api/v1/lesson/{lessonUuid}")
    suspend fun deleteLesson(
        @Path("lessonUuid") lessonId: String,
    )
}
