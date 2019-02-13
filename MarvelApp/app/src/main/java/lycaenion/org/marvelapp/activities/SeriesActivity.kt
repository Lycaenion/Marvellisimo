package lycaenion.org.marvelapp.activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_series.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        btnFavorite = findViewById(R.id.add_favorite_btn)

        var series : Series

        var id : Int

        id = intent.extras.getInt("id")

        MarvelSeriesHandler.getSeries(id).observeOn(AndroidSchedulers.mainThread()).subscribe({
            response -> series = response.data.results[0]
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
            .name("favoriteSeries.realm")
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
            .name("favoriteSeries.realm")
            .build()

        var realm = Realm.getInstance(config)

        realm.executeTransaction{
            var result : RealmResults<FavoriteSeries> =
                realm.where(FavoriteSeries::class.java).equalTo("id", series.id).findAll()
            result.deleteAllFromRealm()
        }
        realm.close()

        println("Series was removed")
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

            println("Checked wiki")

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
