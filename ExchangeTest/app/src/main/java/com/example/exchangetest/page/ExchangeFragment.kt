package com.example.exchangetest.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.exchangetest.CommonData
import com.example.exchangetest.R
import com.example.exchangetest.databinding.FragmentExchangeBinding
import com.example.exchangetest.util.*

class ExchangeFragment : Fragment() {

    private val viewModel: ExchangeViewModel by lazy {
        ViewModelProviders.of(activity!!)
            .get(ExchangeViewModel::class.java)
            .also {
                it.lifecycleOwner = this
            }
    }

    private lateinit var binding: FragmentExchangeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 환율 api 통신
        viewModel.getExchange { success ->
            if (success) {
                binding.searchTimeTv.text = TimeHelper.msToYMDhm(viewModel.timestamp)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeBinding.inflate(inflater)

        binding.parentLl.setOnClickListener {
            if (binding.amountEt.isFocusable) {
                binding.amountEt.clearFocus()
            }
        }

        binding.receiveCountryTv.let { receiveTv ->
            LayoutUtil.corner(receiveTv, 4f)
            LayoutUtil.stroke(receiveTv, R.color.black, 2f)
            receiveTv.setOnClickListener {
                ValuePickerDialog(context!!).apply {

                    addItem(getString(R.string.korea), R.color.greyish_brown)
                    addItem(getString(R.string.japan), R.color.greyish_brown)
                    addItem(getString(R.string.philippines), R.color.greyish_brown)

                    divider = true

                    setCallback(object : OnValuePickerCallback {
                        @SuppressLint("SetTextI18n")
                        override fun onPicked(index: Int, value: String) {
                            when (index) {
                                0 -> {  // korea
                                    viewModel.quotes?.let {
                                        viewModel.currentExchangeRate = it.USDKRW.toDouble()
                                    }
                                    viewModel.currentReceiveCounty = CommonData.KOREA
                                    receiveTv.text = getString(R.string.korea)
                                    binding.exchangeRateTv.text =
                                        "${viewModel.quotes?.USDKRW} KRW / USD"
                                }
                                1 -> {  // japan
                                    viewModel.quotes?.let {
                                        viewModel.currentExchangeRate = it.USDJPY.toDouble()
                                    }
                                    viewModel.currentReceiveCounty = CommonData.JAPAN
                                    receiveTv.text = getString(R.string.japan)
                                    binding.exchangeRateTv.text =
                                        "${viewModel.quotes?.USDJPY} JPY / USD"
                                }
                                2 -> {  // philippines
                                    viewModel.quotes?.let {
                                        viewModel.currentExchangeRate = it.USDPHP.toDouble()
                                    }
                                    viewModel.currentReceiveCounty = CommonData.PHILIPPINES
                                    receiveTv.text = getString(R.string.philippines)
                                    binding.exchangeRateTv.text =
                                        "${viewModel.quotes?.USDPHP} PHP / USD"
                                }
                            }
                            val resultAmount =
                                viewModel.currentAmount * viewModel.currentExchangeRate
                            val currency = when (viewModel.currentReceiveCounty) {
                                CommonData.KOREA -> {
                                    "KRW"
                                }
                                CommonData.JAPAN -> {
                                    "JPY"
                                }
                                CommonData.PHILIPPINES -> {
                                    "PHP"
                                }
                                else -> "KRW"
                            }
                            if (resultAmount == 0.0) {
                                binding.resultMessageTv.text = "수취금액은 0 $currency 입니다"
                            } else {
                                binding.resultMessageTv.text = "수취금액은 ${resultAmount.toStringDecimal()} $currency 입니다"
                            }
                            dismiss()
                        }
                    })
                }.show()
            }
        }

        binding.amountEt.let { editText ->
            LayoutUtil.corner(editText, 4f)
            LayoutUtil.stroke(editText, R.color.black, 2f)

            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    KeyboardUtil(context!!).hideKeyboard()
                }
            }
            editText.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        editText.clearFocus()
                    }
                }
                return@setOnEditorActionListener false
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {

                        viewModel.currentAmount = s.toString().toInt()

                        if (viewModel.currentAmount > 10000 || viewModel.currentAmount < 0) {
                            ToastUtil.showToast(context!!, "송금액이 바르지 않습니다.")
                            editText.text = null
                            viewModel.currentAmount = 0
                            resultMessage()
                        } else {
                            when (viewModel.currentReceiveCounty) {
                                CommonData.KOREA -> {
                                    viewModel.quotes?.let { quotes ->
                                        viewModel.currentExchangeRate = quotes.USDKRW.toDouble()
                                    }
                                }
                                CommonData.JAPAN -> {
                                    viewModel.quotes?.let { quotes ->
                                        viewModel.currentExchangeRate = quotes.USDJPY.toDouble()
                                    }
                                }
                                CommonData.PHILIPPINES -> {
                                    viewModel.quotes?.let { quotes ->
                                        viewModel.currentExchangeRate = quotes.USDPHP.toDouble()
                                    }
                                }
                                else -> {
                                    viewModel.currentExchangeRate = 0.0
                                }
                            }
                        }
                    } else {
                        viewModel.currentAmount = 0
                    }
                    resultMessage()
                }
            })
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun resultMessage() {
        val resultAmount = viewModel.currentAmount * viewModel.currentExchangeRate
        val currency = when (viewModel.currentReceiveCounty) {
            CommonData.KOREA -> {
                "KRW"
            }
            CommonData.JAPAN -> {
                "JPY"
            }
            CommonData.PHILIPPINES -> {
                "PHP"
            }
            else -> "KRW"
        }
        binding.resultMessageTv.text = if (resultAmount == 0.0) {
            "수취금액은 $resultAmount $currency 입니다"
        } else {
            "수취금액은 ${resultAmount.toStringDecimal()} $currency 입니다"
        }

    }
}