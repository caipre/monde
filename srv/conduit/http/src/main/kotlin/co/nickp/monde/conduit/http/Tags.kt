package co.nickp.monde.conduit.http

import co.nickp.monde.conduit.schemas.TagsResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface TagsApi {
    @GET("tags")
    fun get(): Single<Response<TagsResponse>>
}
