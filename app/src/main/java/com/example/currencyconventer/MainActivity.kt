package com.example.currencyconventer

import android.R
import android.R.attr.country
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconventer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel.symbols.observe(this) { symbols ->
//            val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, symbols)
//            aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
//
//            val bb: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, symbols)
//            bb.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
//
//            binding.spFromCurrency.adapter = aa
//            binding.spToCurrency.adapter = bb
//        }

        viewModel.result.observe(this) { result ->
            binding.tvResult.text = result
        }

        viewModel.loading.observe(this) { loading ->
            binding.tvResult.visibility = if (!loading) View.VISIBLE else View.GONE
            binding.CurrencyProgressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        binding.btnConvert.setOnClickListener {
            val amount = binding.amount.text.toString()
            val from = binding.spFromCurrency.selectedItem.toString()
            val to = binding.spToCurrency.selectedItem.toString()

            Log.e("AAA", "amount: $amount, from: $from, to: $to")

            if (amount.isEmpty()) {
                binding.tvResult.text = "Amount is empty"
            } else {
                viewModel.onClickConvert(amount, from, to)
            }
        }
    }


}