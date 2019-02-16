package lycaenion.org.marvelapp.services

import io.reactivex.Single
import lycaenion.org.marvelapp.models.characterModels.APIResponseCharacter
import lycaenion.org.marvelapp.models.characterModels.APIResponseCharacterSeries
import lycaenion.org.marvelapp.models.characterModels.APIResponseSearchCharacter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService{
    @GET("characters")
    fun getAllCharacters(@Query ("offset") offset : Int) : Single<APIResponseSearchCharacter>

    @GET("characters/{characterId}")
    fun getCharacter(@Path("characterId") characterId : Int) : Single<APIResponseCharacter>

    @GET("characters/{characterId}/series")
    fun getCharacterSeries(@Path("characterId") characterId: Int, @Query("offset") offset : Int) : Single<APIResponseCharacterSeries>

    @GET("characters")
    fun searchCharacter(@Query("nameStartsWith") characterName : String, @Query("offset") offset: Int) : Single<APIResponseSearchCharacter>
}