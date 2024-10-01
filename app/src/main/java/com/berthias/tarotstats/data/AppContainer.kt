package com.berthias.tarotstats.data

import android.content.Context
import com.berthias.tarotstats.data.repository.JoueurRepository

class AppContainer(context: Context) {

    val joueurRepository: JoueurRepository by lazy {
        JoueurRepository(
            TarotDatabase.getDatabase(
                context
            ).joueurDao()
        )
    }

}