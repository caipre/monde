package co.nickp.monde.conduit.adapter

import co.nickp.monde.conduit.schemas.GenericErrorModel
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.squareup.moshi.JsonAdapter
import retrofit2.Response

fun <T> Response<T>.toResult(adapter: JsonAdapter<GenericErrorModel>):
    Result<T, GenericErrorModel> =
    if (isSuccessful) Ok(body()!!)
    else Err(adapter.fromJson(errorBody()?.source()!!)!!)
