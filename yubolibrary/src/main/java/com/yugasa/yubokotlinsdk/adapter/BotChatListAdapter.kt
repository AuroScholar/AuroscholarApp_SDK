import android.annotation.SuppressLint
import com.yugasa.yubokotlinsdk.adapter.BotChatReplyAdapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.yugasa.yubokotlinsdk.R
import com.yugasa.yubokotlinsdk.Recycler
import com.yugasa.yubokotlinsdk.adapter.BotProductAdapter
import com.yugasa.yubokotlinsdk.model.ResponseBot
import com.yugasa.yubokotlinsdk.utils.AppUtil
import com.yugasa.yubokotlinsdk.utils.ClickObjectWithGroupListener

class BotChatListAdapter(
    val mContext: Context,
    mArr: List<ResponseBot>,
    val mOnClickListener: ClickObjectWithGroupListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_ITEM_1 = 2
    private var isLoading = false
    private var isSpaceCalled = true
    var mDataset: List<ResponseBot>? = null
    private val viewPool = RecycledViewPool()

    //    user view holder cantains id for bindview
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /*public constructor(string: String? = "") : this() {
            tvMyName.setText(string)
        }*/


        var tvMyName: TextView = itemView.findViewById(R.id.tvMyName)
    }

    //    user view holder cantains id for bindview
    internal class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var rvNodes: RecyclerView = itemView.findViewById(R.id.rvNodes)
        var rvProductNodes: RecyclerView = itemView.findViewById(R.id.rvProductNodes)
    }


    //    Load more layout value
    internal class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar = itemView.findViewById<View>(R.id.load_more_progressBar) as ProgressBar
    }

    override fun getItemViewType(position: Int): Int {
        return if (mDataset!![position] == null) {
            VIEW_TYPE_LOADING
        } else {
            if (mDataset!![position].typeMsg.equals("incoming", false)) {
                VIEW_TYPE_ITEM
            } else {
                VIEW_TYPE_ITEM_1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view =
                LayoutInflater.from(mContext).inflate(R.layout.my_chat_item_layout, parent, false);
            return UserViewHolder(view)
        } else if (viewType == VIEW_TYPE_ITEM_1) {
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.other_chat_item_layout, parent, false);
            return OtherViewHolder(view)
        } else if (viewType == VIEW_TYPE_LOADING) {
            val view =
                LayoutInflater.from(mContext).inflate(R.layout.load_more_footer, parent, false);
            return LoadingViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(mContext).inflate(R.layout.load_more_footer, parent, false)
            return LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (holder is UserViewHolder) {
            val modelResult: ResponseBot? = mDataset!![position]
            holder.tvMyName.text = modelResult?.text
        } else if (holder is OtherViewHolder) {
            val modelResult: ResponseBot? = mDataset!![position]
//            AppUtil.LogMsg("BotChatList", AppUtil.getCustomGson()?.toJson(modelResult))
//            holder.tvName.text = modelResult?.text
            holder.tvName.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(modelResult?.text, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(modelResult?.text)
            }
//            if (position == (mDataset!!.size - 1)) {
            modelResult?.tree?.let { it ->
                var tree: ResponseBot.Tree? = null
                when (modelResult.tree) {
                    is String -> {
                        // no needed
                    }
                    else -> {
                        tree = AppUtil.getCustomGson()?.fromJson(
                            AppUtil.getCustomGson()?.toJson(modelResult.tree),
                            ResponseBot.Tree::class.java
                        )
                    }
                }

                tree?.let {
                    if (it.options?.size!! > 0) {
                        Log.d("TAG", "onBindViewHolder:Amit_code 1")
                        setVisiblity(holder.rvNodes, modelResult?.chatHisory!!, View.VISIBLE)
                        // Create sub item view adapter
                        val imageAdapter = BotChatReplyAdapter(
                            mContext,
                            tree?.options!!,
                            object : ClickObjectWithGroupListener {
                                override fun Click(pos: Int, `object`: Any, parentPos: Int) {
                                    mOnClickListener.Click(pos, `object`, position)
                                    setVisiblity(
                                        holder.rvNodes,
                                        modelResult?.chatHisory!!,
                                        View.GONE
                                    )
                                }
                            })

                        val layoutManager = FlexboxLayoutManager(mContext, FlexDirection.ROW)
                        layoutManager.flexDirection = FlexDirection.ROW;
                        layoutManager.justifyContent = JustifyContent.FLEX_START;
                        val flexboxItemDecoration = FlexboxItemDecoration(mContext)
                        flexboxItemDecoration.setOrientation(FlexboxItemDecoration.BOTH)
                        val drawable = GradientDrawable().apply {
                            setSize(
                                mContext.resources.getDimensionPixelSize(R.dimen._0sdp),
                                mContext.resources.getDimensionPixelSize(R.dimen._0sdp)
                            )
                        }
                        flexboxItemDecoration.setDrawable(drawable)
                        holder.rvNodes.addItemDecoration(flexboxItemDecoration)

                        // Create layout manager with initial prefetch item count
                        holder.rvNodes.layoutManager = layoutManager
                        holder.rvNodes.setRecycledViewPool(viewPool)
                        holder.rvNodes.adapter = imageAdapter
                    } else {
                        setVisiblity(holder.rvNodes, modelResult?.chatHisory!!, View.GONE)
                    }

                    if (it.products != null) {
                        if (it.products?.size!! > 0) {
                            var productAdapter = BotProductAdapter(mContext, it.products)
                            var layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                            holder.rvProductNodes.layoutManager = layoutManager
                            holder.rvProductNodes.adapter = productAdapter
                        } else {
                            Log.d("TAG", "onBindViewHolder:Amit_code 4")
                            setVisiblity(holder.rvNodes, modelResult?.chatHisory!!, View.GONE)
                        }
                    }
                }
            }

            /*} else {
                setVisiblity(holder.rvNodes, modelResult?.chatHisory!!, View.GONE)
            }*/

        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    fun setVisiblity(rvNode: View, chatHist: Int, visiblity: Int) {
        if (chatHist == 0) {
            rvNode.visibility = visiblity
        } else {
            rvNode.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return if (mDataset == null) 0 else mDataset!!.size
    }

    fun changeColor(textOurSideColor: Int?, textOtherColor: Int?) {
//        textOurSideColor.let {UserViewHolder().tvMyName.setText(textOurSideColor)}
        textOtherColor.let {

        }
    }

    init {
        mDataset = mArr
    }
}
