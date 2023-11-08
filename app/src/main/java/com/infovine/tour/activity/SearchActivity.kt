package com.infovine.tour.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.infovine.tour.R
import com.infovine.tour.adapter.SearchAdapter
import com.infovine.tour.adapter.SearchDestListAdapter
import com.infovine.tour.adapter.SearchSaveListAdapter
import com.infovine.tour.data.AreaList
import com.infovine.tour.data.DestList
import com.infovine.tour.data.EstimateTypeA
import com.infovine.tour.fragment.DestListFragment
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import java.util.regex.Pattern

class SearchActivity : BaseActivity(), DestListFragment.CallBackListener {

    var backBtn:ImageView? = null
    var tab_layer: TabLayout? = null
    var search_viewpager: ViewPager2? = null
    var AreaList = ArrayList<AreaList>()
    var DestList = ArrayList<DestList>()
    var searchSaveList = ArrayList<String>()
    var searchAdapter: SearchAdapter? = null

    //    var searchClose: ImageView? = null
    var searchBtn: ImageView? = null
    var searchTxt: EditText? = null
    var searchlistView: RecyclerView? = null
    var searchlistAdapter: SearchDestListAdapter? = null
    var searchDestList = ArrayList<DestList>()
    var mLayoutManager: LinearLayoutManager? = null
    var mLayoutManager1: LinearLayoutManager? = null
    val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    var imm: InputMethodManager? = null

    var intdp: Int? = 0

    //검색어 레이어
    var search_layer: LinearLayout? = null
    var search_save_btn: TextView? = null
    var search_save_clear: TextView? = null
    var search_save_listView: RecyclerView? = null
    var search_save_adapter: SearchSaveListAdapter? = null
    var search_edit_layer: LinearLayout? = null

    //검색 목록
    var search_recyclerview_layer : FrameLayout? = null

    var tab_word:LinearLayout? = null
    var tab_korea:LinearLayout? = null
    var tab_word_txt:TextView? = null
    var tab_korea_txt:TextView? = null
    var tab_word_img:ImageView? = null
    var tab_korea_img:ImageView? = null
    var tab_word_img1:ImageView? = null
    var tab_korea_img1:ImageView? = null

