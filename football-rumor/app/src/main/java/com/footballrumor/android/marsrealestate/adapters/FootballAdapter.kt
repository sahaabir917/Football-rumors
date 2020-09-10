package com.footballrumor.android.marsrealestate.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.footballrumor.android.marsrealestate.R
import com.footballrumor.android.marsrealestate.network.FootballData
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_view_item.view.*
import kotlinx.android.synthetic.main.grid_view_item_2.view.*



class FootballAdapter(listener: OnFootballItemClickListerner) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var footballdata: MutableList<FootballData>
    val contentview: Int = 0
    val Adviews: Int = 1
    val contentview1 = 2
    var onFootballItemClickListerner: OnFootballItemClickListerner? = null


    init {
        footballdata = mutableListOf<FootballData>()
        this.onFootballItemClickListerner = listener

    }

    class ContentViewHolder(itemView: View, val listener: OnFootballItemClickListerner) : RecyclerView.ViewHolder(itemView) {
        fun bind(footballData: FootballData) {
            itemView.bodies.text = footballData.body.toString()
            itemView.sharebutton.setOnClickListener{view->
                listener.onShareBtnClick(view,footballData.source)
            }
            var published_date : String = footballData.publishedAt.toString()
            itemView.published_at.text =  published_date.dropLast(8)
            Picasso.get().load(footballData.image).into(itemView.imageview1)
        }
    }

    class AdviewHolder(itemView: View,val listener: OnFootballItemClickListerner) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val builder = AdLoader.Builder(
                    itemView.context, "ca-app-pub-4651020726870197/2335156824")

            builder.forUnifiedNativeAd { unifiedNativeAd ->
                val template: TemplateView = itemView.findViewById(R.id.my_template)
                template.setNativeAd(unifiedNativeAd)
            }

            val adLoader = builder.build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    class Contentview1(itemView: View, val listener: OnFootballItemClickListerner) : RecyclerView.ViewHolder(itemView){
        fun bind(footballData: FootballData){
            itemView.bodies2.text = footballData.body.toString()
            var publisheddate : String = footballData.publishedAt.toString()
            itemView.published_at2.text = publisheddate.dropLast(8)
            itemView.sharebutton1.setOnClickListener{view->
                listener.onShareBtnClick(view,footballData.source)
            }
            Picasso.get().load(footballData.image).into(itemView.imageview2)
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (position % 7 == 2) {
            Adviews
        } else {
            if(position % 2  == 0){
                contentview
            }
            else{
                contentview1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            contentview ->{
                val view =
                        LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item, parent, false)
                ContentViewHolder(view, onFootballItemClickListerner!!) }

            Adviews -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adbanner, parent, false)
                AdviewHolder(view,onFootballItemClickListerner!!)
            }

            contentview1 -> {
                val view  = LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item_2,parent,false)
                Contentview1(view,onFootballItemClickListerner!!)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }


    }

    override fun getItemCount() = footballdata.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == contentview) {
            (holder as ContentViewHolder).bind(footballdata[position])
            holder.itemView.setOnClickListener { view: View ->
                onFootballItemClickListerner!!.onClickItemClick(view, footballdata[position].source)
            }
        }

        if (getItemViewType(position) == Adviews) {
            (holder as AdviewHolder).bind()
        }

        if(getItemViewType(position) == contentview1){
            (holder as Contentview1).bind(footballdata[position])
            holder.itemView.setOnClickListener { view: View ->
                onFootballItemClickListerner!!.onClickItemClick(view, footballdata[position].source)
            }
        }
    }

    public fun addFootballData(footballDataList: List<FootballData>) {
       this.footballdata.addAll(footballDataList)
    }

    public fun addalldata(footballDataList: List<FootballData>){
        this.footballdata.clear()
        this.footballdata.addAll(footballDataList)
    }

    interface OnFootballItemClickListerner{
        fun onClickItemClick(view:View,source : String)
        fun onShareBtnClick(view:View, url:String)
        fun loadingAd()
    }
}
