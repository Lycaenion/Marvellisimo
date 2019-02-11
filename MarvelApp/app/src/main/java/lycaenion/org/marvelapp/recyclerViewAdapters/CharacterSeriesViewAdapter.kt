package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_mini_listitem.view.*
import lycaenion.org.marvelapp.OnItemClickListener
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.SeriesActivity
import lycaenion.org.marvelapp.models.Series

class CharacterSeriesViewAdapter(var context : Context, var characterSeriesList : List<Series>) : RecyclerView.Adapter<CharacterSeriesViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterSeriesViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_mini_listitem, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return characterSeriesList.size
    }

    override fun onBindViewHolder(viewHolder : ViewHolder, position: Int) {
        viewHolder.title.text = characterSeriesList[position].title

        viewHolder.setOnItemClickListner(object : OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {

                println(characterSeriesList[position].title)
                /*val intent = Intent(context, SeriesActivity::class.java)
                intent.putExtra("id", characterSeriesList[position].id)
                context.startActivity(intent)*/
            }
        })
    }

    fun addSeries(seriesList: List<Series>){
        characterSeriesList = seriesList

        notifyDataSetChanged()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{
        val title : TextView = view.character_series_title

        private var itemClickListener : OnItemClickListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun setOnItemClickListner(itemClickListener: OnItemClickListener){
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            this.itemClickListener!!.onItemClick(adapterPosition, v!!)
        }
    }
}