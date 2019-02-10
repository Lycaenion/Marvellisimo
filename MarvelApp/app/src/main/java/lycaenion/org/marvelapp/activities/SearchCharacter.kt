package lycaenion.org.marvelapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.SearchResultCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.CharactersViewAdapter
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener

class SearchCharacter : AppCompatActivity() {

    private var characterList : List<SearchResultCharacter> = emptyList()
    private lateinit var scrollListener : EndlessRecyclerViewScrollListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_character)
        val recyclerView : RecyclerView = findViewById(R.id.character_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        initAdapter(recyclerView)
        initScrollListner(LinearLayoutManager(this), recyclerView)
    }

    private fun initAdapter(recyclerView : RecyclerView){

        MarvelCharacterHandler.getAllCharacters(0).observeOn(AndroidSchedulers.mainThread()).subscribe({
            response -> characterList = characterList + response.data.results.asList()
            var adapter = CharactersViewAdapter(this, characterList)
            recyclerView.adapter = adapter
        })


    }

    fun initScrollListner(linearLayoutManager: LinearLayoutManager, recyclerView : RecyclerView){
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addCharacters(characterList.size + 1, recyclerView)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    fun addCharacters(offset : Int, recyclerView : RecyclerView){
        if (offset == 0){
            scrollListener.resetState()
            characterList = emptyList()
        }else{
            MarvelCharacterHandler.getAllCharacters(offset).observeOn(AndroidSchedulers.mainThread()).subscribe({
                response -> characterList = characterList + response.data.results.asList()
                var adapter = CharactersViewAdapter(this, characterList)
                recyclerView.adapter = adapter
            })
        }
    }
}
