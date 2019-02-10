package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.OnItemClickListener
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.CharacterActivity
import lycaenion.org.marvelapp.models.SearchResultCharacter

class CharactersViewAdapter(var context : Context, var searchResultCharacters : List<SearchResultCharacter> ) : RecyclerView.Adapter<CharactersViewAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResultCharacters.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(searchResultCharacters[position].thumbnail.path+ "." +searchResultCharacters[position].thumbnail.extension)
            .into(viewHolder.image)

        viewHolder.imageName.text = searchResultCharacters[position].name

        viewHolder.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("id", searchResultCharacters[position].id)
                context.startActivity(intent)
                println("I am here")
            }
        } )

    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{
       val imageName : TextView = view.search_character_name
       val image : CircleImageView = view.search_thumbnail
       val parentLayout : RelativeLayout = view.parent_layout

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