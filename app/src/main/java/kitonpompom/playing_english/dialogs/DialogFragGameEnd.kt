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

class DialogFragGameEnd (private val fragCloseInterface: FragmentCloseInterface, private val fragReturnInterface: FragmentReturnInterface, private val showAdsInterfase: OneShowAdsInterfase) {

    fun showEndDialog(context: Context, itList: List<TaskListItem>, newList: ArrayList<String>, tempADSbtVis:Int, rightAnswer: String){

        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.end_negativ_dialog, null)
        val btEnd = rootView.findViewById<Button>(R.id.btEnd)
        val btReturn = rootView.findViewById<Button>(R.id.btReturn)
        val btContinue = rootView.findViewById<Button>(R.id.btContinue)
        val tvRightAnswer = rootView.findViewById<TextView>(R.id.tvRightAnswer)
        val tvText = rootView.findViewById<TextView>(R.id.textView6)
        val tvSupportText = rootView.findViewById<TextView>(R.id.tvSupportText)
        val linLayBtContinue = rootView.findViewById<LinearLayout>(R.id.linLayBtContinueEndNegDialog)

        builder.setView(rootView)
        builder.setCancelable(false)
        val dialog = builder.create()

        if(tempADSbtVis == 1) {
            linLayBtContinue.visibility = View.GONE
            tvRightAnswer.visibility = View.VISIBLE
            tvText.visibility = View.VISIBLE
            tvSupportText.setText(R.string.cosolation_inscription_3)
            tvRightAnswer.text = rightAnswer


        }else {
            linLayBtContinue.visibility = View.VISIBLE
            tvSupportText.setText(R.string.cosolation_inscription_4)
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

        btContinue.setOnClickListener {
            showAdsInterfase.OneShowInterAds()
            dialog.dismiss()
        }
        dialog.show()
    }

}