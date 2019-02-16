package lycaenion.org.marvelapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionButton
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
    private var imgUrl = ""
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: CharacterSeriesViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnFavorite : Button
    private lateinit var btnLearnMore : Button
    private lateinit var linearLayoutManager: LinearLayoutManager


    private lateinit var fabSearchCharacter : FloatingActionButton
    private lateinit var fabSearchSeries : FloatingActionButton
    private lateinit var fabAllCharacters : FloatingActionButton
    private lateinit var fabAllSeries : FloatingActionButton
    private lateinit var fabFavoriteCharacters : FloatingActionButton
    private lateinit var fabFavoriteSeries : FloatingActionButton

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        initMenu()

        btnFavorite = findViewById(R.id.add_favorite_btn)

        var character : Character

        var id = intent.extras.getInt("id")

        MarvelCharacterHandler.getCharacter(id).observeOn(AndroidSchedulers.mainThread()).subscribe {
            response -> character = response.data.results[0]
            setCharacterView(character)
        }

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.character_series)
        recyclerView.layoutManager = linearLayoutManager
        initAdapter(id)
        initScrollListener(linearLayoutManager, id)

    }

    private fun initMenu(){

        fabSearchCharacter = findViewById(R.id.nav_search_character)
        fabSearchSeries = findViewById(R.id.nav_search_series)
        fabAllCharacters = findViewById(R.id.nav_all_characters)
        fabAllSeries = findViewById(R.id.nav_all_series)
        fabFavoriteCharacters = findViewById(R.id.nav_show_favorite_characters)
        fabFavoriteSeries = findViewById(R.id.nav_show_favorite_series)

        fabSearchCharacter.setOnClickListener {
            startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabSearchSeries.setOnClickListener {
            startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabAllCharacters.setOnClickListener {
            startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabAllSeries.setOnClickListener {
            startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabFavoriteCharacters.setOnClickListener {
            startActivity(Intent(this, FavoriteCharactersActivity::class.java))
        }
        fabFavoriteSeries.setOnClickListener {
            startActivity(Intent(this, FavoriteSeriesActivity::class.java))
        }
    }

    private fun setCharacterView(character : Character){

        imgUrl = character.thumbnail.path + "."+ character.thumbnail.extension

        character_name.text = character.name

        text_series_header.text = "Series where " + character.name + " appears"
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(character_thumbnail)

        setCharacterDescription(character)
        setLearnMoreBtn(character)
        setFavoriteBtn(character)

    }

    private fun setFavoriteBtn(character: Character){

        if(checkIfCharacterIsFavorite(character)){
            removeFavorite(character)
        }else{
            addFavorite(character)
        }
    }

    private fun setCharacterDescription(character : Character){

        if(character.description == ""){
            character_description.text = "No description Available"
        }else{
            character_description.text = character.description
        }
    }

    private fun addFavorite(character: Character){
        btnFavorite.text = "Add to favorites"

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

            var realm = Realm.getInstance(config)
            realm.executeTransaction{
                    realm -> realm.insertOrUpdate(favoriteCharacter) }
            realm.close()

            setFavoriteBtn(character)

        }
    }

    private fun removeFavorite(character : Character){

        btnFavorite.text = "Remove from favorites"

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        btnFavorite.setOnClickListener{

            var realm = Realm.getInstance(config)

            realm.executeTransaction{
               it.where(FavoriteCharacter::class.java)
                   .equalTo("id", character.id)
                   .findAll().deleteAllFromRealm()
            }
            realm.close()
            setFavoriteBtn(character)
        }
    }

    private fun checkIfCharacterIsFavorite(character: Character) : Boolean{
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()
        var realm = Realm.getInstance(config)

        val favorite   = realm.where(FavoriteCharacter::class.java)
            .equalTo("id", character.id)
            .findFirst()
        //realm.close()

        return favorite != null
    }

    private fun setLearnMoreBtn(character: Character){
        btnLearnMore = findViewById<Button>(R.id.wiki_button)

        for(i in character.urls.indices) {
            if (character.urls[i].type == "wiki") {
                println("Checked wiki")

                btnLearnMore.setOnClickListener {

                    var openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(character.urls[i].url)
                    startActivity(openURL)
                }

                btnLearnMore.visibility = View.VISIBLE
                break

            }else{
                btnLearnMore.visibility = View.INVISIBLE
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

    @SuppressLint("CheckResult")
    fun addSeries(offset : Int, id : Int){
        if(offset == 0){
            scrollListener.resetState()
            characterSeriesList = emptyList()
            adapter.addSeries(characterSeriesList)
        }else{
            MarvelCharacterHandler.getSeriesWithCharacter(id, offset).observeOn(AndroidSchedulers.mainThread()).subscribe {
                response -> characterSeriesList = characterSeriesList + response.data.results.asList()
                adapter.addSeries(characterSeriesList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(id : Int){
        MarvelCharacterHandler.getSeriesWithCharacter(id, 0).observeOn(AndroidSchedulers.mainThread()).subscribe {
                response -> characterSeriesList = characterSeriesList + response.data.results.asList()
            adapter = CharacterSeriesViewAdapter(this, characterSeriesList)
            recyclerView.adapter = adapter
        }
    }
}
