package no.alacho.positivity.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.create_post_layout.*
import no.alacho.positivity.R
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import no.alacho.positivity.PostAdapter
import no.alacho.positivity.room.Post
import java.io.ByteArrayOutputStream

class CreatePostFragment : Fragment(), View.OnClickListener {

  private lateinit var postViewModel: PostViewModel
  private lateinit var adapter: PostAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.create_post_layout, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    addPostBtn.setOnClickListener(this)
    cameraButton.setOnClickListener(this)
    mapButton.setOnClickListener(this)
    postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    adapter = PostAdapter(context)
  }

  override fun onClick(v: View) {
    when(v.id) {
      R.id.addPostBtn -> {
        if(title.text.isNotEmpty()){
          val baos = ByteArrayOutputStream()
          if(takenImage.visibility == View.VISIBLE){
            val bitmap = (takenImage.drawable as BitmapDrawable).bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
          }
          postViewModel.insert(Post(0, title.text.toString(), baos.toByteArray()))
          findNavController(this).popBackStack()
        } else {
          Snackbar
            .make(activity!!.findViewById(R.id.constraintView), "You need a title", Snackbar.LENGTH_SHORT)
            .show()
        }
      }
      R.id.cameraButton -> {
        val fragmentController = view?.findViewById<View>(R.id.fragment)
        Navigation.findNavController(fragmentController!!).navigate(R.id.cameraFragment)
        cameraButton.setColorFilter(Color.BLACK)
        mapButton.colorFilter = null
      }
      R.id.mapButton -> {
        val fragmentController = view?.findViewById<View>(R.id.fragment)
        Navigation.findNavController(fragmentController!!).navigate(R.id.mapFragment)
        mapButton.setColorFilter(Color.BLACK)
        cameraButton.colorFilter = null
      }
    }
  }
}