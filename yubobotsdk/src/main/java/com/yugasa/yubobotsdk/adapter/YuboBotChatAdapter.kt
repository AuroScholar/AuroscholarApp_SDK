package com.yugasa.yubobotsdk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yugasa.yubobotsdk.R
import com.yugasa.yubobotsdk.model.ResponseBot
import com.yugasa.yubobotsdk.utils.YuboBotUtil
import com.yugasa.yubobotsdk.utils.ClickObjectWithGroupListener
import com.yugasa.yubobotsdk.utils.RecyclerItemClickListener

class YuboBotChatAdapter(
    private val mContext: Context,
    mArr: List<ResponseBot>,
    private val mOnClickListener: ClickObjectWithGroupListener,
    private val productListener: RecyclerItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var layoutData = HashMap<Int, Int>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_ITEM_1 = 2
    private val VIEW_TYPE_PRODUCT = 3
    private val VIEW_TYPE_REPLIES = 4
    private val VIEW_TYPE_DIMENSION_PIXEL_SIZE = 5
    private var mDataset: List<ResponseBot>? = null
    private val viewPool = RecyclerView.RecycledViewPool()

    init {
        mDataset = mArr
        layoutData[VIEW_TYPE_ITEM] = R.layout.my_chat_item_layout
        layoutData[VIEW_TYPE_LOADING] = R.layout.load_more_footer
        layoutData[VIEW_TYPE_ITEM_1] = R.layout.other_chat_item_layout
        layoutData[VIEW_TYPE_PRODUCT] = R.layout.product_item_layout
        layoutData[VIEW_TYPE_REPLIES] = R.layout.reply_item_layout
        layoutData[VIEW_TYPE_DIMENSION_PIXEL_SIZE] = R.dimen._0sdp
    }


    // User view holder cantains id for bindview
    private inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMyName: TextView = itemView.findViewById(R.id.tvMyName)
    }

    // User view holder cantains id for bindview
    private inner class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var rvNodes: RecyclerView = itemView.findViewById(R.id.rvNodes)
        var rvProductNodes: RecyclerView = itemView.findViewById(R.id.rvProductNodes)
    }


    //    Load more layout value
    private inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(mContext)
                        .inflate(layoutData[VIEW_TYPE_ITEM]!!, parent, false)
                return UserViewHolder(view)
            }
            VIEW_TYPE_ITEM_1 -> {
                val view = LayoutInflater.from(mContext)
                    .inflate(layoutData[VIEW_TYPE_ITEM_1]!!, parent, false)
                return OtherViewHolder(view)
            }
            VIEW_TYPE_LOADING -> {
                val view =
                    LayoutInflater.from(mContext)
                        .inflate(layoutData[VIEW_TYPE_LOADING]!!, parent, false)
                return LoadingViewHolder(view)
            }
            else -> {
                val view: View =
                    LayoutInflater.from(mContext)
                        .inflate(layoutData[VIEW_TYPE_LOADING]!!, parent, false)
                return LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        when (holder) {
            is UserViewHolder -> {
                val modelResult: ResponseBot = mDataset!![position]
                holder.tvMyName.text = modelResult.text
            }
            is OtherViewHolder -> {
                val modelResult: ResponseBot = mDataset!![position]
                holder.tvName.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(modelResult.text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(modelResult.text)
                }
                modelResult.tree.let {
                    var tree: ResponseBot.Tree? = null
                    when (modelResult.tree) {
                        is String -> {
                            // no needed
                        }
                        else -> {
                            tree = YuboBotUtil.getCustomGson()?.fromJson(
                                YuboBotUtil.getCustomGson()?.toJson(modelResult.tree),
                                ResponseBot.Tree::class.java
                            )
                        }
                    }

                    tree?.let {
                        if (it.options?.size!! > 0) {
                            setVisiblity(holder.rvNodes, modelResult.chatHisory!!, View.VISIBLE)
                            // Create sub item view adapter
                            val imageAdapter = YuboBotReplyAdapter(
                                mContext,
                                tree.options!!,
                                layoutData[VIEW_TYPE_REPLIES]!!,
                                object : ClickObjectWithGroupListener {
                                    override fun Click(pos: Int, `object`: Any, parentPos: Int) {
                                        mOnClickListener.Click(pos, `object`, position)
                                        setVisiblity(
                                            holder.rvNodes,
                                            modelResult.chatHisory!!,
                                            View.GONE
                                        )
                                    }
                                })

                            val layoutManager = FlexboxLayoutManager(mContext, FlexDirection.ROW)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.justifyContent = JustifyContent.FLEX_START
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
                            setVisiblity(holder.rvNodes, modelResult.chatHisory!!, View.GONE)
                        }

                        holder.rvProductNodes.visibility = View.GONE
                        if (it.products != null) {
                            if (it.products.isNotEmpty()) {
                                holder.rvProductNodes.visibility = View.VISIBLE
                                val productAdapter = YuboBotProductAdapter(
                                    mContext,
                                    it.products,
                                    layoutData[VIEW_TYPE_PRODUCT]!!,
                                    object : RecyclerItemClickListener {
                                        override fun onItemClick(pos: Int?, view: View?) {
                                            productListener.onItemClick(pos, view)
                                        }
                                    })
                                val layoutManager = LinearLayoutManager(
                                    mContext,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                holder.rvProductNodes.layoutManager = layoutManager
                                holder.rvProductNodes.adapter = productAdapter
                            } else {
                                setVisiblity(holder.rvNodes, modelResult.chatHisory!!, View.GONE)
                            }
                        }
                    }
                }
            }
            is LoadingViewHolder -> {
                holder.progressBar.isIndeterminate = true
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mDataset == null) 0 else mDataset!!.size
    }

    private fun setVisiblity(rvNode: View, chatHist: Int, visiblity: Int) {
        if (chatHist == 0) {
            rvNode.visibility = visiblity
        } else {
            rvNode.visibility = View.VISIBLE
        }
    }

    fun setBotReplyLayout(layout: Int) {
        layoutData[VIEW_TYPE_ITEM_1] = layout
    }


    fun setUserLayout(layout: Int) {
        layoutData[VIEW_TYPE_ITEM] = layout
    }


    fun setProductLayout(layout: Int) {
        layoutData[VIEW_TYPE_PRODUCT] = layout
    }

    fun setBotRepliesLayout(layout: Int) {
        layoutData[VIEW_TYPE_REPLIES] = layout
    }

    fun setDimensionPixelSize(dimens: Int) {
        layoutData[VIEW_TYPE_DIMENSION_PIXEL_SIZE] = dimens
    }

}