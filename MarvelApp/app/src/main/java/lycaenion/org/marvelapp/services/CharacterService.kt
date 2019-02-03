package lycaenion.org.marvelapp.services

import io.reactivex.Single
import lycaenion.org.marvelapp.models.APIResponseCharacter
import lycaenion.org.marvelapp.models.APIResponseSearchCharacter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService{
    @GET("characters")
    fun getAllCharacters() : Single<APIResponseSearchCharacter>

    /*@GET("characters/{characterId}")
    fun getCharacter(@Path("characterId") characterId : String) : APIResponseCharacter

    @GET("characters")
    fun searchCharacter(@Query("nameStartsWith") characterName : String) : APIResponseSearchCharacter*/
}