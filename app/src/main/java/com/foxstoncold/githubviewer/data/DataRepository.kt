package com.foxstoncold.githubviewer.data

import com.foxstoncold.githubviewer.screens.SearchItemModel
import com.foxstoncold.githubviewer.sl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class DataRepository(
    private val api: GHApi
) {
    private val drScope = CoroutineScope(Dispatchers.IO)

    fun printTest(){
        sl.w("test")
    }

    init {
        drScope.launch {
//            api.searchRepos("captain").collect{
//                sl.f(it.status.msg)
//                it.data?.let { sl.i(it) }
//            }
        }
    }

    //region search requests

    fun testSearch(): Flow<ResponseData<List<SearchItemModel>>> = api.searchUsers("java")

    //endregion


}