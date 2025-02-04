package com.foxstoncold.githubviewer

import androidx.lifecycle.ViewModel
import com.foxstoncold.githubviewer.data.DataRepository
import com.foxstoncold.githubviewer.screens.SearchItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class ScreenViewModel(private val dr: DataRepository): ViewModel() {

    //region private fields
    private val vmScope = CoroutineScope(Dispatchers.Default)

    private val _searchItems: MutableStateFlow<List<SearchItemModel>> = MutableStateFlow(emptyList())

    //endregion

    init {
        sl.en()
        getTestRequest()
    }

    //region public fields

    val searchItems: StateFlow<List<SearchItemModel>> = _searchItems.asStateFlow()

    //endregion


    //region public methods

    fun getTestRequest(){
        vmScope.launch {
            val last = dr.testSearch().last()
            sl.f(last.status)

            last.data?.let { _searchItems.emit(it)}
        }
    }

    fun updateExpanded(id: Int, expanded: Boolean) = vmScope.launch {
        val list = _searchItems.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index==-1) return@launch

        list[index] = list[index].copy(expanded = expanded)
        _searchItems.emit(list)
    }

    //endregion



}