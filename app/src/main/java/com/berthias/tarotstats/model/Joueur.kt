package com.berthias.tarotstats.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Joueur")
data class Joueur(
    @PrimaryKey(autoGenerate = false)
    val nom: String
)
