package kitonpompom.playing_english.activities

import android.app.Application
import kitonpompom.playing_english.db.MainDataBase

class MainApp: Application() {

    val database by lazy { MainDataBase.getDataBase(this) }
}