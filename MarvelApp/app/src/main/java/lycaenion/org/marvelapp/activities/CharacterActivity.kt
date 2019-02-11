package lycaenion.org.marvelapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.Character

class CharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        var character : Character

        var id = intent.extras.getInt("id")

        MarvelCharacterHandler.getCharacter(id).observeOn(AndroidSchedulers.mainThread()).subscribe(){
            data -> character = data.data.results[0]
            setCharacterView(character)
        }




    }

    fun setCharacterView(character : Character){

        val imgUrl = character.thumbnail.path + "."+ character.thumbnail.extension

        character_name.text = character.name
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(character_thumbnail)
    }
}
