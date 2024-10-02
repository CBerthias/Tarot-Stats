package com.berthias.tarotstats.data

import android.content.Context
import com.berthias.tarotstats.data.repository.JoueurRepository
import com.berthias.tarotstats.data.repository.PartieRepository

class AppContainer(context: Context) {

    val joueurRepository: JoueurRepository by lazy {
        JoueurRepository(
            TarotDatabase.getDatabase(
                context
            ).joueurDao()
        )
    }

    val partieRepository: PartieRepository by lazy {
        PartieRepository(
            TarotDatabase.getDatabase(
                context
            ).partieDao()
        )
    }

}