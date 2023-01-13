package com.yugasa.yubobotsdk.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yugasa.yubobotsdk.R
import com.yugasa.yubobotsdk.model.ResponseBot
import com.yugasa.yubobotsdk.utils.RecyclerItemClickListener


class YuboBotProductAdapter(
    private val context: Context,
    var list: List<ResponseBot.Product>,
    var layout: Int,
    private val listener: RecyclerItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view =
                LayoutInflater.from(context).inflate(layout, parent, false);
            return viewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.load_more_footer, parent, false)
            return LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is viewHolder) {
            var modelResult: ResponseBot.Product? = list[position]
            Glide.with(context)
                .load(modelResult!!.image)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.imageView)

            modelResult.title?.isNotEmpty().let {
                holder.title.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(modelResult.title!!, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(modelResult.title!!)
                }
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            modelResult.price?.isNotEmpty().let {
                holder.price.text = modelResult.price!!
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.out_bubble))
            }
            modelResult.description?.isNotEmpty().let {
                var description = modelResult.description!!.replace("<br/>", "<br />")
                description = description.replace("</br>", "<br />")
                holder.description.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(description)
                }
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.light_text))
            }

            // Listener
            holder.itemView.setOnClickListener {
                listener.onItemClick(position, holder.itemView)
            }

        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    internal class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var title: TextView = itemView.findViewById(R.id.title)
        var price: TextView = itemView.findViewById(R.id.price)
        var description: TextView = itemView.findViewById(R.id.description)


    }

    //    Load more layout value
    internal class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById<View>(R.id.load_more_progressBar) as ProgressBar
        }
    }

}