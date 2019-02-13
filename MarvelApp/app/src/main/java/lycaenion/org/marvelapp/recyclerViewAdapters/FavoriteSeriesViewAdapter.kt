package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.SeriesActivity
import lycaenion.org.marvelapp.models.databaseModels.FavoriteSeries

class FavoriteSeriesViewAdapter(var favoritesList : List<FavoriteSeries>) : RecyclerView.Adapter<FavoriteSeriesViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteSeriesViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    override fun onBindViewHolder(viewHolder : ViewHolder, position : Int) {
        Glide.with(viewHolder.context)
            .asBitmap()
            .load(favoritesList[position].imgPath)
            .into(viewHolder.img)

        viewHolder.title.text = favoritesList[position].title

        viewHolder.setOnClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(viewHolder.context, SeriesActivity::class.java)
                intent.putExtra("id", favoritesList[position].id)
                viewHolder.context.startActivity(intent)
            }
        })
    }

    inner class ViewHolder(view : View) :RecyclerView.ViewHolder(view), View.OnClickListener{
        private var itemClickListener : OnItemClickListener? = null

        val title : TextView = view.search_name
        val img : CircleImageView = view.search_thumbnail
        val context = view.context!!


        init {
            itemView.setOnClickListener(this)
        }

        fun setOnClickListener(itemClickListener: OnItemClickListener){
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            this.itemClickListener!!.onItemClick(adapterPosition, v!!)
        }

    }

}