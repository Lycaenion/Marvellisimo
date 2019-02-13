package lycaenion.org.marvelapp.activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_character.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.characterModels.Character
import lycaenion.org.marvelapp.models.seriesModels.Series
import lycaenion.org.marvelapp.models.databaseModels.FavoriteCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.CharacterSeriesViewAdapter
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener

class CharacterActivity : AppCompatActivity() {

    private var characterSeriesList : List<Series> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: CharacterSeriesViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var realm : Realm
    private lateinit var btnFavorite : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        btnFavorite = findViewById(R.id.add_favorite_btn)

        var character : Character

        var id : Int

        id = intent.extras.getInt("id")

        MarvelCharacterHandler.getCharacter(id).observeOn(AndroidSchedulers.mainThread()).subscribe({
                data -> character = data.data.results[0]
            setCharacterView(character)
        })

        
        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.character_series)
        recyclerView.layoutManager = linearLayoutManager
        initAdapter(id)
        initScrollListener(linearLayoutManager, id)

    }

    private fun setCharacterView(character : Character){

        val imgUrl = character.thumbnail.path + "."+ character.thumbnail.extension

        character_name.text = character.name

        text_series_header.text = "Series where " +character.name +" appears"
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(character_thumbnail)

        setCharacterDescription(character)
        setLearnMoreBtn(character)
        setFavoriteBtn(character)

    }

    private fun setCharacterDescription(character : Character){

        if(character.description == ""){
            character_description.text = "No description Available"
        }else{
            character_description.text = character.description
        }
    }

    private fun addFavorite(character: Character){

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        btnFavorite.setOnClickListener{

            var favoriteCharacter = FavoriteCharacter()

            favoriteCharacter.id = character.id
            favoriteCharacter.name = character.name
            favoriteCharacter.imgPath = character.thumbnail.path + "." + character.thumbnail.extension

            println("Adding " + favoriteCharacter.name + " to db")

            realm = Realm.getInstance(config)

            realm.executeTransaction{
                    realm -> realm.insertOrUpdate(favoriteCharacter) }
            realm.close()

            setCharacterView(character)

            btnFavorite.text = "Remove from favorites"

        }
    }

    private fun removeFavorite(character : Character){

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        btnFavorite.setOnClickListener{
            realm = Realm.getInstance(config)

            realm.executeTransaction{
                var result : RealmResults<FavoriteCharacter> =
                    realm.where(FavoriteCharacter::class.java).equalTo("id", character.id).findAll()
                result.deleteAllFromRealm()
            }
            realm.close()

            println("character was removed")

            setCharacterView(character)

            btnFavorite.text = "Add to favorites"
        }
    }

    private fun setFavoriteBtn(character: Character){

        if(checkIfCharacterIsFavorite(character)){
            btnFavorite.text = "Remove from favorites"
            removeFavorite(character)
        }else{
            addFavorite(character)
        }
    }



    private fun checkIfCharacterIsFavorite(character: Character) : Boolean{
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()
        realm = Realm.getInstance(config)

        var favoriteCharacter  : FavoriteCharacter? = realm.where(FavoriteCharacter::class.java)
            .equalTo("id", character.id)
            .findFirst()
        //realm.close()

        return character.id == favoriteCharacter?.id
    }

    fun setLearnMoreBtn(character: Character){
        val btn_learn_more = findViewById<Button>(R.id.wiki_button)

        for(i in character.urls.indices) {
            if (character.urls[i].type == "wiki") {
                println("Checked wiki")

                btn_learn_more.setOnClickListener {

                    var openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(character.urls[i].url)
                    startActivity(openURL)
                }

                btn_learn_more.visibility = View.VISIBLE
                break

            }else{
                btn_learn_more.visibility = View.INVISIBLE
            }
        }
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager, id : Int){
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addSeries(page * 20, id)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    fun addSeries(offset : Int, id : Int){
        if(offset == 0){
            scrollListener.resetState()
            characterSeriesList = emptyList()
        }else{
            MarvelCharacterHandler.getSeriesWithCharacter(id, offset).observeOn(AndroidSchedulers.mainThread()).subscribe({
                response -> characterSeriesList = characterSeriesList + response.data.results.asList()
                adapter.addSeries(characterSeriesList)
                adapter.notifyDataSetChanged()
            })
        }
    }

    private fun initAdapter(id : Int){
        MarvelCharacterHandler.getSeriesWithCharacter(id, 0).observeOn(AndroidSchedulers.mainThread()).subscribe({
            response -> characterSeriesList = characterSeriesList + response.data.results.asList()
            adapter = CharacterSeriesViewAdapter(this, characterSeriesList)
            recyclerView.adapter = adapter
        })
    }
}
