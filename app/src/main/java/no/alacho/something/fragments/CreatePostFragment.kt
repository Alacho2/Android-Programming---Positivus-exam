package no.alacho.something.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.create_post_layout.*
import no.alacho.something.R
import android.widget.FrameLayout



class CreatePostFragment : Fragment(), View.OnClickListener {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.create_post_layout, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    addPostBtn.setOnClickListener(this)
    cameraButton.setOnClickListener(this)
    mapButton.setOnClickListener(this)
  }

  override fun onClick(v: View) {
    when(v.id) {
      R.id.addPostBtn -> {
        findNavController(this).popBackStack()
      }
      R.id.cameraButton -> {
        val fragmentController = view?.findViewById<View>(R.id.fragment)
        Navigation.findNavController(fragmentController!!).navigate(R.id.cameraFragment)
        cameraButton.let {
          it.setColorFilter(Color.BLACK)
        }
        mapButton.colorFilter = null
      }
      R.id.mapButton -> {
        val fragmentController = view?.findViewById<View>(R.id.fragment)
        Navigation.findNavController(fragmentController!!).navigate(R.id.mapFragment)
        mapButton.let {
          //it.isEnabled = false
          it.setColorFilter(Color.BLACK)
        }
        cameraButton.colorFilter = null
      }
    }
  }
}