package no.alacho.positivity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,

                @ColumnInfo(name = "post_title")
                val name: String,

                @ColumnInfo(name = "post_content", typeAffinity = ColumnInfo.BLOB)
                val image: ByteArray? = null
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Post

    if (id != other.id) return false
    if (name != other.name) return false
    if (image != null) {
      if (other.image == null) return false
      if (!image.contentEquals(other.image)) return false
    } else if (other.image != null) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id
    result = 31 * result + name.hashCode()
    result = 31 * result + (image?.contentHashCode() ?: 0)
    return result
  }
}