package com.yugasa.yubokotlinsdk

import android.content.Context
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yugasa.yubokotlinsdk.BuildConfig.*
import com.yugasa.yubokotlinsdk.model.*
import com.yugasa.yubokotlinsdk.service.RetroSeviceTask
import com.yugasa.yubokotlinsdk.utils.ClickObjectWithGroupListener
import com.yugasa.yubokotlinsdk.utils.AppConstants
import com.yugasa.yubokotlinsdk.utils.AppUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.reflect.Type
import BotChatListAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.yugasa.yubokotlinsdk.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*


class Recycler @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs), RetroSeviceTask.ServiceResultListener {

    private var layoutManager: LinearLayoutManager? = null
    var botChatListAdapter: BotChatListAdapter? = null;
    var responseSdkYubo = ArrayList<ResponseBot>()
    private val disposable = CompositeDisposable()
    protected val TAG = Recycler::class.java.simpleName
    var setAnimations: SetTypingAnimation? = null
    var id: String = ""
    var appUserId: String = ""
    var chatApi: String? = null
    var setSession: Session? = Session("", "", "", "")
    var intent: String = ""

    fun callBotApi(text: String) {
        if (text.isNotEmpty()) {
            callApiSendData(text, nodeId = responseSdkYubo[responseSdkYubo.size - 1].default_opt)
        }
    }

    fun fileUploadApi(fileName: File) {
        if (fileName != null) {
            callApiFileUpload(chatApi!!, fileName, id)
        }
    }

