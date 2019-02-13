package lycaenion.org.marvelapp.handlers

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import lycaenion.org.marvelapp.models.APIResponseSearchSeries
import lycaenion.org.marvelapp.models.SeriesSearchData
import lycaenion.org.marvelapp.services.CharacterService
import lycaenion.org.marvelapp.services.SeriesService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MarvelSeriesHandler {

    private val service : SeriesService = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com:443/v1/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(SeriesService::class.java)

    private fun getOkHttpClient() : OkHttpClient {
        //val logging = HttpLoggingInterceptor()
        //logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(addHashAndKey())
        //builder.addInterceptor(logging)

        val okHttpClient = builder.build()

        return okHttpClient
    }

    private fun addHashAndKey() : Interceptor = Interceptor { chain ->

        val params = mapOf("ts" to "1", "apikey" to "106a310a39bc5458faa6c5d83f88c5dd" , "hash" to "55e291925d0578bd9b1ec729de47188f")

        val originalRequest = chain.request()
        val urlWithParams = originalRequest.url().newBuilder()
            .apply { params.forEach{addQueryParameter(it.key, it.value)}}
            .build()

        val newRequest = originalRequest.newBuilder().url(urlWithParams).build()
        chain.proceed(newRequest)
    }

    /*fun getAllCharacters(offset : Int) : Single<APIResponseSearchCharacter>{
        return service.getAllCharacters(offset).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error : ${it.message}")
            APIResponseSearchCharacter(1, "", CharacterSearchData(emptyArray()))
        }
    }*/

    fun getAllSeries(offset : Int) : Single<APIResponseSearchSeries>{
        return service.getAllSeries(offset).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error : ${it.message}")
            APIResponseSearchSeries(1, "", SeriesSearchData(emptyArray()))
        }
    }

    fun searchSeries(title : String) : Single<APIResponseSearchSeries>{
        return service.searchSeries(title).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error : ${it.message}")
            APIResponseSearchSeries(1, "", SeriesSearchData(emptyArray()))
        }
    }
}