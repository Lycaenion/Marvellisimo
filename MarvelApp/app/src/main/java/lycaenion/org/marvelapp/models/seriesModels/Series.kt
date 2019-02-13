package lycaenion.org.marvelapp.models.seriesModels

import lycaenion.org.marvelapp.models.Thumbnail
import lycaenion.org.marvelapp.models.Url

class Series(val id : Int,
             val title : String,
             val description : String,
             val startYear : Int,
             val endYear : Int,
             val thumbnail: Thumbnail,
             val urls : Array<Url>) {
}