package co.nickp.monde.conduit.http

import co.nickp.monde.conduit.schemas.SingleArticleResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoritesApi {
    @POST("articles/{slug}/favorite")
    fun favorite(@Path("slug") slug: String):
            Single<Response<SingleArticleResponse>>

    @DELETE("articles/{slug}/favorite")
    fun remove(@Path("slug") slug: String):
            Single<Response<SingleArticleResponse>>
}
