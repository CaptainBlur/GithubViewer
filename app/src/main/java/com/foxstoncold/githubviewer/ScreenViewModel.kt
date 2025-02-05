package com.foxstoncold.githubviewer

import androidx.lifecycle.ViewModel
import com.foxstoncold.githubviewer.data.DataRepository
import com.foxstoncold.githubviewer.data.TransmitStatus
import com.foxstoncold.githubviewer.screens.SearchItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class ScreenViewModel(private val dr: DataRepository): ViewModel() {

    //region private fields
    private val vmScope = CoroutineScope(Dispatchers.Default)

    private val _searchItems: MutableStateFlow<List<SearchItemModel>> = MutableStateFlow(emptyList())
    private val _transmitStatus: MutableStateFlow<TransmitStatus> = MutableStateFlow(TransmitStatus.IDLE)
    private lateinit var debounceJob: Job

    //endregion

    init {
        sl.en()
        getTestRequest()
    }

    //region public fields

    val searchItems: StateFlow<List<SearchItemModel>> = _searchItems.asStateFlow()
    val transmitStatus: StateFlow<TransmitStatus> = _transmitStatus.asStateFlow()

    //endregion


    //region public methods

    fun makeSearchRequest(query: String){
        if (query.length < 3){
            if (this::debounceJob.isInitialized)
                debounceJob.cancel()
            vmScope.launch {
                _searchItems.emit(emptyList())
            }

            return
        }

        suspend fun request(){
            _transmitStatus.emit(TransmitStatus.LOADING)

            val response1 = dr.searchUsers(query).last()
            if (response1.status != TransmitStatus.READY) {
                _transmitStatus.emit(response1.status)
                return
            }
            val response2 = dr.searchRepos(query).last()
            if (response2.status != TransmitStatus.READY) {
                _transmitStatus.emit(response2.status)
                return
            }

            _searchItems.emit((response1.data!! + response2.data!!).sortedBy { it.name })

            _transmitStatus.emit(TransmitStatus.READY)
        }

        val delay: Long = 600
        if (!this::debounceJob.isInitialized || debounceJob.isCompleted || debounceJob.isCancelled){
            debounceJob = vmScope.launch {
                delay(delay)
                request()
            }
        }
        else if (debounceJob.isActive){
            debounceJob.cancel()
            debounceJob = vmScope.launch {
                delay(delay)
                request()
            }
        }
    }

    fun getTestRequest(){
        vmScope.launch {
//            val last = dr.testSearch().last()
//            sl.f(last.status)
//
//            last.data?.let { _searchItems.emit(it)}
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