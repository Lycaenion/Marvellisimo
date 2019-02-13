package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName
import lycaenion.org.marvelapp.models.characterModels.Character
import lycaenion.org.marvelapp.models.characterModels.SearchResultCharacter

class SeriesCharacterData(@SerializedName("results")
                          val results : Array<SearchResultCharacter>) {
}