package lycaenion.org.marvelapp.handlers

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import lycaenion.org.marvelapp.models.APIResponseSearchCharacter
import lycaenion.org.marvelapp.services.CharacterService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MarvelCharacterHandler{


    val service : CharacterService = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com:443/v1/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(CharacterService::class.java)

    fun getOkHttpClient() : OkHttpClient{
        //val logging = HttpLoggingInterceptor()
        //logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(addHashAndKey())
        //builder.addInterceptor(logging)

        val okHttpClient = builder.build()

        return okHttpClient
    }

    fun addHashAndKey() : Interceptor = Interceptor { chain ->

        val params = mapOf("ts" to "1", "apikey" to "106a310a39bc5458faa6c5d83f88c5dd" , "hash" to "55e291925d0578bd9b1ec729de47188f")

        val originalRequest = chain.request()
        val urlWithParams = originalRequest.url().newBuilder()
            .apply { params.forEach{addQueryParameter(it.key, it.value)}}
            .build()

        val newRequest = originalRequest.newBuilder().url(urlWithParams).build()
        chain.proceed(newRequest)
    }

    fun getAllCharacters() : Single<APIResponseSearchCharacter>{

        return service.getAllCharacters().subscribeOn(Schedulers.io())

    }
}
