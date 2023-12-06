package com.assignment.currency.data.repository

import com.assignment.currency.data.local.entity.CurrencyResponse
import com.assignment.currency.data.remote.ApiGateway
import com.assignment.currency.domain.model.Resource
import com.assignment.currency.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class CurrencyRepositoryImpl(
    private val api: ApiGateway
): CurrencyRepository {

    override fun getCurrencyRatesList(): Flow<Resource<CurrencyResponse>> = flow {

        try {
            val newRates = getRemoteCurrencyRates()
            emit(Resource.Success(newRates))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection",
                    data = null
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong! ${e.message}",
                    data = null
                )
            )
        }

    }

    private suspend fun getRemoteCurrencyRates(): CurrencyResponse {
        val response = api.getLatestRates()
        return response
    }

}