    companion object {
        var DestListDumy = ArrayList<DestList>()

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backBtn = findViewById(R.id.search_back_btn)
        backBtn!!.setOnClickListener {
//            finish()
            onBackPressed()
        }
        tab_layer = findViewById(R.id.search_tap)
        search_viewpager = findViewById(R.id.search_viewpager)
        search_viewpager!!.offscreenPageLimit = 1

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        intdp = metrics.densityDpi / 10

        AreaData("word")//최초 해외여행 전체 노출

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        searchClose = findViewById(R.id.search_close)
//        searchClose!!.setOnClickListener {
//            imm!!.hideSoftInputFromWindow(searchTxt!!.windowToken, 0)
//            searchlistView!!.isVisible = false
//            searchTxt!!.clearFocus()
//            searchTxt!!.text.clear()
//        }

        tab_korea_txt = findViewById(R.id.search_korea_txt)
        tab_word_txt = findViewById(R.id.search_word_txt)
        tab_korea_img = findViewById(R.id.search_korea_img)
        tab_word_img = findViewById(R.id.search_word_img)
        tab_korea_img1 = findViewById(R.id.search_korea_img1)
        tab_word_img1 = findViewById(R.id.search_word_img1)
        tab_word = findViewById(R.id.search_word)
        tab_word!!.setOnClickListener {
            tab_word_txt!!.setTextColor(Color.parseColor("#171717"))
            tab_korea_txt!!.setTextColor(Color.parseColor("#bebebe"))
            tab_word_img!!.isVisible = true
            tab_word_img1!!.isVisible = false
            tab_korea_img!!.isVisible = false
            tab_korea_img1!!.isVisible = true
            AreaData("word")
        }
        tab_korea = findViewById(R.id.search_korea)
        tab_korea!!.setOnClickListener {
            tab_korea_txt!!.setTextColor(Color.parseColor("#171717"))
            tab_word_txt!!.setTextColor(Color.parseColor("#bebebe"))
            tab_korea_img!!.isVisible = true
            tab_korea_img1!!.isVisible = false
            tab_word_img!!.isVisible = false
            tab_word_img1!!.isVisible = true
            AreaData("home")
        }

        search_edit_layer = findViewById(R.id.search_edit_layer)
        search_edit_layer!!.setOnClickListener {
            if (!search_layer!!.isVisible) {
                backpress_flag = false
                search_layer!!.isVisible = true
                SearchSaveList()
            }
        }

        search_recyclerview_layer = findViewById(R.id.search_recyclerview_layer)
        search_layer = findViewById(R.id.search_layer)
        search_save_btn = findViewById(R.id.search_save_btn)
        if (!SharedPrefsUtils.getBooleanPreference(mContext!!, Const.SEARCH_SAVE_STATUS, true)) {
            search_save_btn!!.text = "저장기능 켜기  "
        }
        search_save_btn!!.setOnClickListener {
            if (SharedPrefsUtils.getBooleanPreference(mContext!!, Const.SEARCH_SAVE_STATUS, true)) {
                SharedPrefsUtils.setBooleanPreference(mContext!!, Const.SEARCH_SAVE_STATUS,false)
                search_save_btn!!.text = "저장기능 켜기  "
            } else {
                SharedPrefsUtils.setBooleanPreference(mContext!!, Const.SEARCH_SAVE_STATUS, true)
                search_save_btn!!.text = "저장기능 끄기  "
            }
        }

        search_save_clear = findViewById(R.id.search_save_clear)
        search_save_clear!!.setOnClickListener {
            //검색목록삭제
            db.delSearchSaveList("")
            SearchSaveList()
        }
//        searchBtn = findViewById(R.id.search_btn)
//        searchBtn!!.setOnClickListener {
//            searchlistView!!.isVisible = true
//        }

        searchTxt = findViewById(R.id.search_edit_text)
        searchTxt!!.setOnTouchListener { v, event ->
            if (!search_layer!!.isVisible) {
                backpress_flag = false
                search_layer!!.isVisible = true
                SearchSaveList()
            }
            false
        }
//        searchTxt!!.setOnClickListener {
//            if (!search_layer!!.isVisible) {
//                search_layer!!.isVisible = true
//            }
//        }
        searchTxt!!.addTextChangedListener(watcher)
        searchTxt!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                var data = searchTxt!!.text.toString()
//                if (TextUtils.isEmpty(data)) return@setOnKeyListener true
//                SearchData(data)
                imm!!.hideSoftInputFromWindow(searchTxt!!.windowToken, 0)
//                search_layer!!.isVisible = false
                return@setOnKeyListener true
            }
            false
        }

