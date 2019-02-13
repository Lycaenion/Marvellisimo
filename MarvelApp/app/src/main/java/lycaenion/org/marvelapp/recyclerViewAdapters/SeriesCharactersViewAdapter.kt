package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_mini_listitem.view.*
import lycaenion.org.marvelapp.OnItemClickListener
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.CharacterActivity
import lycaenion.org.marvelapp.models.characterModels.SearchResultCharacter
import java.text.FieldPosition

class SeriesCharactersViewAdapter(var context : Context, var seriesCharacterList : List<SearchResultCharacter>) : RecyclerView.Adapter<SeriesCharactersViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesCharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_mini_listitem, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seriesCharacterList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = seriesCharacterList[position].name

        viewHolder.setOnClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("id", seriesCharacterList[position].id)
                context.startActivity(intent)
            }

        })
    }

    fun addCharacters(characterList : List<SearchResultCharacter>){
        seriesCharacterList = characterList

        notifyDataSetChanged()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{
        val name = view.character_series_title

        private var itemClickListener: OnItemClickListener? = null
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