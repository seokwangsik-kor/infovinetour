package com.infovine.tour.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.infovine.tour.R

class MapFragment : Fragment(), OnMapReadyCallback {

    var mContext: Context? = null
    var mapView: MapView? = null
    private var callBackListener: MapCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is MapCallBackListener) callBackListener = mContext as MapCallBackListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = mView.findViewById(R.id.map_view)
        mapView!!.getMapAsync(this)

        return mView
    }

    companion object {
    }

    override fun onMapReady(p0: GoogleMap) {

    }

    internal interface MapCallBackListener {
        fun onMapCallBack()
    }
}