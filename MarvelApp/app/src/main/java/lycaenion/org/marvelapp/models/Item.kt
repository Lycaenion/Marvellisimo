package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class Item(@SerializedName("resourceURI")
           val resourceURI : String,

           @SerializedName("name")
           val name : String)