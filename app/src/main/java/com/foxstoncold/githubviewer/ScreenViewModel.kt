package com.foxstoncold.githubviewer

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.foxstoncold.githubviewer.data.DataRepository
import com.foxstoncold.githubviewer.data.ExplorerContentItem
import com.foxstoncold.githubviewer.data.TransmitStatus
import com.foxstoncold.githubviewer.screens.SearchItemModel
import com.foxstoncold.githubviewer.screens.navigation.Screen
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

    private lateinit var navController: NavController
    private val _explorerContents: MutableStateFlow<List<ExplorerContentItem>> = MutableStateFlow(emptyList())

    //endregion

    init {
        sl.en()
        makeSearchRequest(TextFieldValue("ksp-interstellar"))
        vmScope.launch {
//            fillStubs()
        }
    }

    //region public fields

    val searchQuery: StateFlow<TextFieldValue> = _searchQuery.asStateFlow()
    val searchItems: StateFlow<List<SearchItemModel>> = _searchItems.asStateFlow()
    val transmitStatus: StateFlow<TransmitStatus> = _transmitStatus.asStateFlow()

    val explorerContents: StateFlow<List<ExplorerContentItem>> = _explorerContents.asStateFlow()

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
            fillStubsSearch()
        }

        val delay: Long = 600
        if (!this::debounceJob.isInitialized || debounceJob.isCompleted || debounceJob.isCancelled){
            debounceJob = vmScope.launch {
                delay(delay)
                requestSearch(text)
            }
        }
        else if (debounceJob.isActive){
            debounceJob.cancel()
            debounceJob = vmScope.launch {
                delay(delay)
                requestSearch(text)
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
            requestSearch(_searchQuery.value.text)
        }
    }

    fun clearList(){
        vmScope.launch {
            _searchQuery.emit(TextFieldValue())
            _searchItems.emit(emptyList())
        }
    }

    //endregion

    //region navigation

    fun passNavController(navController: NavController){
        this.navController = navController
    }

    fun enterExplorerRepo(repoName: String, apiUrl: String){
        navController.navigate(Screen.FileExplorer.createRoute(repoName))
        vmScope.launch {
            requestContents(apiUrl)
        }
    }

    fun enterExplorerFolder(completePath: String){

    }

    fun popupFileExplorer(){
        navController.popBackStack()
    }

    //endregion



    //region private fun

    private suspend fun fillStubsSearch(){
        val list = mutableListOf<SearchItemModel>()
        for (i in 0..6){
            list.add(SearchItemModel(type = 0, id = i, stub = true, name = "", avatarUrl = "", profileUrl = ""))
        }

        _searchItems.emit(list)
    }

    private suspend fun requestSearch(query: String){
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

    private suspend fun requestContents(url: String){
        _transmitStatus.emit(TransmitStatus.LOADING)
//        fillStubsSearch()

        val response = dr.getExplorerContents(url).last()
        if (response.status != TransmitStatus.READY) {
            sl.s("${response.status}: ${response.status.msg}")
            _transmitStatus.emit(response.status)
            return
        }

        val list = response.data!!
            .sortedBy { it.type }
            .onEach { it.formatedSize = formatFileSize(it.size) }
        _explorerContents.emit(list)
        _transmitStatus.emit(TransmitStatus.READY)
    }

    private fun formatFileSize(bytes: Int): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var unitIndex = 0
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }
        return if (size % 1.0 == 0.0) "%.0f %s".format(size, units[unitIndex]) else "%.1f %s".format(size, units[unitIndex])
    }

    //endregion



}