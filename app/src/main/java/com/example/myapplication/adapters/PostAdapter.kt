package com.example.myapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Post


class PostAdapter(
    posts: ArrayList<Post>,
    context: Context
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    // Context object used to inflate list_item layout
    private var listItems: List<Post> = posts
    private var context: Context? = context

    // Generated constructor from members
    fun PostAdapter(
        listItems: List<Post>,
        context: Context?
    ) {
        this.listItems = listItems
        this.context = context
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewHeader: TextView
        var textViewText: TextView
        var degreesViewText: TextView
        var relativeLayout: RelativeLayout

        init {
            textViewHeader = itemView.findViewById(R.id.textone)
            textViewText = itemView.findViewById(R.id.texttwo)
            degreesViewText = itemView.findViewById(R.id.degrees)
            relativeLayout = itemView.findViewById(R.id.itemRelativeLayout)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post : Post? = listItems?.get(position)
        if (post != null) {
            holder.textViewText.text = post.getText()
            holder.textViewHeader.text = post.getUserName()
            holder.degreesViewText.text = post.getTemperature().toString() + "°C"

            if (position % 2 == 0) {
                holder.relativeLayout.setBackgroundResource(R.color.colorAccent)
            } else if (position % 3 == 0) {
                holder.relativeLayout.setBackgroundResource(R.color.postColorTwo)
            } else {
                holder.relativeLayout.setBackgroundResource(R.color.postColorOne)
            }
        }
    }
    override fun getItemCount(): Int {
        var count = listItems.size
        println(count)
        return count
    }


}