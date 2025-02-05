package com.foxstoncold.githubviewer

import androidx.compose.ui.text.input.TextFieldValue
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
import kotlinx.coroutines.runBlocking

class ScreenViewModel(private val dr: DataRepository): ViewModel() {

    //region private fields
    private val vmScope = CoroutineScope(Dispatchers.Default)

    private val _searchQuery: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    private val _searchItems: MutableStateFlow<List<SearchItemModel>> = MutableStateFlow(emptyList())
    private val _transmitStatus: MutableStateFlow<TransmitStatus> = MutableStateFlow(TransmitStatus.IDLE)
    private lateinit var debounceJob: Job

    //endregion

    init {
        sl.en()
        vmScope.launch {
//            fillStubs()
        }
    }

    //region public fields

    val searchQuery: StateFlow<TextFieldValue> = _searchQuery.asStateFlow()
    val searchItems: StateFlow<List<SearchItemModel>> = _searchItems.asStateFlow()
    val transmitStatus: StateFlow<TransmitStatus> = _transmitStatus.asStateFlow()

    //endregion


    //region public methods

    fun makeSearchRequest(query: TextFieldValue){
        runBlocking {
            _searchQuery.emit(query)
        }
        val text = query.text

        if (text.length < 3){
            if (this::debounceJob.isInitialized)
                debounceJob.cancel()
            vmScope.launch {
                _searchItems.emit(emptyList())
            }

            return
        }

        vmScope.launch {
            _transmitStatus.emit(TransmitStatus.LOADING)
            fillStubs()
        }

        val delay: Long = 600
        if (!this::debounceJob.isInitialized || debounceJob.isCompleted || debounceJob.isCancelled){
            debounceJob = vmScope.launch {
                delay(delay)
                request(text)
            }
        }
        else if (debounceJob.isActive){
            debounceJob.cancel()
            debounceJob = vmScope.launch {
                delay(delay)
                request(text)
            }
        }
    }

    fun updateExpanded(id: Int, expanded: Boolean) = vmScope.launch {
        val list = _searchItems.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index==-1) return@launch

        list[index] = list[index].copy(expanded = expanded)
        _searchItems.emit(list)
    }

    fun repeatSearchRequest() {
        vmScope.launch {
            request(_searchQuery.value.text)
        }
    }

    fun clearList(){
        vmScope.launch {
            _searchItems.emit(emptyList())
        }
    }

    //endregion

    //region private fun

    private suspend fun fillStubs(){
        val list = mutableListOf<SearchItemModel>()
        for (i in 0..6){
            list.add(SearchItemModel(type = 0, id = i, stub = true, name = "", avatarUrl = "", profileUrl = ""))
        }

        _searchItems.emit(list)
    }

    private suspend fun request(query: String){
        val response1 = dr.searchUsers(query).last()
        if (response1.status != TransmitStatus.READY) {
            sl.s("${response1.status}: ${response1.status.msg}")
            _transmitStatus.emit(response1.status)
            return
        }
        val response2 = dr.searchRepos(query).last()
        if (response2.status != TransmitStatus.READY) {
            sl.s("${response2.status}: ${response2.status.msg}")
            _transmitStatus.emit(response2.status)
            return
        }

        _searchItems.emit((response1.data!! + response2.data!!).sortedBy { it.name })

        _transmitStatus.emit(TransmitStatus.READY)
    }

    //endregion



}