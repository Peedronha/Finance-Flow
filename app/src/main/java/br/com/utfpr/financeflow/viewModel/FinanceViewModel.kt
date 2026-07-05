package br.com.utfpr.financeflow.viewModel

import androidx.lifecycle.ViewModel
import br.com.utfpr.financeflow.data.repository.TransactionRepository
import br.com.utfpr.financeflow.data.local.datastore.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import br.com.utfpr.financeflow.model.CategoriaTransacao
import br.com.utfpr.financeflow.model.TipoTransacao
import br.com.utfpr.financeflow.model.Transacao
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FinanceViewModel(
    private val transactionRepository: TransactionRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val listaTransacoes = transactionRepository.todasTransacoes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun salvar(nome: String, descricao: String, valor: BigDecimal, data: Long, tipo: TipoTransacao, categoria: CategoriaTransacao) {
        val transacao = Transacao(
            nomeTransacao = nome,
            descricao = descricao,
            valor = valor,
            data = data,
            tipo = tipo,
            categoria = categoria
        )
        viewModelScope.launch {
            transactionRepository.inserir(transacao)
        }
    }
    
    // Motor de Regras 50/30/20 Integrado com Meta Personalizada do DataStore
    val planejamentoFinanceiro = combine(
        listaTransacoes,
        dataStoreManager.metaPersonalizada
    ) { lista, metaCustom ->
        val rendaLiquida = lista.filter { it.tipo == TipoTransacao.RECEITA }
            .sumOf { it.valor.toDouble() }
            .toBigDecimal()

        val gastosFixos = lista.filter { it.categoria == CategoriaTransacao.FIXA }
            .sumOf { it.valor.toDouble() }
            .toBigDecimal()

        val gastosVariaveis = lista.filter { it.categoria == CategoriaTransacao.VARIAVEL }
            .sumOf { it.valor.toDouble() }
            .toBigDecimal()

        val investimentos = lista.filter { it.categoria == CategoriaTransacao.INVESTIMENTO }
            .sumOf { it.valor.toDouble() }
            .toBigDecimal()

        // Se a meta customizada for > 0, usa ela, senão usa a padrão (6x renda)
        val metaReservaCalculada = if (metaCustom > 0) {
            BigDecimal.valueOf(metaCustom)
        } else {
            rendaLiquida.multiply(BigDecimal("6.0"))
        }

        PlanejamentoData(
            rendaLiquida = rendaLiquida,
            gastosFixos = gastosFixos,
            gastosVariaveis = gastosVariaveis,
            investimentos = investimentos,
            tetoFixas = rendaLiquida.multiply(BigDecimal("0.50")),
            tetoVariaveis = rendaLiquida.multiply(BigDecimal("0.30")),
            tetoInvestimentos = rendaLiquida.multiply(BigDecimal("0.20")),
            metaReserva = metaReservaCalculada,
            metaIndependencia = if (rendaLiquida > BigDecimal.ZERO) rendaLiquida.divide(BigDecimal("0.01"), 2, java.math.RoundingMode.HALF_UP) else BigDecimal.ZERO,
            isMetaCustom = metaCustom > 0
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlanejamentoData()
    )
}

data class PlanejamentoData(
    val rendaLiquida: BigDecimal = BigDecimal.ZERO,
    val gastosFixos: BigDecimal = BigDecimal.ZERO,
    val gastosVariaveis: BigDecimal = BigDecimal.ZERO,
    val investimentos: BigDecimal = BigDecimal.ZERO,
    val tetoFixas: BigDecimal = BigDecimal.ZERO,
    val tetoVariaveis: BigDecimal = BigDecimal.ZERO,
    val tetoInvestimentos: BigDecimal = BigDecimal.ZERO,
    val metaReserva: BigDecimal = BigDecimal.ZERO,
    val metaIndependencia: BigDecimal = BigDecimal.ZERO,
    val isMetaCustom: Boolean = false
)