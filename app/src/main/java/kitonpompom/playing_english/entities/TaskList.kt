package kitonpompom.playing_english.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list")
data class TaskListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "numberTask")
    val numberTask:Int,

    @ColumnInfo(name = "lockQuestion")
    val lockQuestion:Int,

    @ColumnInfo(name = "numberQuestion")
    val numberQuestion:Int,

    @ColumnInfo(name = "word")
    val word:String,

    @ColumnInfo(name = "rightAnswer")
    val rightAnswer:String,

    @ColumnInfo(name = "suggestedOption1")
    val suggestedOption1:String,

    @ColumnInfo(name = "suggestedOption2")
    val suggestedOption2:String,

    @ColumnInfo(name = "suggestedOption3")
    val suggestedOption3:String,

    @ColumnInfo(name = "OptionInTheTextRus")
    val optionInTheTextRus:String,

    @ColumnInfo(name = "OptionInTheTextEng")
    val optionInTheTextEng:String,

)
