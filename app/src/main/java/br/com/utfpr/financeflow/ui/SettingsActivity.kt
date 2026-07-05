package br.com.utfpr.financeflow.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import br.com.utfpr.financeflow.R
import br.com.utfpr.financeflow.data.local.datastore.DataStoreManager
import br.com.utfpr.financeflow.databinding.ActivitySettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_settings)

        dataStoreManager = DataStoreManager(this)

        // Carregar preferências atuais
        lifecycleScope.launch {
            val meta = dataStoreManager.metaPersonalizada.first()
            if (meta > 0) {
                binding.etMeta.setText(meta.toString())
            }
            
            val isDarkMode = dataStoreManager.modoEscuro.first()
            binding.switchModoEscuro.isChecked = isDarkMode
        }

        binding.btnSalvarSettings.setOnClickListener {
            val metaTexto = binding.etMeta.text.toString()
            val metaValor = metaTexto.toDoubleOrNull() ?: 0.0
            val isDarkMode = binding.switchModoEscuro.isChecked

            lifecycleScope.launch {
                dataStoreManager.salvarMeta(metaValor)
                dataStoreManager.salvarModoEscuro(isDarkMode)
                
                // Aplicar modo escuro imediatamente
                applyDarkMode(isDarkMode)
                
                Toast.makeText(this@SettingsActivity, getString(R.string.msg_settings_saved), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}