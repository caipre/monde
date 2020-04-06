package co.nickp.monde.conduit.http

import co.nickp.monde.conduit.schemas.LoginUserRequest
import co.nickp.monde.conduit.schemas.NewUserRequest
import co.nickp.monde.conduit.schemas.UpdateUserRequest
import co.nickp.monde.conduit.schemas.UserResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UsersApi {
    @POST("users/login")
    fun login(@Body body: LoginUserRequest): Single<Response<UserResponse>>

    @GET("users")
    fun get(): Single<Response<UserResponse>>

    @PUT("users")
    fun update(@Body body: UpdateUserRequest): Single<Response<UserResponse>>

    @POST("users")
    fun register(@Body body: NewUserRequest): Single<Response<UserResponse>>
}
