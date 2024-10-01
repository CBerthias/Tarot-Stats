package com.berthias.tarotstats.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berthias.tarotstats.data.dao.JoueurDao
import com.berthias.tarotstats.model.Joueur

@Database(entities = [Joueur::class], version = 1, exportSchema = false)
abstract class TarotDatabase : RoomDatabase() {

    abstract fun joueurDao(): JoueurDao

    companion object {

        @Volatile
        private var instance: TarotDatabase? = null

        fun getDatabase(context: Context): TarotDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, TarotDatabase::class.java, "tarot_databse")
                    .build()
                    .also { instance = it }
            }
        }
    }
}