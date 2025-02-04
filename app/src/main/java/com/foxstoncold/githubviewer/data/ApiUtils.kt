package com.foxstoncold.githubviewer.data

import com.foxstoncold.githubviewer.sl
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

enum class TransmitStatus{
    IDLE, LOADING, SERVER_ERROR, CONNECTION_ERROR, AUTH_ERROR, OTHER_ERROR, CLIENT_SIDE_ERROR, READY;

    var code: Int = -1
    fun setCode(code: Int): TransmitStatus {
        this.code = code
        return this
    }
    var msg: String = "-"
    fun setMsg(msg: String): TransmitStatus {
        this.msg = msg
        return this
    }
}

data class ResponseData<T>(
    val status: TransmitStatus = TransmitStatus.IDLE,
    val data: T? = null
)

inline fun <T,R>flyCatching(
    crossinline requestingLambda: (suspend ()-> Response<T>),
    crossinline mappingLambda: (suspend (T)->R)
): Flow<ResponseData<R>> {
    return flow {
        emit(ResponseData(TransmitStatus.LOADING))

        val response = try {
            requestingLambda()
        } catch (e: Exception){

            if (e is JsonDataException || e is IllegalArgumentException)
                emit(ResponseData(TransmitStatus.CLIENT_SIDE_ERROR.setMsg("parsing error: " + e.stackTrace.toString())))
            else{
                emit(ResponseData(TransmitStatus.CONNECTION_ERROR.setMsg(e.toString())))
            }

            return@flow
        }

        response.code()
        response.message()
//        response.raw().request.headers["Authorization"]?.let {
//            sl.w(it)
//        }

        when (response.code()){
            404-> emit(ResponseData(TransmitStatus.CONNECTION_ERROR.setCode(response.code()).setMsg(response.message())))
            403-> emit(ResponseData(TransmitStatus.AUTH_ERROR.setMsg(response.message())))
            500-> emit(ResponseData(TransmitStatus.SERVER_ERROR.setMsg(response.message()).setCode(response.code())))
            200-> {
                var mapped: R? = null

                try {
                    val body = response.body()!!
                    mapped = mappingLambda(body)
                } catch (e: Exception){
                    emit(ResponseData(TransmitStatus.CLIENT_SIDE_ERROR.setMsg("mapping error: " + e.stackTrace.toString())))
                }

                emit(ResponseData(TransmitStatus.READY.setMsg(response.message()), mapped))
            }
            else-> emit(ResponseData(TransmitStatus.OTHER_ERROR.setCode(response.code()).setMsg(response.message())))
        }
    }
        .catch {
            sl.s("caught in wrapper's flow", it)
        }
}