package br.com.utfpr.financeflow.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.utfpr.financeflow.data.local.database.AppDatabase
import br.com.utfpr.financeflow.data.local.datastore.DataStoreManager
import br.com.utfpr.financeflow.data.repository.TransactionRepository
import br.com.utfpr.financeflow.databinding.ActivityMainBinding
import br.com.utfpr.financeflow.ui.adapter.TransacaoAdapter
import br.com.utfpr.financeflow.viewModel.FinanceViewModel
import br.com.utfpr.financeflow.viewModel.FinanceViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FinanceViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        val dataStoreManager = DataStoreManager(this)
        FinanceViewModelFactory(repository, dataStoreManager)
    }
    private val adapter = TransacaoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTransacoes.adapter = adapter
        binding.rvTransacoes.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listaTransacoes.collect { lista ->
                    adapter.submitList(lista)
                    
                    if (lista.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.rvTransacoes.visibility = View.GONE
                    } else {
                        binding.tvEmptyState.visibility = View.GONE
                        binding.rvTransacoes.visibility = View.VISIBLE
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.planejamentoFinanceiro.collect { data ->
                    val total = data.rendaLiquida.subtract(data.gastosFixos).subtract(data.gastosVariaveis).subtract(data.investimentos)
                    val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                    binding.tvSaldo.text = nf.format(total)
                }
            }
        }

        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        binding.cardSaldo.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettings?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}