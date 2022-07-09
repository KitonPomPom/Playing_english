package kitonpompom.playing_english.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kitonpompom.playing_english.entities.LevelListItem
import kitonpompom.playing_english.entities.TaskListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query ("SELECT * FROM level_list") // запрос на всю базу уровней
    fun getAllLevelList(): Flow<List<LevelListItem>>

    @Insert
    suspend fun insertLevelList(note:LevelListItem)

    @Query ("SELECT * FROM task_list") //запрос на всю базу заданий
    fun getAllTaskList(): Flow<List<TaskListItem>>

    @Insert
    suspend fun insertTaskList(note:TaskListItem)

    @Query ("SELECT * FROM task_list WHERE numberTask LIKE :numTask") //запрос по номеру задания
    fun getTaskListByNumberTask(numTask: Int): Flow<List<TaskListItem>>

    @Query ("SELECT * FROM task_list WHERE lockQuestion LIKE :lockRequest") //запрос открытых заданий
    fun getTaskListByLockRequest(lockRequest: Int): Flow<List<TaskListItem>>

    @Query ("UPDATE level_list SET typedSuccess = :typSuc WHERE id = (:id) ")
    fun updateTypedSuccess(typSuc:Int, id:Int)

    @Query ("UPDATE level_list SET selectedSuccess = :selSuc WHERE id = (:id) ")
    fun updateSelectedSuccess(selSuc:Int, id:Int)

    @Query ("UPDATE level_list SET lock = :lockResult WHERE id = (:id) ")
    fun updateLock(lockResult:Int, id:Int)

    @Query ("UPDATE level_list SET lockCollect = :lockCollectResult WHERE id = (:id) ")
    fun updateLockCollect(lockCollectResult:Int, id:Int)

    @Query ("UPDATE level_list SET lockSnake = :lockSnakeResult WHERE id = (:id) ")
    fun updateLockSnake(lockSnakeResult:Int, id:Int)

    @Query ("UPDATE task_list SET lockQuestion = :lockResult WHERE numberTask = (:numTask) ")
    fun updateLockQuestion(lockResult:Int, numTask:Int)

}