    fun fileUploadApi(uri: Uri) {
        if (uri != null) {
            try {
                var bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            context.contentResolver,
                            uri!!
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        uri
                    )
                }
                val bytes = ByteArrayOutputStream()
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false)
                bitmap = AppUtil.rotateImageIfRequired(context, bitmap, uri!!)!!
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val destination = File(
                    context.filesDir,
                    System.currentTimeMillis().toString() + ".jpg"
                )
                val fo: FileOutputStream
                try {
                    destination.createNewFile()
                    val fileUri: Uri = Uri.fromFile(destination)
                    Log.e("Profile  ", fileUri.path!!)
                    fo = FileOutputStream(destination)
                    fo.write(bytes.toByteArray())
                    fo.close()
                    val file = FileUtils.getFile(context, fileUri)
                    callApiFileUpload(chatApi!!, file, id)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: java.lang.Exception) {
                Log.e("excepteion", "" + e.message)
            }
        }
    }

    // Init method
    init {

        botChatListAdapter =
            BotChatListAdapter(context, responseSdkYubo, object : ClickObjectWithGroupListener {
                override fun Click(position: Int, `object`: Any, parentPos: Int) {
                    var replyNode = `object` as Option
                    callApiSendData(
                        "",
                        nodeId = replyNode.link,
                        responseBot = responseSdkYubo[parentPos],
                        option = replyNode
                    )
                }
            })

        adapter = botChatListAdapter

    }

    fun changeTextColor(textOurSideColor: Int?, textOurOtherColor: Int?) {
//        botChatListAdapter.
    }

    private fun callApiFileUpload(
        strApi: String,
        fileName: File,
        userId: String,
        nodeId: String? = "False"
    ) {

        var map = HashMap<String, RequestBody>()
        map["fileList"] = userId.toRequestBody("text/plain".toMediaType())
        map["fileList"] = strApi.toRequestBody("text/plain".toMediaType())

        val requestImageFile = fileName.asRequestBody("image/*".toMediaTypeOrNull())
        val multipart =
            MultipartBody.Part.createFormData("fileList", fileName.name, requestImageFile)

        val retroSeviceTask = RetroSeviceTask()
        retroSeviceTask.setApiUrl(BASE_URL)
        retroSeviceTask.setApiMethod(AppConstants.Service_File_Upload_Demo)
        retroSeviceTask.setDisposable(disposable)
        retroSeviceTask.setRequestBodyMap(map)
        retroSeviceTask.setFile(multipart)
        retroSeviceTask.setListener(this)
        retroSeviceTask.setResultType(ResponseBot::class.java)
        retroSeviceTask.createFormdataWithImage(
            userId.toRequestBody("text/plain".toMediaType()),
            strApi.toRequestBody("text/plain".toMediaType())
        )

    }

    //
    private fun callApiSendData(
        text: String,
        nodeId: String? = "False",
        followUp: Int? = 0,
        responseBot: ResponseBot? = null,
        option: Option? = null,
    ) {
        if (option != null) {
            if (option.score != null) {
                option.score.let {
                    var soc = AppUtil.getCustomGson()?.fromJson(
                        AppUtil.getCustomGson()?.toJson(it),
                        Option.Score::class.java
                    )
                    var score1 = HashMap<String, Any>()
                    var score2 = HashMap<String, Any>()
                    score2["weight"] = soc!!.weight!!
                    score2["label"] = soc!!.label!!
                    score1[intent] = score2
                    setSession!!.score = score1
                }
            }
        }
        setSession!!.appUserId = appUserId
        var setData = Data(
            id,
            chatApi!!,
            "False",
            "False",
            text.ifEmpty {
                option!!.option
            }!!,
            nodeId!!,
            setSession!!,
            "pro",
            "True",
            "False",
            "False",
            typeMsg = "incoming",
        )

        var setParams = RequestYubo(setData)

        var dataPrint: String = AppUtil.getCustomGson()?.toJson(setData)!!
        var setDataList: ResponseBot =
            AppUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
//        botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
        setAnimations?.showAnimation(true)
        val retroSeviceTask = RetroSeviceTask()
        retroSeviceTask.setApiUrl(BASE_URL)
        when (followUp) {
            0 -> {
                responseSdkYubo.add(setDataList)
                retroSeviceTask.setApiMethod(AppConstants.Service_Method_Demo)
                retroSeviceTask.setResultType(ResponseBot::class.java)
            }
            1 -> {
                retroSeviceTask.setApiMethod(AppConstants.Service_Follow_UP)
                retroSeviceTask.setResultType(FollowupResponse::class.java)
            }
            else -> {
                responseSdkYubo.add(setDataList)
                retroSeviceTask.setApiMethod(AppConstants.Service_Method_Demo)
                retroSeviceTask.setResultType(ResponseBot::class.java)
            }
        }
        retroSeviceTask.setDisposable(disposable)
        retroSeviceTask.setParamObj(setParams)
        retroSeviceTask.setListener(this)
        retroSeviceTask.createJsondata()
    }

    fun callApiFirstMsg(strApi: String) {
        this.chatApi = strApi
        var setParams = ParamRequest(strApi, id, "Android", setSession!!)
        val retroSeviceTask = RetroSeviceTask()
        retroSeviceTask.setApiUrl(BASE_URL_NEW)
        retroSeviceTask.setApiMethod(AppConstants.Service_Method_Get_Msg)
        retroSeviceTask.setDisposable(disposable)
        retroSeviceTask.setParamObj(setParams)
        retroSeviceTask.setListener(this)
        retroSeviceTask.setResultType(YuboFirstMsgResult::class.java)
        retroSeviceTask.createJsondata()
    }

    /*
    *
    * */
    fun callChatHistory(clientId: String, id: String, map: HashMap<String, String>, clearChat: Boolean) {
        this.chatApi = clientId
        this.id = id
        this.appUserId = map["appUserId"]!!
        setSession!!.appUserId = map["appUserId"]!!
        var setParams = ParamRequest(clientId, (id), map["type"]!!, setSession!!, clearChat)
        val retroSeviceTask = RetroSeviceTask()
        retroSeviceTask.setApiUrl(BASE_URL_NEW1)
        retroSeviceTask.setApiMethod(AppConstants.Service_Method_chatHistory)
        retroSeviceTask.setDisposable(disposable)
        retroSeviceTask.setParamObj(setParams)
        retroSeviceTask.setListener(this)
        retroSeviceTask.setResultType(PreviousChatHistory::class.java)
        retroSeviceTask.createJsondata()
    }

    override fun onResult(
        serviceUrl: String?,
        serviceMethod: String?,
        httpStatusCode: Int,
        resultType: Type?,
        resultObj: Any?
    ) {
        try {
            when {
                serviceMethod.equals(AppConstants.Service_Method_Demo, true) -> {
                    handleResult(resultObj)
                }
                serviceMethod.equals(AppConstants.Service_Method_Get_Msg, true) -> {
                    handleMsgResult(resultObj)
                }
                serviceMethod.equals(AppConstants.Service_Method_Get_Chat, true) -> {

                }
                serviceMethod.equals(AppConstants.Service_Method_chatHistory, true) -> {
                    handleHistoryResult(resultObj)
                }
                serviceMethod.equals(AppConstants.Service_File_Upload_Demo, true) -> {
                    handleImageResult(resultObj)
                }
                serviceMethod.equals(AppConstants.Service_Follow_UP, true) -> {
                    handleFollowUp(resultObj)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Chat Bot Error : " + e.message)
        }
    }

    // handle bot response
    private fun handleResult(resultObj: Any?) {
        var requestYubo: ResponseBot = resultObj as ResponseBot
        setSession = requestYubo.session
        intent = requestYubo.intent!!
        if (requestYubo.status.equals("success", true)) {
            requestYubo.chatHisory = if (requestYubo.replies!!.isNotEmpty()) {
                1
            } else {
                0
            }
            val trees = if (requestYubo.tree is String) {
                val tr = ResponseBot.Tree()
                tr.text = requestYubo.tree?.toString()
                tr
            } else {
                AppUtil.getCustomGson()?.fromJson(
                    AppUtil.getCustomGson()?.toJson(requestYubo.tree),
                    ResponseBot.Tree::class.java
                )!!
            }
            trees.options = requestYubo.replies
            requestYubo.tree = trees
            responseSdkYubo.add(requestYubo)
            setAnimations?.showAnimation(false)
            botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
//            botChatListAdapter?.notifyDataSetChanged()
            setAnimations?.scrollPosition(
                responseSdkYubo.size - 1,
                responseSdkYubo[responseSdkYubo.size - 1].type_option
            )

            MainScope().launch(Dispatchers.Default) {
                var tree: ResponseBot.Tree? = null
                when (requestYubo.tree) {
                    is String -> {
                        // no needed
                    }
                    else -> {
                        tree = AppUtil.getCustomGson()?.fromJson(
                            AppUtil.getCustomGson()?.toJson(requestYubo.tree),
                            ResponseBot.Tree::class.java
                        )
                    }
                }
                tree?.followup?.let {
//                    it.delayInMs?.toLong()?.let { it1 -> delay(it1) }
                    if (it.nodeName!!.isNotEmpty() && it.nodeName!! != "Select Node") {
                        Handler(Looper.getMainLooper()).postDelayed({
                            callApiSendData(
                                text = it.nodeName!!,
                                nodeId = it.nodeName,
                                followUp = 1,
                            )
                        }, it.delayInMs!!.toLong())
                    }
                }
            }

        } else if (requestYubo.status.equals("fallback", true)) {
            responseSdkYubo.add(requestYubo)
            setAnimations?.showAnimation(false)
            botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
//            botChatListAdapter?.notifyDataSetChanged()
            setAnimations?.scrollPosition(
                responseSdkYubo.size - 1,
                responseSdkYubo[responseSdkYubo.size - 1].type_option
            )
        }
    }

    // Handle Follow up response
    private fun handleFollowUp(resultObj: Any?) {
        var followResponse = resultObj as FollowupResponse
        var setSession = Session("", "", "")
        var setData = Data(
            id,
            chatApi!!,
            "False",
            "False",
            followResponse.text!!,
            "False",
            setSession,
            "pro",
            "True",
            "False",
            "False",
            typeMsg = "outgoing",
        )

        var setParams = RequestYubo(setData)

        var dataPrint = AppUtil.getCustomGson()?.toJson(setData)!!
        var setDataList =
            AppUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
        setDataList.chatHisory = 1
        var trees: ResponseBot.Tree = ResponseBot.Tree()
        trees.options = followResponse.replies
        setDataList.tree = trees
        setDataList.type_option = followResponse.type_option
        responseSdkYubo.add(setDataList)
        setAnimations?.showAnimation(false)
        botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
        setAnimations?.scrollPosition(
            responseSdkYubo.size - 1,
            responseSdkYubo[responseSdkYubo.size - 1].type_option
        )

        followResponse.tree?.followup?.let {
//                    it.delayInMs?.toLong()?.let { it1 -> delay(it1) }
            if (it.nodeName!!.isNotEmpty() && it.nodeName!! != "Select Node") {
                Handler(Looper.getMainLooper()).postDelayed({
                    callApiSendData(
                        text = it.nodeName!!,
                        nodeId = it.nodeName,
                        followUp = 1,
                    )
                }, it.delayInMs!!.toLong())
            }
        }

    }


    // handle Image file upload msg response
    private fun handleImageResult(resultObj: Any?) {
        var requestYubo: ResponseBot = resultObj as ResponseBot
//        if (requestYubo.success!!) {
        var setSession = Session("", "", "")
        var setData = Data(
            id,
            chatApi!!,
            "False",
            "False",
            "File upload successfully",
            "False",
            setSession,
            "pro",
            "True",
            "False",
            "False",
            typeMsg = "outgoing",
        )

        var dataPrint: String = AppUtil.getCustomGson()?.toJson(setData)!!
        var setDataList: ResponseBot =
            AppUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
        responseSdkYubo.add(setDataList)

        responseSdkYubo[responseSdkYubo.size - 1].type_option = requestYubo.type_option
        botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
        setAnimations?.scrollPosition(
            responseSdkYubo.size - 1,
            responseSdkYubo[responseSdkYubo.size - 1].type_option
        )
//        }
    }

    // handle first msg response
    private fun handleMsgResult(resultObj: Any?) {
        var yuboFirstMsgResult: YuboFirstMsgResult = resultObj as YuboFirstMsgResult
        if (yuboFirstMsgResult.status.equals("success")) {
            var setSession = Session("", "", "")
            var setData = Data(
                id,
                yuboFirstMsgResult.data.clientId,
                "False",
                "False",
                yuboFirstMsgResult.data.renderData.msg,
                "False",
                setSession,
                "pro",
                "True",
                "False",
                "False",
                typeMsg = "incoming"
            )

            var dataPrint: String = AppUtil.getCustomGson()?.toJson(setData)!!
            var setDataList: ResponseBot =
                AppUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
            responseSdkYubo.add(setDataList)
            botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
//            botChatListAdapter?.notifyDataSetChanged()

        } else if (yuboFirstMsgResult.status.equals("sessionExpired")) {
            AppUtil.showToast(context, yuboFirstMsgResult.message)
        } else {
            AppUtil.showToast(context, yuboFirstMsgResult.message)
        }
    }

    // handle history response
    private fun handleHistoryResult(resultObj: Any?) {
        var previousChatHistory: PreviousChatHistory = resultObj as PreviousChatHistory
        setSession = if (previousChatHistory.message != null) {
            if (previousChatHistory.message!!.sessionData != null) {
                previousChatHistory.message!!.sessionData
            } else {
                Session("", "", "")
            }
        } else {
            Session("", "", "")
        }
        if (previousChatHistory.success!!) {
            responseSdkYubo.clear()
            previousChatHistory.formData?.let {
                val requestYubo = ResponseBot()
                requestYubo.text = previousChatHistory.formData?.text
                requestYubo.type_option = it.typeOpt
                val tree = ResponseBot.Tree(
                    node_id = it.nodeId,
                    node_name = it.nodeName,
                    default = it.default,
                    options = it.options,
                    text = it.text
                )
                it.followup?.let {
//                    it.delayInMs?.toLong()?.let { it1 -> delay(it1) }
                    if (it.nodeName!!.isNotEmpty() && it.nodeName!! != "Select Node") {
                        Handler(Looper.getMainLooper()).postDelayed({
                            callApiSendData(
                                text = it.nodeName!!,
                                nodeId = it.nodeName,
                                followUp = 1,
                            )
                        }, it.delayInMs!!.toLong())
                    }
                }
                requestYubo.tree = tree
                requestYubo.chatHisory = 1
                responseSdkYubo.add(requestYubo)
            }

            previousChatHistory.message?.userChat?.forEach {
                val requestYubo = ResponseBot()
                requestYubo.text = it.text
                requestYubo.typeMsg = it.messageType
                requestYubo.replies = it.replies
                requestYubo.node = it.node
                requestYubo.node_id = it.node
                requestYubo.type_option = it.type_option
                requestYubo.chatHisory = 1
                requestYubo.intent = it.intent

                val tree = ResponseBot.Tree(
                    node_id = it.node,
                    options = it.replies,
                    text = it.text
                )

                requestYubo.tree = tree
                responseSdkYubo.add(
                    AppUtil.getCustomGson()?.fromJson(
                        AppUtil.getCustomGson()?.toJson(requestYubo),
                        ResponseBot()::class.java
                    )!!
                )
            }

            setAnimations?.showAnimation(false)
            setAnimations?.scrollPosition(
                responseSdkYubo.size - 1,
                responseSdkYubo.get(responseSdkYubo.size - 1).type_option
            )
            botChatListAdapter?.notifyItemInserted(responseSdkYubo.size - 1)

        }
    }

    // inter face for typing option & animation
    interface SetTypingAnimation {
        fun showAnimation(boolean: Boolean)
        fun scrollPosition(scrollToBottom: Int, typeOption: Boolean? = true)
    }
}
