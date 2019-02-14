package lycaenion.org.marvelapp.models.databaseModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FavoriteSeries : RealmObject(){
    @PrimaryKey
    var id : Int? = null

    var title : String? = null
    var imgPath : String? = null
}