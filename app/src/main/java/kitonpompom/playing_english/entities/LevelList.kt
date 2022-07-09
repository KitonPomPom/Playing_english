package kitonpompom.playing_english.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "level_list")
data class LevelListItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,

    @ColumnInfo(name = "nameLevel")
    val nameLevel: String,

    @ColumnInfo(name = "numTask")
    val numTask: Int,

    @ColumnInfo(name = "typedSuccess")
    val typedSuccess: Int,

    @ColumnInfo(name = "selectedSuccess")
    val selectedSuccess:Int,

    @ColumnInfo(name = "lock")
    val lock:Int,

    @ColumnInfo(name = "lockCollect")
    val lockCollect:Int,

    @ColumnInfo(name = "lockSnake")
    val lockSnake:Int

): Serializable