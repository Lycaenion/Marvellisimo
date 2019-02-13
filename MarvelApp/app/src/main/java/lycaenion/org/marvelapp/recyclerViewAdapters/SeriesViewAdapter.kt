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
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.SeriesActivity
import lycaenion.org.marvelapp.models.seriesModels.Series

class SeriesViewAdapter (var context : Context, var searchResultSeries : List<Series>) : RecyclerView.Adapter<SeriesViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewAdapter.ViewHolder {
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

    override fun onBindViewHolder(viewHolder : ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(searchResultSeries[position].thumbnail.path + "." + searchResultSeries[position].thumbnail.extension)
            .into(viewHolder.img)

        viewHolder.title.text = searchResultSeries[position].title

        viewHolder.setOnItemClickListener(object: OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, SeriesActivity::class.java)
                intent.putExtra("id", searchResultSeries[position].id)
                context.startActivity(intent)
            }
        })
    }

    fun emptyList() {
        searchResultSeries = kotlin.collections.emptyList()
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