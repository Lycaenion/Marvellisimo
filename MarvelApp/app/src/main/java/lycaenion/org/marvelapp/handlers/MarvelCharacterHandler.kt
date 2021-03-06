package lycaenion.org.marvelapp.handlers

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import lycaenion.org.marvelapp.models.characterModels.*
import lycaenion.org.marvelapp.services.CharacterService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MarvelCharacterHandler{


    private val service : CharacterService = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com:443/v1/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(CharacterService::class.java)

    private fun getOkHttpClient() : OkHttpClient{

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(addHashAndKey())

        return builder.build()
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

    fun getAllCharacters(offset : Int) : Single<APIResponseSearchCharacter>{
        return service.getAllCharacters(offset).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error : ${it.message}")
            APIResponseSearchCharacter(1, "", CharacterSearchData(emptyArray()))
        }
    }

    fun getCharacter( id : Int) : Single<APIResponseCharacter>{
        return service.getCharacter(id).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error: ${it.message}")
            APIResponseCharacter(1, "", CharacterData(emptyArray()))
        }
    }

    fun getSeriesWithCharacter( id : Int, offset : Int) : Single<APIResponseCharacterSeries>{
        return service.getCharacterSeries(id, offset).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error: ${it.message}")
            APIResponseCharacterSeries(1, "", CharacterSeriesData(emptyArray()))
        }
    }

    fun searchCharacter(name : String, offset: Int) : Single<APIResponseSearchCharacter>{
        return service.searchCharacter(name, offset).subscribeOn(Schedulers.io()).retry(10).onErrorReturn {
            println("error:  ${it.message}")
            APIResponseSearchCharacter(1, "", CharacterSearchData(emptyArray()))
        }
    }
}
