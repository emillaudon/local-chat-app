package com.example.myapplication


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


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

        init {
            textViewHeader = itemView.findViewById(R.id.textone)
            textViewText = itemView.findViewById(R.id.texttwo)
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
            holder.textViewHeader.text = post.getUserName() + " " + post.getTemperature() + "°C"
        }
    }
    override fun getItemCount(): Int {
        var count = listItems.size
        println(count)
        return count
    }


}