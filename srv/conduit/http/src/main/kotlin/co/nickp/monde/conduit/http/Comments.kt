package co.nickp.monde.conduit.http

import co.nickp.monde.conduit.schemas.MultipleCommentResponse
import co.nickp.monde.conduit.schemas.NewCommentRequest
import co.nickp.monde.conduit.schemas.SingleCommentResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentsApi {
    @GET("articles/{slug}/comments")
    fun get(@Path("slug") slug: String):
            Single<Response<MultipleCommentResponse>>

    @POST("articles/{slug}/comments")
    fun comment(@Path("slug") slug: String, @Body body: NewCommentRequest):
            Single<Response<SingleCommentResponse>>

    @DELETE("articles/{slug}/comments/{id}")
    fun remove(@Path("slug") slug: String, @Path("id") id: Int):
            Completable
}
