package no.alacho.positivity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import no.alacho.positivity.room.Post

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
      val image = current.image!!
      holder.postTitle.text = current.name
      if(image.isNotEmpty()) {
        val bitmap = BitmapFactory.decodeByteArray(current.image, 0, image.size)
        val layoutParams = holder.postImage.layoutParams
        layoutParams.width = 350
        layoutParams.height = 350
        holder.postImage.layoutParams = layoutParams
        holder.postImage.setImageBitmap(bitmap)
        holder.postImage.visibility = View.VISIBLE
      } else {
        holder.postImage.visibility = View.GONE
        holder.postTitle.textSize = 22f
      }
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