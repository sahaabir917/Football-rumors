package com.footballrumor.android.marsrealestate.overview

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.footballrumor.android.marsrealestate.adapters.FootballAdapter
import com.footballrumor.android.marsrealestate.R
import com.footballrumor.android.marsrealestate.databinding.FragmentOverviewBinding
import com.footballrumor.android.marsrealestate.network.FootballApi
import com.footballrumor.android.marsrealestate.network.FootballList
import kotlinx.android.synthetic.main.fragment_overview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }


    lateinit var footballist : FootballList
    var isScrolling = false
    var currentItem: Int = 0
    var totalItem: Int = 0
    var scrolloutItems: Int = 0
    var pageid = 1
    val pagesize = 10
    lateinit var adapter: FootballAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var totalRecords: Int = 0
    var totalpages : Int = 0
    /**
     *
     *
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        adapter = FootballAdapter(object : FootballAdapter.OnFootballItemClickListerner{
            override  fun onClickItemClick(view:View,source : String){
                val mArgs = Bundle()
                d("argument will passed", source)
                mArgs.putString("Key", source)
                view.findNavController()
                        .navigate(R.id.action_overviewFragment_to_detailsFragment, mArgs)
            }
            override  fun onShareBtnClick(view:View, url:String){
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            override fun loadingAd() {
            }
        })
        binding.recyclerview.adapter = adapter
        binding.recyclerview.setHasFixedSize(true)

        viewModel.properties.observe(this, Observer {Footballist->
            Showalldata(Footballist)
            totalpages = Footballist.page.totalPages
            d("the total number of pages is", Footballist.page.totalPages.toString())
        })



        layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.layoutManager = layoutManager


        binding.swipe.setOnRefreshListener{
            pageid = 1
            swipe.isRefreshing = false

            var  username : String = "D4CFDE0CDD141AA"
            var password : String = "AD743A46C3C"

            var base : String  = "$username:$password"

            var authHeader : String = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)

            FootballApi.retrofitService.getdata(pagesize, pageid ,authHeader).enqueue(object : Callback<FootballList> {
                override fun onFailure(call: Call<FootballList>, t: Throwable) {
                    Log.d("after fail to retrive data ", "pageid = $pageid")
                }

                override fun onResponse(call: Call<FootballList>, response: Response<FootballList>) {

                    Showalldatas(response.body()!!)
                    totalpages = response.body()!!.page.totalPages
                }
            })
        }


        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = layoutManager.childCount
                totalItem = layoutManager.itemCount
                Log.d("current item is ", currentItem.toString())
                Log.d("total items", totalItem.toString())
                scrolloutItems =
                        (recyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (isScrolling && ((currentItem + scrolloutItems) == totalItem)) {
                    isScrolling = false
                    fetchagain(totalRecords)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

        setHasOptionsMenu(false)
        return binding.root
    }

    private fun Showalldata(footballist: FootballList) {
        adapter.addFootballData(footballist.data)
        d("total adapter is - ", adapter.footballdata.size.toString())
        adapter.notifyDataSetChanged()
    }


    private fun Showalldatas(footballist: FootballList){
        adapter.addalldata(footballist.data)
        adapter.notifyDataSetChanged()
    }

    private fun fetchagain(totalrecords: Int) {
        Handler().postDelayed(Runnable {
            kotlin.run {
                pageid++
                if (totalpages >= pageid) {
                    viewModel.retrofitcalling(pageid)
                } else {
                    Toast.makeText(context, "No more News", Toast.LENGTH_SHORT).show()
                }
            }
        }, 5000)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}