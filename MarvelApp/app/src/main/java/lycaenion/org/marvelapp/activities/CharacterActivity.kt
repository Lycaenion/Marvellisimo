package lycaenion.org.marvelapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.Character
import lycaenion.org.marvelapp.models.Series
import lycaenion.org.marvelapp.recyclerViewAdapters.CharacterSeriesViewAdapter
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener

class CharacterActivity : AppCompatActivity() {

    private var characterSeriesList : List<Series> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: CharacterSeriesViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        var character : Character

        var id : Int

        id = intent.extras.getInt("id")

        MarvelCharacterHandler.getCharacter(id).observeOn(AndroidSchedulers.mainThread()).subscribe(){
                data -> character = data.data.results[0]
            setCharacterView(character)
        }

        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.character_series)
        recyclerView.layoutManager = linearLayoutManager
        initAdapter(id)
        initScrollListener(linearLayoutManager, id)





    }

    fun setCharacterView(character : Character){

        val imgUrl = character.thumbnail.path + "."+ character.thumbnail.extension

        character_name.text = character.name
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(character_thumbnail)
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
