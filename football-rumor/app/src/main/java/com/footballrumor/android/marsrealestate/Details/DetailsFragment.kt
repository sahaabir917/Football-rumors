package com.footballrumor.android.marsrealestate.Details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.footballrumor.android.marsrealestate.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_details.*

/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var args = getArguments()
        var index = args!!.getString("Key","Not passed any data ")

        webviews.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        webviews.loadUrl(index.toString())
        webviews.settings.javaScriptEnabled = true

        webviews.webViewClient = WebViewClient()
        webviews.canGoBack()
        webviews.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && webviews.canGoBack()){
             webviews.goBack()
               return@OnKeyListener true
            }
            false
        })

        MobileAds.initialize(context,getString(R.string.bannerid))
        val addrequest = AdRequest.Builder().build()
        adView.loadAd(addrequest)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.shareingicon,menu)

            if(null == getShareIntent().resolveActivity(activity!!.packageManager)){
                menu?.findItem(R.id.shareicon)?.setVisible(false)
            }
    }

    private fun getShareIntent() : Intent{
        var args = getArguments()
        var index = args!!.getString("Key","Not passed any data ")
        return ShareCompat.IntentBuilder.from(activity)
                .setText(index)
                .setType("text/plain")
                .intent
    }

    private fun ShareSuccess(){
        startActivity(getShareIntent())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       when(item!!.itemId){
           R.id.shareicon -> ShareSuccess()
       }
        return super.onOptionsItemSelected(item)
    }
}
