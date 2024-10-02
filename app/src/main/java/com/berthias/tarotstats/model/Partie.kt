package com.berthias.tarotstats.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partie")
data class Partie(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nomJoueur: String,
    val couleur: CouleurEnum,
    val gagne: Boolean
)
