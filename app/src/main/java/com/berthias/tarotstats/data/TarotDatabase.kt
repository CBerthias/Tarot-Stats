package com.berthias.tarotstats.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berthias.tarotstats.data.dao.JoueurDao
import com.berthias.tarotstats.data.dao.PartieDao
import com.berthias.tarotstats.model.Joueur
import com.berthias.tarotstats.model.Partie

@Database(
    entities = [Joueur::class, Partie::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 2, to = 3)]
)
abstract class TarotDatabase : RoomDatabase() {

    abstract fun joueurDao(): JoueurDao
    abstract fun partieDao(): PartieDao

    companion object {

        @Volatile
        private var instance: TarotDatabase? = null

        fun getDatabase(context: Context): TarotDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, TarotDatabase::class.java, "tarot_databse").build()
                    .also { instance = it }
            }
        }
    }
}