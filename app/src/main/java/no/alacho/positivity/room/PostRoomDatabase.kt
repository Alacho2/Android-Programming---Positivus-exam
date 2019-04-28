package no.alacho.positivity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Post::class], version = 1)
abstract class PostRoomDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: PostRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PostRoomDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostRoomDatabase::class.java,
                    "word_database"
                )//.addCallback(WordDatabaseCallback(scope))  Til debugging
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

  private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
      super.onOpen(db)
      INSTANCE?.let { database ->
        scope.launch(Dispatchers.IO) {
          deleteMyDatabase(database.postDao())
        }
      }
    }

    private fun deleteMyDatabase(wordDao: PostDao) {
      wordDao.deleteAllPosts()
    }
  }
}