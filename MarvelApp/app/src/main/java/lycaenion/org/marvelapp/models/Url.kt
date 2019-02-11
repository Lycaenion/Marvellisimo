package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class Url(
    @SerializedName("type")
    val type : String,
    @SerializedName("url")
    val url : String) {
}