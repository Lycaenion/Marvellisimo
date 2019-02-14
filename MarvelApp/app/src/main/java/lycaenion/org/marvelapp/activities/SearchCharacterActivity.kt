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
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.characterModels.SearchResultCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.SearchCharactersViewAdapter
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener

class SearchCharacterActivity : AppCompatActivity() {

    private var characterList : List<SearchResultCharacter> = emptyList()
    private lateinit var scrollListener : EndlessRecyclerViewScrollListener
    private lateinit var adapter : SearchCharactersViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var searchString  = ""
    private lateinit var searchView : SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_character)

        initMenu()

        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.character_recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        searchView = findViewById(R.id.search_character)
        initAdapter()
        initScrollListener(linearLayoutManager)
        setTextListener()
    }


    private fun initMenu(){

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

    }

    private fun setTextListener(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {
                searchString = newText
                adapter.emptyList()
                addCharacters(0, searchString)
                return false

                /*if(newString!!.isEmpty()){
                    searchString = newString
                }else{
                    resetAdapter()
                    searchString = newString
                    addCharacters(0, searchString)
                    resetAdapter()
                }
                
                if(searchString.equals("")){
                    resetAdapter()
                    addCharacters(0, searchString)
                }else{
                    resetAdapter()
                    addCharacters(0, searchString)
                }*/
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                if(searchString == query){
                    return false
                }

                searchString = query!!
                adapter.emptyList()
                addCharacters(search = searchString)
                return false

                /*if(query!!.isEmpty()){
                    searchString = ""
                }else{
                    searchString = query!!
                }

                if(searchString.equals("")){
                    resetAdapter()
                    addCharacters(0, searchString)
                }else{
                    resetAdapter()
                    addCharacters(0, searchString)
                    resetAdapter()
                }*/
                return false
            }
        })
    }

    private fun resetAdapter(){
        scrollListener.resetState()
        adapter.emptyList()
        characterList = emptyList()
        adapter.addCharacters(characterList)
        //adapter.notifyDataSetChanged()
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(){
        MarvelCharacterHandler.getAllCharacters(0).observeOn(AndroidSchedulers.mainThread()).subscribe {
                response -> characterList = characterList + response.data.results.asList()
            adapter = SearchCharactersViewAdapter(this, characterList)
            recyclerView.adapter = adapter
        }
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager){
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addCharacters(page * 20, searchString)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    @SuppressLint("CheckResult")
    fun addCharacters(offset : Int = 10, search: String){
            if (offset == 0){
                scrollListener.resetState()
                adapter.emptyList()
                characterList = emptyList()
                /*MarvelCharacterHandler.getAllCharacters(0).observeOn(AndroidSchedulers.mainThread()).subscribe { response -> characterList = characterList + response.data.results.asList()
                    adapter.addCharacters(characterList)
                    adapter.notifyDataSetChanged()
                }*/
            }

            if(search.isEmpty()){
                //if this function stops working, add following rows here:
                //var currentSize : Int
                //currentSize = adapter.itemCount
                MarvelCharacterHandler.getAllCharacters(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { response -> characterList = characterList + response.data.results.asList()
                    adapter.addCharacters(characterList)
                    //adapter.notifyDataSetChanged()
                }
            }else{
                MarvelCharacterHandler.searchCharacter(search, offset).observeOn(AndroidSchedulers.mainThread()).subscribe { response -> characterList = characterList + response.data.results.asList()
                    adapter.addCharacters(characterList)
                    //println(characterList.size)
                    adapter.notifyDataSetChanged()
                }
        }
    }
}
