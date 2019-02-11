package lycaenion.org.marvelapp.models.characterModels

import com.google.gson.annotations.SerializedName

class CharacterSearchData(
                    @SerializedName("total")
                    val total : Int,

                    @SerializedName("results")
                    val results : Array<SearchResultCharacter>)