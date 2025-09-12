package ru.itmo.edugoolda.data.join_requests.internal

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import ru.itmo.edugoolda.data.join_requests.internal.dto.JoinRequestListResponse
import ru.itmo.edugoolda.data.join_requests.internal.dto.JoinRequestResponseRequest

interface JoinRequestApi {
    @PUT("api/v1/join_request/{requestUuid}")
    suspend fun respondToJoinRequest(
        @Path("requestUuid") requestId: String,
        @Body request: JoinRequestResponseRequest
    )

    @GET("api/v1/join_requests")
    suspend fun getInvitationList(
        @Query("page_size") pageSize: Int,
        @Query("page") page: Int
    ): JoinRequestListResponse
}
