package br.com.utfpr.financeflow.data.local.dao

import androidx.room.*
import br.com.utfpr.financeflow.model.Transacao
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transacoes ORDER BY data DESC")
    fun getAll(): Flow<List<Transacao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transacao: Transacao)

    @Update
    suspend fun update(transacao: Transacao)

    @Delete
    suspend fun delete(transacao: Transacao)

    @Query("SELECT SUM(valor) FROM transacoes WHERE tipo = 'RECEITA'")
    fun getTotalReceitas(): Flow<Double?>

    @Query("SELECT SUM(valor) FROM transacoes WHERE tipo = 'DESPESA'")
    fun getTotalDespesas(): Flow<Double?>
}