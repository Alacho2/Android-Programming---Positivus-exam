package no.alacho.something

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.alacho.something.room.Post

class PostAdapter(context: Context?) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var posts = emptyList<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
      val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
      return PostViewHolder(itemView)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
      val current = posts[position]
      val bitmap = BitmapFactory.decodeByteArray(current.image, 0, current.image!!.size)
      holder.postTitle.text = current.name
      holder.postImage.setImageBitmap(bitmap)
    }

  internal fun setPosts(posts: List<Post>) {
    this.posts = posts
    notifyDataSetChanged()
  }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      val postTitle: TextView = itemView.findViewById(R.id.descriptionTitle)
      val postImage: ImageView = itemView.findViewById(R.id.postImage)
    }

}