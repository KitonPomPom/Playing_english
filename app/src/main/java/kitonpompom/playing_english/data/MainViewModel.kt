package kitonpompom.playing_english.data

import androidx.lifecycle.*
import kitonpompom.playing_english.db.MainDataBase
import kitonpompom.playing_english.entities.LevelListItem
import kitonpompom.playing_english.entities.TaskListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDataBase) : ViewModel() {

    private val dao = database.getDao()
    val allLevelList: LiveData<List<LevelListItem>> = dao.getAllLevelList().asLiveData()
    fun insertLevelList(levelList: LevelListItem) = viewModelScope.launch {
        dao.insertLevelList(levelList)
    }

    val allTaskListItem: LiveData<List<TaskListItem>> = dao.getAllTaskList().asLiveData()
    fun insertTaskList(taskList: TaskListItem) = viewModelScope.launch {
            dao.insertTaskList(taskList)
    }

    fun getTaskListByNumberTask(numTask: Int): LiveData<List<TaskListItem>> { //функция взятия из БД по номеру задания
        val taskListItemByNumberTask: LiveData<List<TaskListItem>> =
            dao.getTaskListByNumberTask(numTask).asLiveData()
        return taskListItemByNumberTask
    }

    fun getTaskListByLockRequest(lockRequest: Int): LiveData<List<TaskListItem>> { //функция взятия из БД по значения ключа
        val taskListItemByLockRequest: LiveData<List<TaskListItem>> =
            dao.getTaskListByLockRequest(lockRequest).asLiveData()
        return taskListItemByLockRequest
    }

    fun updateTypedSuccessById(id:Int, typSuc: Int) = viewModelScope.launch {
        this.async(Dispatchers.IO) {
            dao.updateTypedSuccess(typSuc, id)
        }.await()
    }

    fun updateSelectdedSuccessById(id:Int, selSuc: Int) = viewModelScope.launch {
        this.async(Dispatchers.IO) {
            dao.updateSelectedSuccess(selSuc, id)
        }.await()
    }

    fun updateLockById(id:Int, lockResult: Int) = viewModelScope.launch {
        this.async(Dispatchers.IO) {
            dao.updateLock(lockResult, id)
        }.await()
    }

    fun updateLockCollectById(id:Int, lockCollectResult: Int) = viewModelScope.launch {
        this.async(Dispatchers.IO) {
            dao.updateLockCollect(lockCollectResult, id)
        }.await()
    }

    fun updateLockSnakeById(id:Int, lockSnakeResult: Int) = viewModelScope.launch {
        this.async(Dispatchers.IO) {
            dao.updateLockSnake(lockSnakeResult, id)
        }.await()
    }

    fun updateLockQuestionByNumTask(numTask: Int, lockResult: Int) = viewModelScope.launch {
        this.async(Dispatchers.IO) {
            dao.updateLockQuestion(lockResult, numTask)
        }.await()
    }

    class MainViewModelFactory(val database: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}