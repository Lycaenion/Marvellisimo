package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class Thumbnail(@SerializedName("path")
                val path : String,

                @SerializedName("extension")
                val extension : String)