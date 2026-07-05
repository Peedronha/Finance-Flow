package br.com.utfpr.financeflow.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.utfpr.financeflow.data.local.database.AppDatabase
import br.com.utfpr.financeflow.data.repository.TransactionRepository
import br.com.utfpr.financeflow.databinding.ActivityDashboardBinding
import br.com.utfpr.financeflow.viewModel.FinanceViewModel
import br.com.utfpr.financeflow.viewModel.FinanceViewModelFactory
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: FinanceViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        FinanceViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Inteligência Financeira"

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.planejamentoFinanceiro.collect { data ->
                    atualizarGrafico(data.gastosFixos.toFloat(), data.gastosVariaveis.toFloat(), data.investimentos.toFloat())
                    atualizarBarrasProgresso(data)
                    atualizarMetas(data)
                }
            }
        }
    }

    private fun atualizarGrafico(fixas: Float, variaveis: Float, investimentos: Float) {
        val entries = mutableListOf<PieEntry>()
        if (fixas > 0) entries.add(PieEntry(fixas, "Fixas"))
        if (variaveis > 0) entries.add(PieEntry(variaveis, "Variáveis"))
        if (investimentos > 0) entries.add(PieEntry(investimentos, "Investimentos"))

        val dataSet = PieDataSet(entries, "Distribuição de Gastos")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 14f

        val pieData = PieData(dataSet)
        binding.pieChart.data = pieData
        binding.pieChart.description.isEnabled = false
        binding.pieChart.centerText = "Gastos"
        binding.pieChart.animateY(1000)
        binding.pieChart.invalidate()
    }

    private fun atualizarBarrasProgresso(data: br.com.utfpr.financeflow.viewModel.PlanejamentoData) {
        val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        // Fixas
        binding.pbFixas.max = data.tetoFixas.toInt().coerceAtLeast(1)
        binding.pbFixas.progress = data.gastosFixos.toInt()
        binding.tvValorFixas.text = "${nf.format(data.gastosFixos)} / ${nf.format(data.tetoFixas)}"

        // Variáveis
        binding.pbVariaveis.max = data.tetoVariaveis.toInt().coerceAtLeast(1)
        binding.pbVariaveis.progress = data.gastosVariaveis.toInt()
        binding.tvValorVariaveis.text = "${nf.format(data.gastosVariaveis)} / ${nf.format(data.tetoVariaveis)}"

        // Investimentos
        binding.pbInvestimentos.max = data.tetoInvestimentos.toInt().coerceAtLeast(1)
        binding.pbInvestimentos.progress = data.investimentos.toInt()
        binding.tvValorInvestimentos.text = "${nf.format(data.investimentos)} / ${nf.format(data.tetoInvestimentos)}"
    }

    private fun atualizarMetas(data: br.com.utfpr.financeflow.viewModel.PlanejamentoData) {
        val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        binding.tvMetaReserva.text = "Reserva de Emergência (6 meses): ${nf.format(data.metaReserva)}"
        binding.tvMetaIndependencia.text = "Meta Independência Financeira: ${nf.format(data.metaIndependencia)}"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}