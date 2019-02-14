package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.SeriesActivity
import lycaenion.org.marvelapp.models.databaseModels.FavoriteSeries
import lycaenion.org.marvelapp.models.seriesModels.Series

class SearchSeriesViewAdapter (var context : Context, var searchResultSeries : List<Series>) : RecyclerView.Adapter<SearchSeriesViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSeriesViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResultSeries.size
    }

    fun addSeries(seriesList : List<Series>){
        searchResultSeries = seriesList
        notifyDataSetChanged()
    }

    fun emptyList() {
        searchResultSeries = kotlin.collections.emptyList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder : ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(searchResultSeries[position].thumbnail.path + "." + searchResultSeries[position].thumbnail.extension)
            .into(viewHolder.img)

        viewHolder.title.text = searchResultSeries[position].title

        if(checkIfFavorite(searchResultSeries[position].id)){
            Glide.with(context)
                .load(R.drawable.ic_favorite_heart)
                .into(viewHolder.favoriteIcon)
        }else{
            viewHolder.favoriteIcon.visibility = View.INVISIBLE
        }

        viewHolder.setOnItemClickListener(object: OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, SeriesActivity::class.java)
                intent.putExtra("id", searchResultSeries[position].id)
                context.startActivity(intent)
            }
        })
    }

    private fun checkIfFavorite(id : Int): Boolean{

        var realm : Realm
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        realm = Realm.getInstance(config)

        var favoriteSeries : FavoriteSeries? = realm.where(FavoriteSeries::class.java)
            .equalTo("id", id)
            .findFirst()

        realm.close()

        return favoriteSeries != null

    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        val title : TextView = view.search_name
        val img : CircleImageView = view.search_thumbnail
        val favoriteIcon : ImageView = view.favorite_indicator

        private var itemClickListener : OnItemClickListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun setOnItemClickListener(itemClickListener: OnItemClickListener){
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            this.itemClickListener!!.onItemClick(adapterPosition, v!!)
        }
    }
}