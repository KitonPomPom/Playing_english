package kitonpompom.playing_english.dialogs

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kitonpompom.playing_english.R

class DialogCollectGame(private val interfaceDialogCollectGame: InterfaceDialogCollectGame) {
    fun showSelectedGame(context: Context, numTask:Int, id:Int, nameLevel:String, lockCollect: Int, lockSnake: Int){

        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.game_selection_dialog, null)
        val btStartQuiz = rootView.findViewById<Button>(R.id.btStartQuizGame)
        val btStartCollect = rootView.findViewById<Button>(R.id.btStartCollectGame)
        val btStartSnake = rootView.findViewById<Button>(R.id.btStartSnakeGame)
        val tvNameLevel = rootView.findViewById<TextView>(R.id.tvNumberLvl)
        val linlayCollect = rootView.findViewById<LinearLayout>(R.id.id_linLayStopCollect)
        val linlaySnake = rootView.findViewById<LinearLayout>(R.id.id_linLayStopSnake)

        builder.setView(rootView)
        builder.setCancelable(true)
        val dialog = builder.create()

        tvNameLevel.text = nameLevel

        if(lockCollect == 1 || lockCollect == 2){
            linlayCollect.visibility = View.GONE
        }else{
            linlayCollect.visibility = View.VISIBLE
        }
        if(lockSnake == 1 || lockSnake == 2){
            linlaySnake.visibility = View.GONE
        }else{
            linlaySnake.visibility = View.VISIBLE
        }

        linlayCollect.setOnClickListener(){
            Toast.makeText(context, "Пройдите викторину", Toast.LENGTH_LONG).show()
        }

        linlaySnake.setOnClickListener(){
            Toast.makeText(context, "Пройдите собирайку", Toast.LENGTH_LONG).show()
        }

        btStartQuiz.setOnClickListener(){
            interfaceDialogCollectGame.dialogStartQuizGame(numTask,id, lockCollect, lockSnake)
            dialog.dismiss()
        }
        btStartCollect.setOnClickListener(){
            interfaceDialogCollectGame.dialogStartCollectGame(numTask,id, lockCollect, lockSnake)
            dialog.dismiss()
        }
        btStartSnake.setOnClickListener(){
            interfaceDialogCollectGame.dialogStartSnakeGame(numTask,id, lockCollect, lockSnake)
            dialog.dismiss()
        }

        dialog.show()
    }
}