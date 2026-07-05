package br.com.utfpr.financeflow.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nomeTransacao: String,
    val descricao: String,
    val valor: BigDecimal,
    val data: Long,
    val tipo: TipoTransacao,
    val categoria: CategoriaTransacao = CategoriaTransacao.VARIAVEL
)