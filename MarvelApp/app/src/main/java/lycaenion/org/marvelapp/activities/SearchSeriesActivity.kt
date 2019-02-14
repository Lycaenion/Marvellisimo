package lycaenion.org.marvelapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import com.github.clans.fab.FloatingActionButton
import io.reactivex.android.schedulers.AndroidSchedulers
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelSeriesHandler
import lycaenion.org.marvelapp.models.seriesModels.Series
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener
import lycaenion.org.marvelapp.recyclerViewAdapters.SearchSeriesViewAdapter

class SearchSeriesActivity : AppCompatActivity() {

    private var seriesList : List<Series> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: SearchSeriesViewAdapter
    private lateinit var recyclerView : RecyclerView
    private var searchString = ""
    private lateinit var searchView : SearchView
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fabSearchCharacter : FloatingActionButton
    private lateinit var fabSearchSeries : FloatingActionButton
    private lateinit var fabAllCharacters : FloatingActionButton
    private lateinit var fabAllSeries : FloatingActionButton
    private lateinit var fabFavoriteCharacters : FloatingActionButton
    private lateinit var fabFavoriteSeries : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_series)
        initMenu()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.series_recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        searchView = findViewById(R.id.search_series)
        initAdapter()
        initScrollListener(linearLayoutManager)
        setTextListener()
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

    private fun setTextListener(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextChange(newText: String): Boolean {
                searchString = newText
                adapter.emptyList()
                addSeries(0, searchString)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                if(searchString == query){
                    return false
                }

                searchString = query!!
                adapter.emptyList()
                addSeries(search = searchString)
                return false
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(){
        MarvelSeriesHandler.getAllSeries(0).observeOn(AndroidSchedulers.mainThread()).subscribe {
                response -> seriesList = seriesList + response.data.results.asList()
            adapter = SearchSeriesViewAdapter(this, seriesList)
            recyclerView.adapter = adapter
        }
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager){
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addSeries(page*20, searchString)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    @SuppressLint("CheckResult")
    fun addSeries(offset : Int = 0, search: String){

        if(offset == 0){

            scrollListener.resetState()
            adapter.emptyList()
            seriesList = emptyList()

        }

        if(search.isEmpty()){
            MarvelSeriesHandler.getAllSeries(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { response -> seriesList = seriesList + response.data.results.asList()
                adapter.addSeries(seriesList)
            }
        }else{
            MarvelSeriesHandler.searchSeries(searchString, offset).observeOn(AndroidSchedulers.mainThread()).subscribe { response -> seriesList = seriesList + response.data.results.asList()
                adapter.addSeries(seriesList)
                adapter.notifyDataSetChanged()

            }
        }
    }
}
