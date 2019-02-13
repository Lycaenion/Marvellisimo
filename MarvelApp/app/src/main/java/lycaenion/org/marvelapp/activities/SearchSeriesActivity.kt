package lycaenion.org.marvelapp.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelSeriesHandler
import lycaenion.org.marvelapp.models.seriesModels.Series
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener
import lycaenion.org.marvelapp.recyclerViewAdapters.SeriesViewAdapter

class SearchSeriesActivity : AppCompatActivity() {
    private var seriesList : List<Series> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: SeriesViewAdapter
    private lateinit var recyclerView : RecyclerView
    private var searchString = ""
    private lateinit var searchView : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_series)

        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.series_recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        searchView = findViewById(R.id.search_series)
        initAdapter()
        initScrollListener(linearLayoutManager)
        setTextListener()

    }

    private fun setTextListener(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!!.isEmpty()){
                    searchString = ""
                }else{
                    searchString = query!!
                }

                if(searchString.equals("")){
                    resetAdapter()
                    addSeries(0,searchString)
                }else{
                    resetAdapter()
                    addSeries(0, searchString)
                    resetAdapter()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isEmpty()){
                    searchString = newText
                }else{
                    searchString = newText
                }

                if(searchString.equals("")){
                    resetAdapter()
                    addSeries(0, searchString)
                    resetAdapter()
                }else{
                    resetAdapter()
                    println("size: " + seriesList.size)
                    addSeries(0, searchString)
                    resetAdapter()
                }

                return false
            }

        })
    }

    private fun resetAdapter(){
        scrollListener.resetState()
        adapter.emptyList()
        seriesList = emptyList()
        adapter.addSeries(seriesList)
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(){
        MarvelSeriesHandler.getAllSeries(0).observeOn(AndroidSchedulers.mainThread()).subscribe({
            response -> seriesList = seriesList + response.data.results.asList()
            adapter = SeriesViewAdapter(this, seriesList)
            recyclerView.adapter = adapter
        })
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
    fun addSeries(offset : Int, search: String){
        if(search.equals("")){
            if(offset == 0){
                scrollListener.resetState()
                adapter.notifyDataSetChanged()
                MarvelSeriesHandler.getAllSeries(0).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    response -> seriesList = seriesList + response.data.results.asList()
                    adapter.addSeries(seriesList)
                    adapter.notifyDataSetChanged()
                })
            }else{
                MarvelSeriesHandler.getAllSeries(offset).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    response -> seriesList = seriesList + response.data.results.asList()
                    adapter.addSeries(seriesList)
                    adapter.notifyDataSetChanged()
                })
            }
        }else{
            MarvelSeriesHandler.searchSeries(searchString, offset).observeOn(AndroidSchedulers.mainThread()).subscribe({
                response -> seriesList = seriesList + response.data.results.asList()
                adapter.addSeries(seriesList)
                adapter.notifyDataSetChanged()
            })
        }
    }
}
