package lycaenion.org.marvelapp.models.databaseModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class FavoriteCharacter : RealmObject() {
    @PrimaryKey
    var id : Int? = null
    var name : String? = null
    var imgPath: String? = null
}