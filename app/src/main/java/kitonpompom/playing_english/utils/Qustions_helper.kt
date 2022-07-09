package kitonpompom.playing_english.utils

import android.content.Context
import android.util.Log
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.entities.LevelListItem
import kitonpompom.playing_english.entities.TaskListItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object Qustions_helper {

    fun getAllInfLev(context: Context, mainViewModel: MainViewModel): Int{ // функция превращающая файл джейсон в массив с странами

        var tempStart: Int = 0
        try {
            val inputStream: InputStream = context.assets.open("informationLevels.json") // создаём инпутстрим и указываем из какого файла (поток из байтов)
            val size: Int = inputStream.available() //узнаем размер данного стрима "informationLevels.json"
            val bytesArray = ByteArray(size) //создаем массив куда и помещаем считанные байты
            inputStream.read(bytesArray) //считываем и записываем в массив
            val jsonFiles = String(bytesArray) // массив превращаем в объект Стринг
            val jsonArray = JSONArray(jsonFiles) // Превращаем в файл Джейсон обЪект
            if(jsonArray != null) {

                for (n in 0 until jsonArray.length()){
                    val jsonObject: JSONObject = jsonArray.getJSONObject(n)
                    val numTask = jsonObject.getInt("num task")
                    val title = jsonObject.getString("title")
                    val lock = jsonObject.getInt("lock")
                    val tasks = jsonObject.getJSONArray("tasks")
                    mainViewModel.insertLevelList(LevelListItem(null,title, numTask,0,0,lock,0,0))
                    for (i in 0 until tasks.length()){
                        val tasksObject = tasks.getJSONObject(i)
                        val numberQuestion = tasksObject.getInt("number question")
                        val lockQuestion = tasksObject.getInt("lock question")
                        val numberTask = tasksObject.getInt("number task")
                        val word = tasksObject.getString("word")
                        val rightAnswer = tasksObject.getString("right answer")
                        val answerOptions1 = tasksObject.getString("answer options1")
                        val answerOptions2 = tasksObject.getString("answer options2")
                        val answerOptions3 = tasksObject.getString("answer options3")
                        val optionInTheTextRus = tasksObject.getString("option in the text rus")
                        val optionInTheTextEng = tasksObject.getString("option in the text eng")
                        mainViewModel.insertTaskList(TaskListItem(null, numberTask, lockQuestion ,numberQuestion, word,rightAnswer,answerOptions1,answerOptions2,answerOptions3, optionInTheTextRus, optionInTheTextEng))
                        tempStart = 1
                    }
                }
            }
        }catch (e:IOException){
            Log.d("MyLog","Exception $e")
        }
        return tempStart
    }
}
