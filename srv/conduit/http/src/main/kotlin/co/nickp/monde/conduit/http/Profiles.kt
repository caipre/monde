package co.nickp.monde.conduit.http

import co.nickp.monde.conduit.schemas.ProfileResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfilesApi {
    @GET("profiles/{username}")
    fun get(@Path("username") username: String):
            Single<Response<ProfileResponse>>

    @POST("profiles/{username}/follow")
    fun follow(@Path("username") username: String):
            Single<Response<ProfileResponse>>

    @DELETE("profiles/{username}/follow")
    fun unfollow(@Path("username") username: String):
            Single<Response<ProfileResponse>>

}