//        var filterAlphaNumSpace = InputFilter { source, start, end, dest, dstart, dend ->
//            val ps = Pattern.compile("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|@\\u318D]*")
//            if (!ps.matcher(source).matches()) {
//                return@InputFilter ""
//            } else source
//        }
//        searchTxt!!.filters = arrayOf(filterAlphaNumSpace)

        searchlistView = findViewById(R.id.search_recyclerview)
        searchlistAdapter = SearchDestListAdapter(mContext!!, searchDestList, itemClickInterface)
        searchlistView!!.adapter = searchlistAdapter


        search_save_listView = findViewById(R.id.search_save_recyclerview)
        search_save_adapter = SearchSaveListAdapter(mContext!!, searchSaveList, itemClickInterface1)
        search_save_listView!!.adapter = search_save_adapter
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!backpress_flag) {
                SearchData(charSequence!!.toString())
                search_layer!!.isVisible = false
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    fun SearchSaveList() {
        var result: ArrayList<String>? = db.getSearchSaveList()!!
        searchSaveList.clear()
        if (result!!.size == 0) {
            search_save_listView!!.isVisible = false
        } else {
            search_save_listView!!.isVisible = true
            searchSaveList.addAll(result)
            search_save_adapter!!.notifyDataSetChanged()
        }
    }

    fun SearchData(destName: String) {
        search_recyclerview_layer!!.isVisible = true
        searchDestList.clear()
        var result: ArrayList<DestList> = db.getDest(destName)!!
        if (result.size == 0) {
            searchlistView!!.isVisible = false
        } else {
//            search_layer!!.isVisible = false
            searchlistView!!.isVisible = true
            searchDestList.addAll(result)
            searchlistAdapter!!.notifyDataSetChanged()
        }
        BaseUtil.LogErr(searchDestList.toString())
    }

    val oBject1 = object : SearchSaveListAdapter.OnClickInterface {
        override fun onClick(name: String, boolean: Boolean) {
            if (boolean) {//true면 해당 리스트 삭제
                db.delSearchSaveList(name)
                SearchSaveList()
            } else {
                search_layer!!.isVisible = false
                search_save_listView!!.isVisible = false
                SearchData(name)
            }
        }
    }
    val itemClickInterface1: SearchSaveListAdapter.OnClickInterface = oBject1

    val oBject = object : SearchDestListAdapter.OnClickInterface {
        override fun onClick(name: String, name_sub: String, cd: Int, imgUrl: String) {
            resultData(name, name_sub, cd, imgUrl)
//            db.insertSearchSave(name)
        }
    }
    val itemClickInterface: SearchDestListAdapter.OnClickInterface = oBject

    fun AreaData(area:String) {
        AreaList.clear()
        DestList.clear()
        DestListDumy.clear()

        //임시 패치
        if(area=="word") {
            AreaList.add(0, AreaList(0, "추천", "", 0, ""))
        }
//        AreaList.addAll(db.getArea()!!)
        AreaList.addAll(db.getArea(area)!!)
//        DestList.addAll(db.getDest(null)!!)
        DestList.addAll(db.getDest(area)!!)
        DestListDumy.addAll(db.getDest(null)!!)

        searchAdapter = SearchAdapter(this, AreaList)
        search_viewpager!!.adapter = searchAdapter
        searchAdapter!!.notifyDataSetChanged()

        try {
            TabLayoutMediator(tab_layer!!, search_viewpager!!) { tab, position ->
                tab.text = AreaList[position].areaNm
            }.attach()
        } catch (e: Exception) {

        }
        setTabItemMargin(tab_layer!!, intdp!!)
    }

    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int) {
        for (i in 0..AreaList.size) {
            val tabs = tabLayout.getChildAt(0) as ViewGroup
            for (i in 0 until tabs.childCount) {
                val tab = tabs.getChildAt(i)
                val lp = tab.layoutParams as LinearLayout.LayoutParams
                if (i == 0) {
                    lp.leftMargin = marginEnd
                } else {
                    lp.leftMargin = marginEnd / 4
                }

                if (i == AreaList.size - 1) {
                    lp.rightMargin = marginEnd
                } else {
                    lp.rightMargin = marginEnd / 4
                }
                lp.topMargin = marginEnd
                lp.bottomMargin = marginEnd
                tab.layoutParams = lp
                tabLayout.requestLayout()
            }
        }
    }

    override fun onDestFragmentCallBack(name: String, name_sub:String, cd: Int, imgUrl: String) {
        resultData(name, name_sub, cd, imgUrl)
    }

    fun resultData(name: String, name_sub: String, cd: Int, imgUrl: String) {
        if (SharedPrefsUtils.getBooleanPreference(mContext!!, Const.SEARCH_SAVE_STATUS, true)) {
            db.insertSearchSave(name)
        }
        val intent = Intent()
        intent.putExtra("area_name", name)
        intent.putExtra("area_name_sub", name_sub)
        intent.putExtra("area_cd", cd)
        intent.putExtra("area_img", imgUrl)
        setResult(RESULT_OK, intent)
        finish()
    }

    var backpress_flag = false
    override fun onBackPressed() {
//        if(search_layer!!.isVisible || search_recyclerview_layer!!.isVisible || searchlistView!!.isVisible) {
        if(search_layer!!.isVisible ||search_recyclerview_layer!!.isVisible) {
            search_layer!!.isVisible = false
            search_recyclerview_layer!!.isVisible = false
//            searchlistView!!.isVisible = false
            backpress_flag = true
            searchTxt!!.text = null
            searchTxt!!.clearFocus()
            return
        }
        finish()
    }
}
