package br.com.utfpr.financeflow.data.repository

import br.com.utfpr.financeflow.data.local.dao.TransactionDao
import br.com.utfpr.financeflow.model.Transacao

class TransactionRepository(private val transactionDao: TransactionDao) {

    val todasTransacoes = transactionDao.getAll()

    suspend fun inserir(transacao: Transacao) {
        transactionDao.insert(transacao)
    }

    val totalReceitas = transactionDao.getTotalReceitas()
}