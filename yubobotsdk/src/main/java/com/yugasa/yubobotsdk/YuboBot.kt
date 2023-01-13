package com.yugasa.yubobotsdk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.yugasa.yubobotsdk.adapter.YuboBotChatAdapter
import com.yugasa.yubobotsdk.listeners.YuboBotAdapterListener
import com.yugasa.yubobotsdk.model.*
import com.yugasa.yubobotsdk.service.ApiCallListener
import com.yugasa.yubobotsdk.service.ApiClient
import com.yugasa.yubobotsdk.service.RetrofitApiCall
import com.yugasa.yubobotsdk.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.lang.reflect.Type


class YuboBot(
    val context: Context,
    var data: HashMap<String, Any>,
    var setAnimations: SetTypingAnimation
) :
    ApiCallListener {

    private val TAG = "YuboBot"
    private var layoutManager: LinearLayoutManager? = null
    var yuboBotChatAdapter: YuboBotChatAdapter? = null
    private var responseSdkYubo = ArrayList<ResponseBot>()
    private var setSession: Session? = Session("", "", "", "")
    private var intent: String = ""
    private var yuboListener: YuboBotAdapterListener? = null


    // Init method
    init {
        yuboBotChatAdapter =
            YuboBotChatAdapter(context, responseSdkYubo, object : ClickObjectWithGroupListener {
                override fun Click(position: Int, `object`: Any, parentPos: Int) {
                    val replyNode = `object` as Option
                    callApiSendData(
                        "",
                        nodeId = replyNode.link,
                        responseBot = responseSdkYubo[parentPos],
                        option = replyNode
                    )
                    var map = HashMap<Int, Any>()
                    yuboListener!!.onClickYuboBotView(YuboBotConstants.YUBO_BOT_REPLIES, map)
                }
            }, object : RecyclerItemClickListener {
                override fun onItemClick(pos: Int?, view: View?) {
                    var map = HashMap<Int, Any>()
                    yuboListener!!.onClickYuboBotView(YuboBotConstants.YUBO_BOT_PRODUCT, map)
                }
            })
        if (data["yubo_id"] != null && data["id"] == YuboBotConstants.YUBO_DEVICE_ID_2) {
            data["id"] = AppUtil.getDeviceId(context, data["id"] as Int, yuboDeviceId = data["yubo_id"] as String)
        } else {
            data["id"] = AppUtil.getDeviceId(context, data["id"] as Int)
        }
        callChatHistory()
    }



    fun setYuboBotListener(yuboListener: YuboBotAdapterListener) {
        this.yuboListener = yuboListener
    }

    fun callBotApi(text: String) {
        if (text.isNotEmpty()) {
            if (responseSdkYubo[responseSdkYubo.size - 1].default_opt!!.isEmpty()) {
                callApiSendData(text)
            } else {
                callApiSendData(
                    text,
                    nodeId = responseSdkYubo[responseSdkYubo.size - 1].default_opt
                )
            }
        }
    }

    fun fileUploadApi(fileName: File) {
        if (fileName != null) {
            callApiFileUpload(data["client_id"] as String, fileName, data["id"] as String)
        }
    }

    fun fileUploadApi(uri: Uri) {
        if (uri != null) {
            try {
                var bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            context.contentResolver,
                            uri
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
                bitmap = YuboBotUtil.rotateImageIfRequired(context, bitmap, uri)!!
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
                    callApiFileUpload(data["client_id"] as String, file, data["id"] as String)
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


    private fun callApiFileUpload(
        strApi: String,
        fileName: File,
        userId: String,
        nodeId: String? = "False"
    ) {

        val map = HashMap<String, RequestBody>()
        map["fileList"] = userId.toRequestBody("text/plain".toMediaType())
        map["fileList"] = strApi.toRequestBody("text/plain".toMediaType())

        val requestImageFile = fileName.asRequestBody("image/png".toMediaTypeOrNull())
        val multipart =
            MultipartBody.Part.createFormData("fileList", fileName.name, requestImageFile)

        RetrofitApiCall.hitApi(
            ApiClient.apiInterFace.uploadWithSingleImage(
                api_link = YuboBotConstants.Service_File_Upload_Demo,
                multipart,
                userId.toRequestBody("text/plain".toMediaType()),
                strApi.toRequestBody("text/plain".toMediaType())
            ),
            this,
            YuboBotConstants.Service_File_Upload_Demo,
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
                    val soc = YuboBotUtil.getCustomGson()?.fromJson(
                        YuboBotUtil.getCustomGson()?.toJson(it),
                        Option.Score::class.java
                    )
                    val score1 = HashMap<String, Any>()
                    val score2 = HashMap<String, Any>()
                    score2["weight"] = soc!!.weight!!
                    score2["label"] = soc.label!!
                    score1[intent] = score2
                    setSession!!.score = score1
                }
            }
        }
        setSession!!.appUserId = data["appUserId"] as String
        val setData = Data(
            data["id"] as String,
            data["client_id"] as String,
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

        val setParams = RequestYubo(setData)

        val dataPrint: String = YuboBotUtil.getCustomGson()?.toJson(setData)!!
        val setDataList: ResponseBot =
            YuboBotUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
        setAnimations.showAnimation(true)
        var url = ""
        url = when (followUp) {
            0 -> {
                responseSdkYubo.add(setDataList)
                YuboBotConstants.Service_Method_Demo
            }
            1 -> {
                YuboBotConstants.Service_Follow_UP
            }
            else -> {
                responseSdkYubo.add(setDataList)
                YuboBotConstants.Service_Method_Demo
            }
        }


        RetrofitApiCall.hitApi(
            ApiClient.apiInterFace.getWithoutHeaderApi(
                api_link = url,
                requestBody = AppUtil.getRequestBody(setParams)
            ),
            this,
            url,
        )

    }

    /*
    *
    * */
    private fun callChatHistory() {
        setSession!!.appUserId = data["appUserId"]!! as String
        setSession!!.data = data
        val setParams =
            ParamRequest(
                data["client_id"] as String,
                data["id"] as String,
                data["type"]!! as String,
                setSession!!,
                data["clear_chat"]!! as Boolean
            )

        RetrofitApiCall.hitApi(
            ApiClient.apiInterFace1.getWithoutHeaderApi(
                api_link = YuboBotConstants.Service_Method_chatHistory,
                requestBody = AppUtil.getRequestBody(setParams)
            ),
            this,
            YuboBotConstants.Service_Method_chatHistory,
        )
    }

    // handle bot response
    private fun handleResult(requestYubo: ResponseBot) {
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
                YuboBotUtil.getCustomGson()?.fromJson(
                    YuboBotUtil.getCustomGson()?.toJson(requestYubo.tree),
                    ResponseBot.Tree::class.java
                )!!
            }
            trees.options = requestYubo.replies
            requestYubo.tree = trees
            responseSdkYubo.add(requestYubo)
            setAnimations.showAnimation(false)
            yuboBotChatAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
            setAnimations.scrollPosition(
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
                        tree = YuboBotUtil.getCustomGson()?.fromJson(
                            YuboBotUtil.getCustomGson()?.toJson(requestYubo.tree),
                            ResponseBot.Tree::class.java
                        )
                    }
                }
                tree?.followup?.let {
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
            setAnimations.showAnimation(false)
            yuboBotChatAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
            setAnimations.scrollPosition(
                responseSdkYubo.size - 1,
                responseSdkYubo[responseSdkYubo.size - 1].type_option
            )
        }
    }

    // Handle Follow up response
    private fun handleFollowUp(followResponse: FollowupResponse) {
        val setSession = Session("", "", "")
        val setData = Data(
            data["id"] as String,
            data["client_id"] as String,
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

        val dataPrint = YuboBotUtil.getCustomGson()?.toJson(setData)!!
        val setDataList =
            YuboBotUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
        setDataList.chatHisory = 1
        val trees: ResponseBot.Tree = ResponseBot.Tree()
        trees.options = followResponse.replies
        setDataList.tree = trees
        setDataList.type_option = followResponse.type_option
        this.responseSdkYubo.add(setDataList)
        setAnimations.showAnimation(false)
        yuboBotChatAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
        setAnimations.scrollPosition(
            responseSdkYubo.size - 1,
            responseSdkYubo[responseSdkYubo.size - 1].type_option
        )

        followResponse.tree?.followup?.let {
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
    private fun handleImageResult(message: String) {
        val setSession = Session("", "", "")
        val setData = Data(
            data["id"] as String,
            data["client_id"] as String,
            "False",
            "False",
            message,
            "False",
            setSession,
            "pro",
            "True",
            "False",
            "False",
            typeMsg = "outgoing",
        )

        val dataPrint: String = YuboBotUtil.getCustomGson()?.toJson(setData)!!
        val setDataList: ResponseBot =
            YuboBotUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
        responseSdkYubo.add(setDataList)
        setAnimations.showAnimation(false, 1)
        responseSdkYubo[responseSdkYubo.size - 1].type_option = true
        yuboBotChatAdapter?.notifyItemInserted(responseSdkYubo.size - 1)
        setAnimations.scrollPosition(
            responseSdkYubo.size - 1,
            responseSdkYubo[responseSdkYubo.size - 1].type_option
        )
    }

    // handle first msg response
    private fun handleMsgResult(yuboFirstMsgResult: YuboFirstMsgResult) {
        when (yuboFirstMsgResult.status) {
            "success" -> {
                val setSession = Session("", "", "")
                val setData = Data(
                    data["id"] as String,
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

                val dataPrint: String = YuboBotUtil.getCustomGson()?.toJson(setData)!!
                val setDataList: ResponseBot =
                    YuboBotUtil.getCustomGson()!!.fromJson(dataPrint, ResponseBot::class.java)
                responseSdkYubo.add(setDataList)
                yuboBotChatAdapter?.notifyItemInserted(responseSdkYubo.size - 1)

            }
            "sessionExpired" -> {
                YuboBotUtil.showToast(context, yuboFirstMsgResult.message)
            }
            else -> {
                YuboBotUtil.showToast(context, yuboFirstMsgResult.message)
            }
        }
    }

    // handle history response
    private fun handleHistoryResult(previousChatHistory: PreviousChatHistory) {
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
            previousChatHistory.formData?.let { it ->
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
                requestYubo.default_opt = it.node
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
                    YuboBotUtil.getCustomGson()?.fromJson(
                        YuboBotUtil.getCustomGson()?.toJson(requestYubo),
                        ResponseBot()::class.java
                    )!!
                )
            }

            setAnimations.showAnimation(false)
            setAnimations.scrollPosition(
                responseSdkYubo.size - 1,
                responseSdkYubo[responseSdkYubo.size - 1].type_option
            )
            yuboBotChatAdapter?.notifyItemInserted(responseSdkYubo.size - 1)

        }
    }

    override fun onSuccess(response: String, requestUrl: String) {
        try {
            when {
                requestUrl.equals(YuboBotConstants.Service_Method_Demo, true) -> {
                    val responseBot =
                        RetrofitApiCall.getPayload(ResponseBot::class.java, response)
                    handleResult(responseBot)
                }
                requestUrl.equals(YuboBotConstants.Service_Method_Get_Msg, true) -> {
                    val yuboFirstMsgResult =
                        RetrofitApiCall.getPayload(YuboFirstMsgResult::class.java, response)
                    handleMsgResult(yuboFirstMsgResult)
                }
                requestUrl.equals(YuboBotConstants.Service_Method_Get_Chat, true) -> {

                }
                requestUrl.equals(YuboBotConstants.Service_Method_chatHistory, true) -> {
                    val previousChatHistory =
                        RetrofitApiCall.getPayload(PreviousChatHistory::class.java, response)
                    handleHistoryResult(previousChatHistory)
                }
                requestUrl.equals(YuboBotConstants.Service_File_Upload_Demo, true) -> {
                    if (response == "success") {
                        handleImageResult("File uploaded successfully")
                    } else {
                        var fileFailure =
                            RetrofitApiCall.getPayload(FileFailureResponse::class.java, response)
                        handleImageResult(fileFailure.message)
                    }
                }
                requestUrl.equals(YuboBotConstants.Service_Follow_UP, true) -> {
                    val followUpResponse =
                        RetrofitApiCall.getPayload(FollowupResponse::class.java, response)
                    handleFollowUp(followUpResponse)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Chat Bot Error : " + e.message)
        }
    }

    override fun onError(response: String, requestUrl: String) {
        AppLogger.d(TAG, "onError: $response")
    }


    // inter face for typing option & animation
    interface SetTypingAnimation {
        fun showAnimation(boolean: Boolean, userType: Int = 0)
        fun scrollPosition(scrollToBottom: Int, typeOption: Boolean? = true)
    }

}