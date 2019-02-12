package lycaenion.org.marvelapp.models.characterModels

import com.google.gson.annotations.SerializedName
import lycaenion.org.marvelapp.models.Thumbnail
import lycaenion.org.marvelapp.models.Url

class Character(@SerializedName("id")
                val id: Int,

                @SerializedName("name")
                val name : String,

                @SerializedName("description")
                val description : String,

                val thumbnail : Thumbnail,

                @SerializedName("urls")
                val urls: Array<Url>)