package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class SearchResultCharacter(@SerializedName("id")
                            val id : Int,

                            @SerializedName("name")
                            val name : String,

                            val thumbnail: Thumbnail)