package com.foxstoncold.githubviewer.data

import com.foxstoncold.githubviewer.sl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
//            api.getExplorerContents("https://api.github.com/repos/Captain1986/CaptainBlackboard/contents").collect{
//                sl.f(it.status.msg)
//                it.data?.let { sl.i(it) }
//            }
//            api.getExplorerContents("https://api.github.com/repos/Captain1986/CaptainBlackboard/contents/D%230002-hack_your_printf").collect{
//                sl.f("")
//                it.data?.let { sl.i(it) }
//            }
        }
    }

    //region search requests

    fun searchUsers(query: String) = api.searchUsers(query)
    fun searchRepos(query: String) = api.searchRepos(query)
    fun getExplorerContents(apiLink: String) = api.getExplorerContents(apiLink)

    //endregion


}