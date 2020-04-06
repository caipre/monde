package co.nickp.monde.conduit.http

import co.nickp.monde.conduit.schemas.MultipleArticlesResponse
import co.nickp.monde.conduit.schemas.NewArticleRequest
import co.nickp.monde.conduit.schemas.SingleArticleResponse
import co.nickp.monde.conduit.schemas.UpdateArticleRequest
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticlesApi {
    @GET("articles/feed")
    fun feed(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Single<Response<MultipleArticlesResponse>>

    @GET("articles")
    fun fetch(
        @Query("tag") tag: String? = null,
        @Query("author") author: String? = null,
        @Query("favorited") favorited: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Single<Response<MultipleArticlesResponse>>

    @POST("articles")
    fun create(@Body article: NewArticleRequest):
            Single<Response<SingleArticleResponse>>

    @GET("articles/{slug}")
    fun get(@Path("slug") slug: String):
            Single<Response<SingleArticleResponse>>

    @PUT("articles/{slug}")
    fun update(
        @Path("slug") slug: String,
        @Body article: UpdateArticleRequest
    ): Single<Response<SingleArticleResponse>>

    @DELETE("articles/{slug}")
    fun delete(@Path("slug") slug: String):
            Completable
}

