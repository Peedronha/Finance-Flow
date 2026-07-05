package br.com.utfpr.financeflow.util

import androidx.room.TypeConverter
import br.com.utfpr.financeflow.model.TipoTransacao
import br.com.utfpr.financeflow.model.CategoriaTransacao
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromTipoTransacao(value: TipoTransacao): String {
        return value.name
    }

    @TypeConverter
    fun toTipoTransacao(value: String): TipoTransacao {
        return TipoTransacao.valueOf(value)
    }

    @TypeConverter
    fun fromCategoriaTransacao(value: CategoriaTransacao): String {
        return value.name
    }

    @TypeConverter
    fun toCategoriaTransacao(value: String): CategoriaTransacao {
        return CategoriaTransacao.valueOf(value)
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): Double {
        return value.toDouble()
    }

    @TypeConverter
    fun toBigDecimal(value: Double): BigDecimal {
        return BigDecimal.valueOf(value)
    }
}