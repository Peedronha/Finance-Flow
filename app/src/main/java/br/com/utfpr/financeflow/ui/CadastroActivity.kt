package br.com.utfpr.financeflow.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.utfpr.financeflow.R
import br.com.utfpr.financeflow.data.local.database.AppDatabase
import br.com.utfpr.financeflow.data.local.datastore.DataStoreManager
import br.com.utfpr.financeflow.data.repository.TransactionRepository
import br.com.utfpr.financeflow.databinding.ActivityCadastroBinding
import br.com.utfpr.financeflow.model.CategoriaTransacao
import br.com.utfpr.financeflow.model.TipoTransacao
import br.com.utfpr.financeflow.viewModel.FinanceViewModel
import br.com.utfpr.financeflow.viewModel.FinanceViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    
    private val viewModel: FinanceViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        val dataStoreManager = DataStoreManager(this)
        FinanceViewModelFactory(repository, dataStoreManager)
    }

    private var dataSelecionada: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            dataSelecionada = it.getLong("DATA_SELECIONADA")
        }

        configurarBotoes()
        atualizarBotaoData()
        sincronizarVisibilidadeCategoria(binding.radioGroupTipo.checkedRadioButtonId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("DATA_SELECIONADA", dataSelecionada)
    }

    private fun configurarBotoes() {
        binding.radioGroupTipo.setOnCheckedChangeListener { _, checkedId ->
            sincronizarVisibilidadeCategoria(checkedId)
        }

        binding.data.setOnClickListener {
            abrirDatePicker()
        }

        binding.salvarTransacao.setOnClickListener {
            val nome = binding.nomeTransacao.text.toString()
            val descricao = binding.descricao.text.toString()
            val valorTexto = binding.valor.text.toString()
            
            val tipo = if (binding.radioGroupTipo.checkedRadioButtonId == R.id.receita) {
                TipoTransacao.RECEITA
            } else {
                TipoTransacao.DESPESA
            }

            val categoria = if (tipo == TipoTransacao.RECEITA) {
                CategoriaTransacao.RECEITA
            } else {
                when (binding.radioGroupCategoria.checkedRadioButtonId) {
                    R.id.rbFixa -> CategoriaTransacao.FIXA
                    R.id.rbInvestimento -> CategoriaTransacao.INVESTIMENTO
                    else -> CategoriaTransacao.VARIAVEL
                }
            }

            if (validarCampos(nome, valorTexto)) {
                val valor = BigDecimal(valorTexto)
                viewModel.salvar(nome, descricao, valor, dataSelecionada, tipo, categoria)
                Toast.makeText(this, getString(R.string.msg_success_save), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun sincronizarVisibilidadeCategoria(checkedId: Int) {
        if (checkedId == R.id.despesa) {
            binding.tvCategoria.visibility = View.VISIBLE
            binding.radioGroupCategoria.visibility = View.VISIBLE
        } else {
            binding.tvCategoria.visibility = View.GONE
            binding.radioGroupCategoria.visibility = View.GONE
        }
    }

    private fun validarCampos(nome: String, valor: String): Boolean {
        var isValid = true
        if (nome.isBlank()) {
            binding.tilNome.error = getString(R.string.error_empty_name)
            isValid = false
        } else {
            binding.tilNome.error = null
        }
        
        if (valor.isBlank()) {
            binding.tilValor.error = getString(R.string.error_empty_value)
            isValid = false
        } else {
            val valorNum = valor.toDoubleOrNull()
            if (valorNum == null || valorNum <= 0) {
                binding.tilValor.error = getString(R.string.error_invalid_value)
                isValid = false
            } else {
                binding.tilValor.error = null
            }
        }
        return isValid
    }

    private fun abrirDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.title_date_picker))
            .setSelection(dataSelecionada)
            .build()

        picker.addOnPositiveButtonClickListener { timestamp ->
            dataSelecionada = timestamp
            atualizarBotaoData()
        }

        picker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun atualizarBotaoData() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataFormatada = sdf.format(Date(dataSelecionada))
        binding.data.text = getString(R.string.label_date, dataFormatada)
    }
}
