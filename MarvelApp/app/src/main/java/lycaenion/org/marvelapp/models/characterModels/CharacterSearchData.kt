package lycaenion.org.marvelapp.models.characterModels

import com.google.gson.annotations.SerializedName

class CharacterSearchData(
                    @SerializedName("results")
                    val results : Array<SearchResultCharacter>)