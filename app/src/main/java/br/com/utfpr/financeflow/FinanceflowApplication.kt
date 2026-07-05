package br.com.utfpr.financeflow

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import br.com.utfpr.financeflow.data.local.datastore.DataStoreManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FinanceflowApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        val dataStoreManager = DataStoreManager(this)
        
        // Aplicar o modo escuro salvo ao iniciar o app
        MainScope().launch {
            val isDarkMode = dataStoreManager.modoEscuro.first()
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}