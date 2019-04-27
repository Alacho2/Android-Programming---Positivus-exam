package no.alacho.something.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {
    @Query("SELECT * FROM post_table")
    fun getAllPosts(): LiveData<List<Post>>

    @Insert
    fun insertPost(post: Post)

    @Query("DELETE FROM post_table")
    fun deleteAllPosts()
}