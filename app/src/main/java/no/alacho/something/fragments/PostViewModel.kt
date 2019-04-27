package no.alacho.something.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import no.alacho.something.room.Post
import no.alacho.something.room.PostRepository
import no.alacho.something.room.PostRoomDatabase
import kotlin.coroutines.CoroutineContext

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository
    val allPosts: LiveData<List<Post>>

    private var parentJob = Job()
    private val coRoutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coRoutineContext)

    init {
        val wordsDao = PostRoomDatabase.getDatabase(application, scope).postDao()
        repository = PostRepository(wordsDao)
        allPosts = repository.allPosts
    }

    fun insert(post: Post) = scope.launch(Dispatchers.IO) {
        repository.insert(post)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}
