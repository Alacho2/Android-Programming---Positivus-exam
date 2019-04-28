package no.alacho.positivity.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class PostRepository(private val postDao: PostDao){
    val allPosts: LiveData<List<Post>> = postDao.getAllPosts()

    @WorkerThread
    suspend fun insert(post: Post){
        postDao.insertPost(post)
    }
}