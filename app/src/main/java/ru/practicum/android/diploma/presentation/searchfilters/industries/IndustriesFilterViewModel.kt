package ru.practicum.android.diploma.presentation.searchfilters.industries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.repository.FiltersInteractor
import ru.practicum.android.diploma.domain.filters.repository.FiltersParametersInteractor
import ru.practicum.android.diploma.domain.models.filters.Industry
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class IndustriesFilterViewModel(
    private val interactor: FiltersInteractor,
    private val filterInteractor: FiltersParametersInteractor
) : ViewModel() {

    private val _industriesState = MutableLiveData<IndustriesUiState>()
    val industriesState: MutableLiveData<IndustriesUiState> = _industriesState

    private var industriesList: List<Industry> = emptyList()
    private var selectedId: String? = null

    init {
        getIndustries()
    }

    private fun getIndustries() {
        viewModelScope.launch {
            _industriesState.postValue(IndustriesUiState.Loading)
            interactor.getIndustries()
                .collect {
                    stateIndustry(it)
                }
        }
    }

    private fun stateIndustry(resource: Resource<List<Industry>>) {
        _industriesState.postValue(
            when (resource) {
                is Resource.Success -> {
                    industriesList = resource.data ?: emptyList()
                    selectedId = filterInteractor.getSelectedIndustryId()
                    val updatedList = industriesList.map {
                        it.copy(isSelected = it.id == selectedId)
                    }
                    IndustriesUiState.Content(updatedList)
                }

                is Resource.Error -> {
                    IndustriesUiState.Error
                }
            }
        )
    }

    fun search(query: String) {
        try {
            if (industriesList.isEmpty()) {
                _industriesState.postValue(IndustriesUiState.Error)
            }

            val filtered = industriesList.filter {
                it.name.contains(query, ignoreCase = true)
            }

            if (filtered.isEmpty()) {
                _industriesState.postValue(IndustriesUiState.Empty)
            } else {
                _industriesState.postValue(IndustriesUiState.Content(filtered))
            }
        } catch (e: IOException) {
            Log.e("Industry", "Exception search industry")
            _industriesState.postValue(IndustriesUiState.Error)
        }
    }

    fun onClickIndustry(industry: Industry) {
        filterInteractor.selectIndustry(industry.id, industry.name)
        industriesList = industriesList.map {
            it.copy(isSelected = it.id == industry.id)
        }
        _industriesState.postValue(IndustriesUiState.Content(industriesList))
    }
}
