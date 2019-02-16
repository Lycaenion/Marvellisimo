package lycaenion.org.marvelapp.services

import io.reactivex.Single
import lycaenion.org.marvelapp.models.seriesModels.APIResponseSearchSeries
import lycaenion.org.marvelapp.models.seriesModels.APIResponseSeries
import lycaenion.org.marvelapp.models.seriesModels.APIResponseSeriesCharacters
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesService {

    @GET("series")
    fun getAllSeries(@Query("offset") offset : Int) : Single<APIResponseSearchSeries>

    @GET("series/{seriesId}")
    fun getSeries(@Path("seriesId") seriesId : Int) : Single<APIResponseSeries>

    @GET("series/{seriesId}/characters")
    fun getSeriesCharacters(@Path("seriesId") seriesId: Int, @Query("offset") offset: Int) : Single<APIResponseSeriesCharacters>

    @GET("series")
    fun searchSeries(@Query("titleStartsWith") seriesName : String, @Query("offset") offset : Int) : Single<APIResponseSearchSeries>
}