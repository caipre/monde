package co.nickp.monde

import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

data class AppContext(
    val okhttp: OkHttpClient,
    val retrofit: Retrofit,
    val moshi: Moshi
) {
    companion object {
        fun create(cacheDir: File): AppContext {
            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)

            val okhttp = OkHttpClient.Builder()
                .addInterceptor(logging)
                .cache(Cache(cacheDir, 1024L * 1024L))
                .callTimeout(2500, TimeUnit.MILLISECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://conduit.productionready.io/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val moshi = Moshi.Builder()
                .build()

            return AppContext(okhttp, retrofit, moshi)
        }
    }
}
