package kitonpompom.playing_english.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kitonpompom.playing_english.R
import kitonpompom.playing_english.entities.TaskListItem
import kitonpompom.playing_english.frag.FragmentCloseInterface
import kitonpompom.playing_english.frag.FragmentReturnInterface
import kitonpompom.playing_english.frag.StartGameEndDialog

class DialogFragGameEndPositive(private val fragCloseInterface: FragmentCloseInterface, private val fragReturnInterface: FragmentReturnInterface, private val startGameEndDialog: StartGameEndDialog) {



    fun showEndPositiveDialog(context: Context, itList: List<TaskListItem>, newList: ArrayList<String>, resultOptions:Int, resultInput:Int, lockCollect:Int, lockSnake:Int){

        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.end_positive_dialog, null)
        val btEnd = rootView.findViewById<Button>(R.id.btEnd)
        val btReturn = rootView.findViewById<Button>(R.id.btReturn)
        val tvResultNap = rootView.findViewById<TextView>(R.id.tvResultNap)
        val tvResultUgad = rootView.findViewById<TextView>(R.id.tvResultUgad)
        val tvCongrGame = rootView.findViewById<TextView>(R.id.tvCongrGame)
        val linlaybtStartCollectGame = rootView.findViewById<LinearLayout>(R.id.linLayBtStartCollectGame)
        val linlaybtStartSnakeGame = rootView.findViewById<LinearLayout>(R.id.linLayBtStartSnakeGame)
        val btStartSnake = rootView.findViewById<Button>(R.id.btStartSnakeGamePosDialog)
        val btStartCollect = rootView.findViewById<Button>(R.id.btStartCollectGameEndVict)

        val option = resultOptions.toString()
        val input = resultInput.toString()
        if(lockCollect == 0 && lockSnake == 0){
            tvCongrGame.visibility = View.VISIBLE
            linlaybtStartCollectGame.visibility = View.VISIBLE
            fragCloseInterface.updateLockGame()
        }else{
            tvCongrGame.visibility = View.GONE
            linlaybtStartCollectGame.visibility = View.GONE
        }
        if(lockCollect == 1 && lockSnake == 1){
            tvCongrGame.text = context.getText(R.string.consolidate)
            tvCongrGame.visibility = View.VISIBLE
            linlaybtStartCollectGame.visibility = View.VISIBLE
            linlaybtStartSnakeGame.visibility = View.VISIBLE
        }
        if(lockCollect == 1 && lockSnake == 0){
            tvCongrGame.text = context.getText(R.string.consolidate)
            tvCongrGame.visibility = View.VISIBLE
            linlaybtStartCollectGame.visibility = View.VISIBLE
            linlaybtStartSnakeGame.visibility = View.GONE
        }
        if(lockCollect == 2 && lockSnake == 2){
            linlaybtStartCollectGame.visibility = View.GONE
            linlaybtStartSnakeGame.visibility = View.GONE
            tvCongrGame.visibility = View.GONE
        }



        tvResultNap.text = "Угадано: $input "
        tvResultUgad.text = "Введено: $option"


        builder.setView(rootView)
        builder.setCancelable(false)
        val dialog = builder.create()

        btStartCollect.setOnClickListener(){
            startGameEndDialog.startGameCollectEndDialog()
            dialog.dismiss()
        }

        btStartSnake.setOnClickListener(){
            startGameEndDialog.startGameSnakeEndDialog()
            dialog.dismiss()
        }

        btEnd.setOnClickListener{
            //Toast.makeText(context, "End", Toast.LENGTH_LONG).show()
            fragCloseInterface.onFragClose()
            dialog.dismiss()
        }

        btReturn.setOnClickListener{
            //Toast.makeText(context, "Return", Toast.LENGTH_LONG).show()
            fragReturnInterface.returnGame(newList, itList)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showExitQuestion(context: Context){

        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.exit_question_dialog, null)
        val btNo = rootView.findViewById<Button>(R.id.btNo)
        val btYes = rootView.findViewById<Button>(R.id.btYes)

        builder.setView(rootView)
        builder.setCancelable(true)
        val dialog = builder.create()

        btYes.setOnClickListener(){
            fragCloseInterface.onFragClose()
            dialog.dismiss()
        }
        btNo.setOnClickListener(){
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showEndCollectGamePositiveDialog(context: Context, itList: List<TaskListItem>, newList: ArrayList<String>, lockSnake: Int){

        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.end_positive_dialog_collect_game, null)
        val btEnd = rootView.findViewById<Button>(R.id.btEnd)
        val btReturn = rootView.findViewById<Button>(R.id.btReturn)
        val btStartSnake = rootView.findViewById<Button>(R.id.btStartSnakeGamePosDialog)
        val linlaybtStartSnake = rootView.findViewById<LinearLayout>(R.id.linLayBtStartSelectionDialog)
        val tvCongrEndCollectGame = rootView.findViewById<TextView>(R.id.tvCongrDialogEndCollect)



        builder.setView(rootView)
        builder.setCancelable(false)
        val dialog = builder.create()

        if(lockSnake == 0){
            fragCloseInterface.updateLockGame()
        }else{
            tvCongrEndCollectGame.text = context.getText(R.string.consolidate_end_collect)
        }
        if (lockSnake == 2){
            tvCongrEndCollectGame.text = context.getText(R.string.congr_end_all_word_end_dialog_collect)
            linlaybtStartSnake.visibility = View.GONE
        }




        btEnd.setOnClickListener{
            //Toast.makeText(context, "End", Toast.LENGTH_LONG).show()
            fragCloseInterface.onFragClose()
            dialog.dismiss()
        }

        btStartSnake.setOnClickListener{
            startGameEndDialog.startGameSnakeEndDialog()
            dialog.dismiss()
        }

        btReturn.setOnClickListener{
            //Toast.makeText(context, "Return", Toast.LENGTH_LONG).show()
            fragReturnInterface.returnGame(newList, itList)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showEndSnakeGamePositiveDialog(context: Context, itList: List<TaskListItem>, newList: ArrayList<String>, lockSnake: Int){

        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.end_positive_dialog_snake_game, null)
        val btEnd = rootView.findViewById<Button>(R.id.btEnd)
        val btReturn = rootView.findViewById<Button>(R.id.btReturn)
        val tvCongr = rootView.findViewById<TextView>(R.id.tvCongrSnakeEndDialog)



        builder.setView(rootView)
        builder.setCancelable(false)
        val dialog = builder.create()

        if (lockSnake == 2){
            tvCongr.text = context.getText(R.string.congr_end_all_word_end_dialog_collect)
        }


        btEnd.setOnClickListener{
            //Toast.makeText(context, "End", Toast.LENGTH_LONG).show()
            fragCloseInterface.onFragClose()
            dialog.dismiss()
        }

        btReturn.setOnClickListener{
            //Toast.makeText(context, "Return", Toast.LENGTH_LONG).show()
            fragReturnInterface.returnGame(newList, itList)
            dialog.dismiss()
        }
        dialog.show()
    }

}