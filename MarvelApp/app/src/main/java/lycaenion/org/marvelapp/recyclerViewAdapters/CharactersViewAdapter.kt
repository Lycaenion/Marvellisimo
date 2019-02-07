package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.models.SearchResultCharacter

class CharactersViewAdapter(var context : Context, var searchResultCharacters : Array<SearchResultCharacter> ) : RecyclerView.Adapter<CharactersViewAdapter.ViewHolder>(){

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


    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
       val imageName : TextView = view.search_character_name
        val image : CircleImageView = view.search_thumbnail
    }



}