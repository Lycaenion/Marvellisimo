package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class Character(@SerializedName("id")
                val id: Int,

                @SerializedName("name")
                val name : String,

                @SerializedName("description")
                val description : String,

                val thumbnail : Thumbnail)