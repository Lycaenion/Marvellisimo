package lycaenion.org.marvelapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.NavUtils
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.github.clans.fab.FloatingActionButton
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_series.*
import kotlinx.android.synthetic.main.app_bar_main.*
import lycaenion.org.marvelapp.FavoriteSeriesFragment
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelSeriesHandler
import lycaenion.org.marvelapp.models.seriesModels.Series
import lycaenion.org.marvelapp.models.characterModels.SearchResultCharacter
import lycaenion.org.marvelapp.models.databaseModels.FavoriteSeries
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener
import lycaenion.org.marvelapp.recyclerViewAdapters.SeriesCharactersViewAdapter

class SeriesActivity : AppCompatActivity() {



    private var seriesCharactersList : List<SearchResultCharacter> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: SeriesCharactersViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnFavorite : Button
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var btnLearnMore : Button

    private lateinit var fabSearchCharacter : FloatingActionButton
    private lateinit var fabSearchSeries : FloatingActionButton
    private lateinit var fabAllCharacters : FloatingActionButton
    private lateinit var fabAllSeries : FloatingActionButton
    private lateinit var fabFavoriteCharacters : FloatingActionButton
    private lateinit var fabFavoriteSeries : FloatingActionButton

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
        initMenu()

        btnFavorite = findViewById(R.id.add_favorite_btn)

        var series : Series

        var id = intent.extras.getInt("id")

        MarvelSeriesHandler.getSeries(id).observeOn(AndroidSchedulers.mainThread()).subscribe {
            response -> series = response.data.results[0]
            setSeriesView(series)
        }

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.characters_in_series)
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

    private fun setSeriesView(series : Series){
        val imgUrl = series.thumbnail.path+"."+series.thumbnail.extension

        series_title.text = series.title

        text_characters_header.text = "Characters that appears in " + series.title
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(series_thumbnail)

        setSeriesDescription(series)
        setLearnMoreBtn(series)
        setFavoriteBtn(series)

    }

    private fun setFavoriteBtn(series: Series){

        if(checkIfSeriesIsFavorite(series)){
            removeFavorite(series)
        }else{
            addFavorites(series)
        }

    }

    private fun setSeriesDescription(series: Series){

        if(series.description == ""){
            series_description.text = "No description Available"
        }else{
            series_description.text = series.description
        }
    }

    private fun addFavorites(series: Series){
        btnFavorite.text = "Add to favorites"

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        btnFavorite.setOnClickListener{
            var favoriteSeries  = FavoriteSeries()

            favoriteSeries.id = series.id
            favoriteSeries.title = series.title
            favoriteSeries.imgPath = series.thumbnail.path+"."+series.thumbnail.extension

            val realm = Realm.getInstance(config)
            realm.executeTransaction {
                realm -> realm.insertOrUpdate(favoriteSeries)
            }
            realm.close()
            setFavoriteBtn(series)
            //setSeriesView(series)
            //btnFavorite.text = "Remove from favorites"
        }
    }

    private fun removeFavorite(series: Series){

        btnFavorite.text = "Remove from favorites"

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        btnFavorite.setOnClickListener {

            var realm = Realm.getInstance(config)
            realm.executeTransaction {
                it.where(FavoriteSeries::class.java)
                    .equalTo("id", series.id)
                    .findAll()
                    .deleteAllFromRealm()
            }

            realm.close()
            setFavoriteBtn(series)

            //btnFavorite.text = "Add to Favorites "

        }
    }




    private fun checkIfSeriesIsFavorite(series: Series) : Boolean{
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        var realm = Realm.getInstance(config)

        val favorite = realm.where<FavoriteSeries>(FavoriteSeries::class.java)
            .equalTo("id", series.id)
            .findFirst()
        //realm.close()

        return favorite != null

    }

    private fun setLearnMoreBtn(series : Series){

        btnLearnMore = findViewById(R.id.wiki_button)

        for(i in series.urls.indices) {

            btnLearnMore.setOnClickListener {

                var openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(series.urls[i].url)
                startActivity(openURL)

            }
        }
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager, id : Int){
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addCharacters(page * 20, id)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun addCharacters(offset : Int, id: Int){
        if(offset == 0){
            scrollListener.resetState()
            seriesCharactersList  = emptyList()
            adapter.addCharacters(seriesCharactersList)
        }else{
            MarvelSeriesHandler.getCharactersInSeries(offset, id).observeOn(Schedulers.io()).subscribe {
                response -> seriesCharactersList = seriesCharactersList + response.data.results.toList()
                adapter.addCharacters(seriesCharactersList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(id : Int){
        MarvelSeriesHandler.getCharactersInSeries(0 , id).observeOn(AndroidSchedulers.mainThread()).subscribe { response -> seriesCharactersList = seriesCharactersList + response.data.results.asList()
            adapter = SeriesCharactersViewAdapter(this, seriesCharactersList)
            recyclerView.adapter = adapter
        }
    }
}
