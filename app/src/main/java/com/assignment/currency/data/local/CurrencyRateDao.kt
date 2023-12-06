package com.assignment.currency.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.assignment.currency.data.local.entity.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Upsert
    suspend fun insertAll(currencyRates: List<CurrencyRateEntity>)

    @Query("SELECT * FROM currencyrateentity")
    fun getAllCurrencyRates(): Flow<List<CurrencyRateEntity>>
}