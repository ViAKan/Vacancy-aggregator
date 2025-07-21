package ru.practicum.android.diploma.ui.searchfilters

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFiltersFragmentBinding
import ru.practicum.android.diploma.domain.models.filters.FilterParameters
import ru.practicum.android.diploma.presentation.SearchFiltersViewModel
import ru.practicum.android.diploma.util.getThemeColor
import ru.practicum.android.diploma.util.hideKeyboardOnDone
import ru.practicum.android.diploma.util.hideKeyboardOnIconClose

class SearchFiltersFragment : Fragment() {

    private var _binding: SearchFiltersFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchFiltersViewModel>()
    private var themeColor: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SearchFiltersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeColor = requireContext().getThemeColor(com.google.android.material.R.attr.colorOnContainer)

        binding.editTextWorkplace.setOnClickListener {
            findNavController().navigate(R.id.action_searchFiltersFragment_to_workplaceFiltersFragment)
        }

        binding.editTextIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_searchFiltersFragment_to_industryFilterFragment)
        }

        binding.icon.setOnClickListener {
            binding.editText.setText("")
            binding.editText.clearFocus()
            binding.topHint.setTextColor(themeColor)
            binding.editText.hideKeyboardOnIconClose(requireContext())
        }

        binding.editText.doOnTextChanged { text, start, before, count ->
            val query = text?.toString()?.trim().orEmpty()

            binding.icon.isVisible = query.isNotEmpty()
            binding.btnApply.isVisible = query.isNotEmpty()
            binding.btnCancel.isVisible = query.isNotEmpty()
        }

        binding.editText.hideKeyboardOnDone(requireContext())
        
        binding.editText.setOnFocusChangeListener { v, hasFocus ->
            val color = if (hasFocus) {
                ContextCompat.getColor(requireContext(), R.color.blue)
            } else {
                val currentText = binding.editText.text.toString()
                if (currentText.isEmpty()) {
                    themeColor
                } else {
                    ContextCompat.getColor(requireContext(), R.color.black)
                }
            }
            binding.topHint.setTextColor(color)
        }
        
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.inputLayoutWorkplace.setEndIconOnClickListener {
            viewModel.clearWorkplace()
        }

        binding.btnCancel.setOnClickListener {
            binding.editText.setText("")
            binding.materialCheckbox.isChecked = false
            viewModel.resetFilters()
        }

        binding.btnApply.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean(SEARCH_WITH_FILTERS_KEY, true)
            }
            setFragmentResult(SEARCH_WITH_FILTERS_KEY, bundle)
            findNavController().popBackStack()
        }

        viewModel.getFiltersParametersScreen.observe(viewLifecycleOwner) {
            renderWorkplace(it)
        }

        viewModel.loadParameters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderWorkplace(state: FilterParameters) {
        val country = state.countryName
        val region = state.regionName
        val isEmpty = country.isNullOrBlank() && region.isNullOrBlank()
        val gray = ContextCompat.getColor(requireContext(), R.color.gray)

        val workplaceText = listOfNotNull(country, region)
            .filter { it.isNotBlank() }
            .joinToString(", ")

        binding.editTextWorkplace.setText(workplaceText)

        val color = if (isEmpty) {
            gray
        } else {
            requireContext().getThemeColor(com.google.android.material.R.attr.colorOnPrimary)
        }
        binding.inputLayoutWorkplace.defaultHintTextColor = ColorStateList.valueOf(color)
        binding.inputLayoutIndustry.defaultHintTextColor = ColorStateList.valueOf(color)

        val hintSize = if (isEmpty) R.style.HintAppearance_Normal else R.style.HintAppearance_Small
        binding.inputLayoutWorkplace.setHintTextAppearance(hintSize)
        binding.inputLayoutIndustry.setHintTextAppearance(hintSize)

        val icon = if (isEmpty) R.drawable.arrow_forward_24px else R.drawable.close_24px
        binding.inputLayoutWorkplace.setEndIconDrawable(icon)

        binding.btnApply.isVisible = !isEmpty
        binding.btnCancel.isVisible = !isEmpty
    }

    companion object{
        const val SEARCH_WITH_FILTERS_KEY ="search_with_filters_key"
    }
}
