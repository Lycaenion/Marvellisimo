package lycaenion.org.marvelapp.models

import lycaenion.org.marvelapp.models.characterModels.Character

class APIResponseSeriesCharacters(val code : Int,
                                  val status : String,
                                  val data : SeriesCharacterData) {
}