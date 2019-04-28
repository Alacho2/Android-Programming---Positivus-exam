package no.alacho.something.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import no.alacho.something.PostAdapter
import no.alacho.something.R
import java.util.*
import kotlin.concurrent.timerTask
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.recycler.*
import no.alacho.something.room.Post

class Posts : Fragment(), View.OnClickListener {
  private lateinit var layoutManager: StaggeredGridLayoutManager
  private lateinit var adapter: PostAdapter
  private lateinit var search: View
  private lateinit var postViewModel: PostViewModel
  private lateinit var postList: List<Post>



  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.recycler, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    fab.setOnClickListener(this)
    search = activity!!.findViewById<View>(R.id.app_bar_search)
    search.setOnClickListener(this)
    searchBar.visibility = View.GONE
    searchBar.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {}
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        if(s.isNotEmpty()){
          val filterList = postList.filter {
            it.name.contains(s)
          }
          adapter.setPosts(filterList)
        }

        if (s.isEmpty() && searchBar.visibility == View.VISIBLE) {
          Timer().schedule(timerTask {
            activity!!.runOnUiThread {
              searchBar.visibility = View.GONE
              search.visibility = View.VISIBLE
              fab.show()
              val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
              imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
              adapter.setPosts(postList)
            }
          }, 2000)
        }
      }
    })

    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    postRecycler.layoutManager = layoutManager
    adapter = PostAdapter(view?.context)
    postRecycler.adapter = adapter

    postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
    postViewModel.allPosts.observe(this, Observer { posts ->
      postList = posts
      adapter.setPosts(posts)
    })
  }

  override fun onClick(v: View) {
    when(v.id) {
      search.id -> {
        setUpSearchBar()
      }
      R.id.fab -> {
        findNavController(this).navigate(R.id.action_posts_to_createPostFragment)
      }
    }
  }

  private fun setUpSearchBar(){
    fab.hide()
    searchBar.visibility = View.VISIBLE
    searchBar.animate().alpha(1.0f)
    searchBar.requestFocus()
    search.visibility = View.GONE
    val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT)
  }

}