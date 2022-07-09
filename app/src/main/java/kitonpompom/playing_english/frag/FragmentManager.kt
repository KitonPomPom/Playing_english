package kitonpompom.playing_english.frag

import androidx.appcompat.app.AppCompatActivity
import kitonpompom.playing_english.R

object FragmentManager {
    var curentFrag: BaseFragment? = null

    fun setFragment(newFrag: BaseFragment,idFragSave: String, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frl_zad, newFrag)
        transaction.addToBackStack(idFragSave)
        transaction.commit()
        curentFrag = newFrag
    }
}