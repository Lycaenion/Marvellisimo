package lycaenion.org.marvelapp.models.characterModels

import com.google.gson.annotations.SerializedName
import lycaenion.org.marvelapp.models.Thumbnail

class SearchResultCharacter(@SerializedName("id")
                            val id : Int,

                            @SerializedName("name")
                            val name : String,

                            val thumbnail: Thumbnail
)