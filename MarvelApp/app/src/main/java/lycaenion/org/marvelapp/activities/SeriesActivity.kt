package lycaenion.org.marvelapp.activities

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
    lateinit var favoriteFragment : FavoriteSeriesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        var fabSearchCharacter = findViewById<FloatingActionButton>(R.id.nav_search_character)
        var fabSearchSeries = findViewById<FloatingActionButton>(R.id.nav_search_series)
        var fabAllCharacters = findViewById<FloatingActionButton>(R.id.nav_all_characters)
        var fabAllSeries = findViewById<FloatingActionButton>(R.id.nav_all_series)
        var fabFavoriteCharacters = findViewById<FloatingActionButton>(R.id.nav_show_favorite_characters)
        var fabFavoriteSeries = findViewById<FloatingActionButton>(R.id.nav_show_favorite_series)

        fabSearchCharacter.setOnClickListener {
                view -> startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabSearchSeries.setOnClickListener {
                view -> startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabAllCharacters.setOnClickListener {
                view -> startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabAllSeries.setOnClickListener {
                view -> startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabFavoriteCharacters.setOnClickListener {
                view -> startActivity(Intent(this, FavoriteCharactersActivity::class.java))
        }

        fabFavoriteSeries.setOnClickListener {
                view -> startActivity(Intent(this, FavoriteSeriesActivity::class.java))
        }


        btnFavorite = findViewById(R.id.add_favorite_btn)

        /*val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)*/

        var series : Series

        var id : Int

        id = intent.extras.getInt("id")

        MarvelSeriesHandler.getSeries(id).observeOn(AndroidSchedulers.mainThread()).subscribe({
            response -> series = response.data.results[0]
            println(response.code)
            setSeriesView(series)
        })

        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.characters_in_series)
        recyclerView.layoutManager = linearLayoutManager
        initAdapter(id)
        initScrollListener(linearLayoutManager, id)


    }

    fun setSeriesView(series : Series){
        val imgUrl = series.thumbnail.path+"."+series.thumbnail.extension

        series_title.text = series.title

        text_characters_header.text = "Characters that appears in " + series.title
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(series_thumbnail)

        println("I got here")
        setSeriesDescription(series)
        setLearnMoreBtn(series)
        setFavoriteBtn(series)
    }

    fun setFavoriteBtn(series: Series){
        if(checkIfSeriesIsFavorite(series)){
            btnFavorite.text = "Remove from favorites"
            removeFavorite(series)
        }else{
            addFavorites(series)
        }

    }

    fun addFavorites(series: Series){
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

            println("Adding " + favoriteSeries.title + " to db")

            val realm = Realm.getInstance(config)
            realm.executeTransaction {
                realm -> realm.insertOrUpdate(favoriteSeries)
            }
            realm.close()
            btnFavorite.text = "Remove from favorites"
        }
    }

    private fun removeFavorite(series: Series){
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        var realm = Realm.getInstance(config)

        realm.executeTransaction{
            var result : RealmResults<FavoriteSeries> =
                realm.where(FavoriteSeries::class.java).equalTo("id", series.id).findAll()
            result.deleteAllFromRealm()
        }
        realm.close()
        setSeriesView(series)

        btnFavorite.text = "Add to favorites"

    }

    private fun setSeriesDescription(series: Series){
        if(series.description == ""){
            series_description.text = "No description Available"
        }else{
            series_description.text = series.description
        }
    }

    private fun checkIfSeriesIsFavorite(series: Series) : Boolean{
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favoriteSeries.realm")
            .build()

        var realm = Realm.getInstance(config)

        var favoriteSeries : FavoriteSeries? = realm.where(FavoriteSeries::class.java)
            .equalTo("id", series.id)
            .findFirst()
        realm.close()

        return favoriteSeries != null

    }

    private fun setLearnMoreBtn(series : Series){
        val btn_learn_more = findViewById<Button>(R.id.wiki_button)

        for(i in series.urls.indices) {

            btn_learn_more.setOnClickListener {

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

    fun addCharacters(offset : Int, id: Int){
        if(offset == 0){
            scrollListener.resetState()
            seriesCharactersList  = emptyList()
            adapter.addCharacters(seriesCharactersList)
        }else{
            MarvelSeriesHandler.getCharactersInSeries(offset, id).observeOn(Schedulers.io()).subscribe({
                response -> seriesCharactersList = seriesCharactersList + response.data.results.toList()
                adapter.addCharacters(seriesCharactersList)
                adapter.notifyDataSetChanged()
            })
        }
    }

    private fun initAdapter(id : Int){
        MarvelSeriesHandler.getCharactersInSeries(0 , id).observeOn(AndroidSchedulers.mainThread()).subscribe({
                response -> seriesCharactersList = seriesCharactersList + response.data.results.asList()
            adapter = SeriesCharactersViewAdapter(this, seriesCharactersList)
            recyclerView.adapter = adapter
        })
    }




}
