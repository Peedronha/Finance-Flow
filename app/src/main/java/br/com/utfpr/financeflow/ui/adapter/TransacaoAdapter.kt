package br.com.utfpr.financeflow.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.utfpr.financeflow.R
import br.com.utfpr.financeflow.model.Transacao
import br.com.utfpr.financeflow.model.TipoTransacao
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransacaoAdapter : ListAdapter<Transacao, TransacaoAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.textNome)
        val descricao: TextView = view.findViewById(R.id.textDescricao)
        val data: TextView = view.findViewById(R.id.textData)
        val valor: TextView = view.findViewById(R.id.textValor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transacao, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.nome.text = item.nomeTransacao
        holder.descricao.text = item.descricao
        
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.data.text = sdf.format(Date(item.data))
        
        val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        holder.valor.text = nf.format(item.valor)

        val color = if (item.tipo == TipoTransacao.RECEITA) {
            R.color.green
        } else {
            R.color.red
        }
        holder.valor.setTextColor(ContextCompat.getColor(holder.itemView.context, color))
    }

    class DiffCallback : DiffUtil.ItemCallback<Transacao>() {
        override fun areItemsTheSame(old: Transacao, new: Transacao) = old.id == new.id
        override fun areContentsTheSame(old: Transacao, new: Transacao) = old == new
    }
}