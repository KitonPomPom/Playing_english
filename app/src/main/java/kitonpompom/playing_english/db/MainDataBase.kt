package kitonpompom.playing_english.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kitonpompom.playing_english.entities.LevelListItem
import kitonpompom.playing_english.entities.TaskListItem
import kitonpompom.playing_english.utils.Migration

@Database (entities = [LevelListItem::class, TaskListItem::class],version = 2)
abstract class MainDataBase: RoomDatabase() {
    abstract fun getDao():Dao



    companion object{
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context:Context): MainDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "dat_lis_game.db"
                )
                    .addMigrations(Migration.MIGRATION_1_2)
                    .build()
                instance
            }
        }
    }
}