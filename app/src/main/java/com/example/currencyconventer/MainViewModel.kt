package com.example.currencyconventer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class MainViewModel(
    private val exchangeRatesApi: ExchangeRatesApi
) : ViewModel() {


    val result: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val symbols: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    init {
        viewModelScope.launch {
            val symbolsResponse = exchangeRatesApi.getSymbols()
            val symbolsCodes = symbolsResponse.symbols.keys.toList()

            symbols.postValue(symbolsCodes)
        }
    }

    fun onClickConvert(amount: String, from: String, to: String) {
        viewModelScope.launch {
            try {
                loading.postValue(true)
                val response = exchangeRatesApi.convert(from, to, amount)
                result.postValue("$amount $from = ${response.result} $to\n\n (1 $from = ${response.info.rate} $to)")
                Log.e("AAA", "response: $response")
            }
           catch (e:Exception){
               result.postValue("Проблемы с интернетом")
           } finally {
               loading.postValue(false)
           }

        }
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return MainViewModel(
            APIClient.exchangeRatesApi
        ) as T
    }
}
