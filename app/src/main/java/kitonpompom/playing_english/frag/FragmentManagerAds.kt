package kitonpompom.playing_english.frag

import androidx.appcompat.app.AppCompatActivity
import kitonpompom.playing_english.R

object FragmentManagerAds {
    var curentFrag: BaseFragmentAds? = null

    fun setFragment(newFrag: BaseFragmentAds, idFrag: String, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frl_zad, newFrag)
        transaction.addToBackStack(idFrag)
        transaction.commit()
        curentFrag = newFrag
    }
}