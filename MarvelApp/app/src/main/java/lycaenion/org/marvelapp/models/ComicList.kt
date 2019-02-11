package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class ComicList(@SerializedName ("collectionURI")
                val collectionURI : String,

                val list : Array<Item>)