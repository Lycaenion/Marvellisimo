package lycaenion.org.marvelapp.services

import retrofit2.http.GET
import retrofit2.http.Query

interface ComicService{
    @GET("comics")
    fun getAllComics()

    @GET("comics")
    fun searchComic(@Query("titleStartsWith") comicTitle : String)
}