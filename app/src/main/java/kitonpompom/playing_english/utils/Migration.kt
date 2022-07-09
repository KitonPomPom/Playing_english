package kitonpompom.playing_english.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {
    internal val MIGRATION_1_2 =
        object : Migration(1, 2) { // Объект для миграции БД на новую версию
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tasks ADD COLUMN deadline LONG")
            }
        }
}