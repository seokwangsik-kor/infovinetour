package com.infovine.tour.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.adapter.CityListAdapter
import com.infovine.tour.adapter.ThemeListAdapter
import com.infovine.tour.adapter.TypeAListAdapter1
import com.infovine.tour.adapter.TypeAListAdapter2
import com.infovine.tour.data.EstimateOptionCategory
import com.infovine.tour.data.EstimateTypeA
import com.infovine.tour.data.ThemeList
import com.infovine.tour.fragment.CitySelAlertFragment
import com.infovine.tour.fragment.CitySelectFragment
import com.infovine.tour.fragment.EstimateCloseFragment
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.BaseUtil.startCity
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import java.text.DecimalFormat

class GlobalEstimateActivity : BaseActivity(), CitySelAlertFragment.CityAlertCallBackListener, CitySelectFragment.CitySelectCallBackListener, EstimateCloseFragment.EstimateCloseAlertCallBackListener {

    var Alert_EstimateClose: EstimateCloseFragment? = null
    var City_Self_Select: CitySelectFragment? = null
    var city_self : LinearLayout? = null
    var Alert_City: CitySelAlertFragment? = null
    var topBackbtn: ImageView? = null
    var themeListView: RecyclerView? = null
    var cityListView: RecyclerView? = null
    var themeList = ArrayList<ThemeList>()
    var cityList = ArrayList<String>()
    var mAdapter: ThemeListAdapter? = null
    var cityAdapter: CityListAdapter? = null
    var money_edit:EditText? = null
    var money_total:TextView? = null
    var adult_minus:ImageView? = null
    var adult_plus:ImageView? = null
    var child_minus:AppCompatButton? = null
    var child_plus:AppCompatButton? = null
    var baby_minus:AppCompatButton? = null
    var baby_plus:AppCompatButton? = null
    var adult_cnt_txt:TextView? = null
    var child_cnt_txt:TextView? = null
    var baby_cnt_txt:TextView? = null
    var confirmBtn:FrameLayout? = null
    var confirmBtnCheck:CheckBox? = null
    var confirmBtnTxt:TextView? = null
    var dateBtn:LinearLayout? = null
    var dateTxt:TextView? = null
    var won_img: TextView? = null

    //02번
    var areaSelect:FrameLayout? = null
    var areaLayer:LinearLayout? = null
    var areaImg:ImageView? = null
    var areaDest:TextView? = null
    var areaDestSub:TextView? = null
    var areaDestEdit:LinearLayout? = null

    private val decimalFormat = DecimalFormat("#,###")
    private var result: String = ""

    //견적서 선택값
    var memCd = 0 //회원 Cd
    var themeCdList = "" //테마 리스트
    var destCd = 0 //목적지 Cd
    var cityName = ""//여행지
    var money = 0 //금액
    var regMemo = ""
    var business = 0
    var mileage = 0
    var dateGb = 0 //여행 일자 구분
    var sDate = "" //견적요청 일자 시작일
    var eDate = "" //견적요청 일자 종료일
    var adultCnt = 0 //견적요청 인원 성인
    var childCnt = 0 //견적요청 인원 아동
    var babyCnt = 0 //견적요청 인원 소아
    var mcList = "" //
    var optionCodeList = ""

    var ListView1: RecyclerView? = null
    var ListView2: RecyclerView? = null
    var mAdapter1: TypeAListAdapter1? = null
    var mAdapter2: TypeAListAdapter2? = null
    var optionCd = ArrayList<EstimateOptionCategory>()
    var optionList1 = ArrayList<EstimateTypeA>()
    var optionList2 = ArrayList<EstimateTypeA>()

    var optionCD = ArrayList<Int>()

    var edit_flag = false

    var imm: InputMethodManager? = null
    val AREA_INFO = 666
    val DATE_INFO = 777

    var start_city = ""
    var start_city_txt1 : TextView? = null
    var start_city_txt : TextView? = null
    var start_city_layer : LinearLayout? = null
    var start_city_del : ImageView? = null

//    var main_srollview : ScrollView? = null
    var theme_Cd = ""

    companion object {
        var mCotext:Context? =null
        var region = ""
        var self_city_flag = false
        var city_list_flag = false
        var del_alt_flag = false
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_estimate)

        mCotext = this

//        main_srollview = findViewById(R.id.estimate_main_srollview)
        Alert_EstimateClose = EstimateCloseFragment()
        Alert_City = CitySelAlertFragment()
        topBackbtn = findViewById(R.id.global_estimate_back_btn)
        topBackbtn!!.setOnClickListener {
//            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_STARTDATE1, "")
//            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_ENDDATE1, "")
//            finish()
            onBackPressed()
        }

        areaLayer = findViewById(R.id.global_estimate02_layer)
        areaLayer!!.setOnClickListener {
            val intent = Intent(mContext, SearchActivity::class.java)
            startActivityForResult(intent, AREA_INFO)
        }
        areaImg = findViewById(R.id.global_estimate02_dest_image)
        areaDest = findViewById(R.id.global_estimate02_dest_txt)
        areaDestSub = findViewById(R.id.global_estimate02_dest_sub_txt)
        areaDestEdit = findViewById(R.id.global_estimate02_dest_edit_layer)
        areaDestEdit!!.setOnClickListener {
            val intent = Intent(mContext, SearchActivity::class.java)
            startActivityForResult(intent, AREA_INFO)
        }

        areaSelect = findViewById(R.id.global_estimate02_none_layer)
        areaSelect!!.setOnClickListener {
            val intent = Intent(mContext, SearchActivity::class.java)
            startActivityForResult(intent, AREA_INFO)
        }

        dateBtn = findViewById(R.id.global_estimate03_date_btn)
        dateBtn!!.setOnClickListener {
            val intent = Intent(mContext, DatePickersActivity::class.java)
            startActivityForResult(intent, DATE_INFO)
        }
        dateTxt = findViewById(R.id.global_estimate03_date_txt)

        won_img = findViewById(R.id.global_estimate04_edit_img)
        won_img!!.setOnClickListener {
            money_edit!!.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(money_edit, InputMethodManager.SHOW_IMPLICIT)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE )
        }
        money_total = findViewById(R.id.global_estimate04_total_text)
        money_edit = findViewById(R.id.global_estimate04_edit_text)
        money_edit!!.addTextChangedListener(watcher)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        money_edit!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (TextUtils.isEmpty(money_edit!!.text)) {
//                    money_edit!!.hint="0"
                }
                imm!!.hideSoftInputFromWindow(money_edit!!.windowToken, 0)
                money_edit!!.clearFocus()
                return@setOnKeyListener true
            }
            false
        }
        money_edit!!.setOnClickListener {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE )
        }
        money_edit!!.setOnTouchListener { v, event ->
//            money_edit!!.hint=""
            false
        }

        adult_cnt_txt = findViewById(R.id.global_estimate04_adult_txt)
        adult_minus = findViewById(R.id.global_estimate04_adult_minus)
        adult_plus = findViewById(R.id.global_estimate04_adult_plus)
        adult_minus!!.setOnClickListener{
            if(adultCnt > 0) {
                adultCnt--
                adult_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                adult_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            if (adultCnt == 0) {
                adult_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                adult_minus!!.setBackgroundResource(R.drawable.ic_minus_off)
            }
            adult_cnt_txt!!.text = adultCnt.toString()
            Confirm()
//            moneyTotal(money_edit!!.text.toString())
        }
        adult_plus!!.setOnClickListener{
            if (adultCnt==40) return@setOnClickListener
            adultCnt++
            if (adultCnt==40) {
                adult_plus!!.setBackgroundResource(R.drawable.ic_add_off)
            } else {
                adult_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                adult_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            adult_cnt_txt!!.text = adultCnt.toString()
            Confirm()
//            moneyTotal(money_edit!!.text.toString())
        }

        child_cnt_txt = findViewById(R.id.global_estimate04_child_txt)
        child_minus = findViewById(R.id.global_estimate04_child_minus)
        child_plus = findViewById(R.id.global_estimate04_child_plus)
        child_plus!!.isPressed = true
        child_minus!!.setOnClickListener{
            if(childCnt > 0) {
                childCnt--
                child_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                child_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            if (childCnt == 0) {
                child_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                child_minus!!.setBackgroundResource(R.drawable.ic_minus_off)
            }
            child_cnt_txt!!.text = childCnt.toString()
            Confirm()
//            moneyTotal(money_edit!!.text.toString())
        }
        child_plus!!.setOnClickListener{
            if (childCnt==40) return@setOnClickListener
            childCnt++
            if (childCnt==40) {
                child_plus!!.setBackgroundResource(R.drawable.ic_add_off)
            } else {
                child_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                child_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            child_cnt_txt!!.text = childCnt.toString()
            Confirm()
//            moneyTotal(money_edit!!.text.toString())
        }

        baby_cnt_txt = findViewById(R.id.global_estimate04_baby_txt)
        baby_minus = findViewById(R.id.global_estimate04_baby_minus)
        baby_plus = findViewById(R.id.global_estimate04_baby_plus)
        baby_plus!!.isPressed = true
        baby_minus!!.setOnClickListener{
            if(babyCnt > 0) {
                babyCnt--
                baby_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                baby_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            if (babyCnt == 0) {
                baby_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                baby_minus!!.setBackgroundResource(R.drawable.ic_minus_off)
            }
            baby_cnt_txt!!.text = babyCnt.toString()
            Confirm()
//            moneyTotal(money_edit!!.text.toString())
        }
        baby_plus!!.setOnClickListener{
            if (babyCnt==40) return@setOnClickListener
            babyCnt++
            if (babyCnt==40) {
                baby_plus!!.setBackgroundResource(R.drawable.ic_add_off)
            } else {
                baby_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                baby_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            baby_cnt_txt!!.text = babyCnt.toString()
            Confirm()
//            moneyTotal(money_edit!!.text.toString())
        }

        confirmBtnTxt = findViewById(R.id.global_estimate_confirm_txt)
        confirmBtnCheck = findViewById(R.id.global_estimate_confirm_check)
        confirmBtn = findViewById(R.id.global_estimate_confirm_btn)
        confirmBtn!!.setOnClickListener {
            if (confirmBtnCheck!!.isChecked) {
                val intent = Intent(mContext, EstimateConfirmActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        themeListView = findViewById(R.id.estimate01_recyclerview)
        mAdapter = ThemeListAdapter(mContext!!, themeList, itemClickInterface3)

        themeListView!!.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mContext, 3)
        themeListView!!.layoutManager = gridLayoutManager
        themeListView!!.adapter = mAdapter
//        if(edit_flag) {
//            var cd = Integer.parseInt(SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_THEMECD, theme_Cd).toString())
            ThemeData()
//        } else {
//            ThemeData(null)
//        }


        start_city_layer = findViewById(R.id.global_estimate05_start_layer)
        start_city_txt =  findViewById(R.id.global_estimate05_start_txt)
        start_city_txt!!.setOnClickListener {
            CityDelAlert()
        }
        start_city_del =  findViewById(R.id.global_estimate05_start_del)
        start_city_del!!.setOnClickListener {
            CityDelAlert()
        }
        start_city_txt1 = findViewById(R.id.global_estimate05_start_txt1)

        cityListView = findViewById(R.id.estimate05_recyclerview)
        cityAdapter = CityListAdapter(mContext!!, cityList, itemClickInterface)

        cityListView!!.setHasFixedSize(true)
        val gridLayoutManager1 = GridLayoutManager(mContext, 4)
        cityListView!!.layoutManager = gridLayoutManager1
        cityListView!!.adapter = cityAdapter
        CityData()

        city_self = findViewById(R.id.global_estimate05_self)
        city_self!!.setOnClickListener {
            City_Self_Select = CitySelectFragment()
            City_Self_Select!!.show(supportFragmentManager, "")
            self_city_flag = true
        }

        ListView1 = findViewById(R.id.global_estimate_q6_recyclerview1)
        ListView2 = findViewById(R.id.global_estimate_q6_recyclerview2)
        mAdapter1 = TypeAListAdapter1(mContext!!, optionList1, itemClickInterface1)
        mAdapter2 = TypeAListAdapter2(mContext!!, optionList2, itemClickInterface2)
        ListView1!!.setHasFixedSize(true)
        val gridLayoutManager2 = GridLayoutManager(mContext, 3)
        ListView1!!.layoutManager = gridLayoutManager2
        ListView1!!.adapter = mAdapter1

        ListView2!!.setHasFixedSize(true)
        val gridLayoutManager3 = GridLayoutManager(mContext, 4)
        ListView2!!.layoutManager = gridLayoutManager3
        ListView2!!.adapter = mAdapter2

        optionList1.clear()
        optionList2.clear()
        arr_1.clear()
        arr_2.clear()
        optionCD.clear()

        OptionCategory()
    }

    fun OptionCategory() {
        optionCd.addAll(db.getOptionCategory()!!)
        optionCd.sortBy { it.sortNo }
        for (i in 0 until optionCd.size) {
            OptionData(optionCd[i].lcCd, i)
        }
    }

    fun OptionData(cd : Int, index:Int) {
        if(index==0) {
            optionList1.addAll(db.getOptionDetail(cd)!!)
        }
        if(index==1) {
            optionList2.addAll(db.getOptionDetail(cd)!!)
        }
        mAdapter1!!.singleSelNum()
        mAdapter1!!.listchangeSingleSelect(SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_OPTION_AIR_SINGLE_CD, 0))
        mAdapter2!!.singleSelNum()
        mAdapter2!!.listchangeSingleSelect(SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_OPTION_HOTEL_SINGLE_CD, 0))
        if(index==0) mAdapter1!!.notifyDataSetChanged()
        if(index==1) mAdapter2!!.notifyDataSetChanged()
    }

//    fun ThemeData(cd: Int?) {
//        themeList.addAll(db.getTheme()!!)
//        if (cd != null) {
//            mAdapter!!.listchange(cd!!)
//        }
//        mAdapter!!.notifyDataSetChanged()
//        Confirm()
//    }
    fun ThemeData() {
        themeList.addAll(db.getTheme()!!)
        mAdapter!!.notifyDataSetChanged()
        Confirm()
    }

    fun CityData() {
        cityList.clear()
        cityList.addAll(startCity())
        cityAdapter!!.notifyDataSetChanged()
    }

    fun CityChangeAlert(before_city:String, after_city:String, flag:Boolean) {
        del_alt_flag = false
        val bundle = Bundle()
        bundle.putString("title", "출발지는 한 곳만 지정할 수 있어요 :)")
        bundle.putString("before_city", before_city)
        bundle.putString("after_city", after_city)
        bundle.putBoolean("flag", flag)
        Alert_City!!.arguments = bundle
        Alert_City!!.show(supportFragmentManager, "")
    }

    fun CityDelAlert() {
        del_alt_flag = true
        val bundle = Bundle()
        bundle.putString("title", "출발지를 삭제 하시겠습니까?")
        bundle.putString("body", "출발지 삭제 후\n다시 지정할 수 있습니다.")
//        bundle.putString("city", start_city)
        Alert_City!!.arguments = bundle
        Alert_City!!.show(supportFragmentManager, "")
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != result){
                result = decimalFormat.format(charSequence.toString().replace(",","").toDouble())
                money_edit!!.setText(result)
                money_edit!!.setSelection(result.length)

                moneyTotal(charSequence.toString())
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            Confirm()
            if(TextUtils.isEmpty(p0)) {
                moneyTotal("0")
            }
        }
    }

    fun moneyTotal(money: String) {
//        var cnt = adultCnt + childCnt + babyCnt
//        if(cnt == 0) {
            money_total!!.text = decimalFormat.format(money.replace(",","").toDouble())
//        } else {
//            if (cnt > 1) {
//                if(!TextUtils.isEmpty(money)) {
//                    money_total!!.text = decimalFormat.format(money.replace(",","").toDouble() * cnt)
//                }
//            } else {
//                if(!TextUtils.isEmpty(money)) {
//                    money_total!!.text = decimalFormat.format(money.replace(",","").toDouble())
//                }
//            }
//        }
    }

//    var date = ""
    fun DateTypeCheck(calData:String?) {
        dateGb=0

        if (!TextUtils.isEmpty(calData)) {
            dateTxt!!.text = calData
            dateGb = 4
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_DATE_EDIT, calData)
        }
        SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_DATETYPE, dateGb)
        Confirm()
    }

    fun AreaData(name:String, name_sub:String, cd:Int, img:String) {
        areaSelect!!.isVisible = false
        areaLayer!!.isVisible = true
        areaDest!!.text =  name
        areaDestSub!!.text = name_sub
//        Glide.with(mContext!!).load(Const.IMAGE_DOMAIN+img).into(areaImg!!)
        Glide.with(mContext!!).load(img).into(areaImg!!)

        if (TextUtils.isEmpty(name)) {
//            areaTxt!!.text = "원하시는 나라 또는 도시를 선택 해주세요."
        } else {
//            if (!TextUtils.isEmpty(img)) {
//                Glide.with(mContext!!).load(Const.IMAGE_DOMAIN+img).into(areaImg!!)
//            }
            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_DEST, cd)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_DEST_NAME, name)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_DEST_NAME_SUB, name_sub)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_DEST_IMG, img)

//            areaTxt!!.text = name
//            heart2!!.isVisible = true
        }
//        ConfirmBtn_Visible()
        Confirm()
    }

    //견적신청서 작성 완료 버튼 활성
    fun Confirm() {

        confirmBtnCheck!!.isChecked = false
        confirmBtnTxt!!.setTextColor((Color.parseColor("#bebebe")))

        //Q1
        if (TextUtils.isEmpty(theme_Cd)) return
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_THEMECD, theme_Cd)
        //Q2
        if (TextUtils.isEmpty(areaDest!!.text)) return
        //Q3
        if (dateGb == 0) return
//        if (TextUtils.isEmpty(dateTxt!!.text)) return
        //Q4
//        if (money_edit!!.text.isEmpty()) return
        var won = money_edit!!.text.toString()
        if (TextUtils.isEmpty(won)) return
        if (won!="0") {
            var m = won.replace(",", "").toInt()
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_MONEY_WON, won)
            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_MONEY, m)
        } else return
//        if(adultCnt == 0) return
        if(adultCnt + childCnt + babyCnt == 0) return
        SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_ADULT_CNT, adultCnt)
        SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_CHILD_CNT, childCnt)
        SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_BABY_CNT, babyCnt)
        //Q5
        var start_area = start_city_txt!!.text.toString()
        if (TextUtils.isEmpty(start_area)) return
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_START_AREA, start_area)
        //Q6
        BaseUtil.LogErr("Q6_OptionListSave()" + Q6_OptionListSave())
//        if(edit_flag) {
            if (!Q6_OptionListSave()) return
//        } else {
//            Q6_OptionListSave()
//        }
        confirmBtnCheck!!.isChecked = true
        confirmBtnTxt!!.setTextColor((Color.parseColor("#ffffff")))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DATE_INFO) {
            if(data==null) return
            var date = data.getStringExtra("date")!!
            DateTypeCheck(date)
        }

        if (requestCode == AREA_INFO) {
            if(data==null) return
            AreaData(data.getStringExtra("area_name")!!, data.getStringExtra("area_name_sub")!!,
                data.getIntExtra("area_cd", 0), data.getStringExtra("area_img")!!)
        }
    }

    val oBject3 = object :ThemeListAdapter.OnClickInterface{
        override fun onClick(cd:String) {
            theme_Cd = cd
            Confirm()
        }
    }
    val itemClickInterface3: ThemeListAdapter.OnClickInterface = oBject3

    val oBject2 = object :TypeAListAdapter2.OnClickInterface{
        override fun onClick(name:String, cd: Int) {
            hotel_setText(name, cd)
            Confirm()
        }
    }
    val itemClickInterface2: TypeAListAdapter2.OnClickInterface = oBject2

    val oBject1 = object :TypeAListAdapter1.OnClickInterface{
        override fun onClick(name:String, cd: Int) {
            air_setText(name, cd)
            Confirm()
        }
    }
    val itemClickInterface1: TypeAListAdapter1.OnClickInterface = oBject1

//    var list_position = -1
    var onClickFlag = false
    var selfClickFlag = false
    val oBject = object :CityListAdapter.OnClickInterface{
        override fun onClick(city: String, p:Int) {

            if(selfClickFlag) {//이전에 셀프선택이 있을경우
                CityChangeAlert(start_city_txt!!.text.toString(), city, false)
                self_city_flag = false
                SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, -1)
            } else {
                city_list_flag = true
//                onClickFlag = true
                start_city_txt!!.text = city
                start_city_layer!!.isVisible = true
                start_city_txt1!!.text = "공항에서 출발"
//                heart5!!.isVisible = true
                SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, p)
            }
//            if(cityself_end_flag) return
//            list_position = p
            Confirm()
        }
    }
    val itemClickInterface: CityListAdapter.OnClickInterface = oBject

//    var city_end_flag = false
//    var cityself_end_flag = false
    override fun onAlertCallBack(cityname: String) {

        if(del_alt_flag) {//출발지 삭제
            if (cityname == "notDel")  return
            start_city_layer!!.isVisible = false
            start_city_txt!!.text = ""
            cityAdapter!!.listchange(-1)
            cityAdapter!!.notifyDataSetChanged()
            self_city_flag = false
            city_list_flag = false
            selfClickFlag = false
            onClickFlag = false
            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, -1)
        } else {//출발지 변경

            //셀프로 변경완료 후 셀프 플래그 변경 도시 어댑터 초기화
            if(self_city_flag) {
                if(!TextUtils.isEmpty(cityname)) {
                    cityAdapter!!.listchange(-1)
                    cityAdapter!!.notifyDataSetChanged()
                    selfClickFlag = true
                    city_list_flag = false
                    SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, -1)
                }
            } else {
                if(TextUtils.isEmpty(cityname)) {
                    cityAdapter!!.listchange(-1)
                    cityAdapter!!.notifyDataSetChanged()
                    SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, -1)
                } else {
                    selfClickFlag = false
                    city_list_flag = true
                }
            }
            if(!TextUtils.isEmpty(cityname)) {
                start_city_txt!!.text = cityname
                if(selfClickFlag) {
                    start_city_txt1!!.text = "에서 출발"
                } else {
                    start_city_txt1!!.text = "공항에서 출발"
                }
            }
        }
        Confirm()
//        heart5_Visible()
    }


    override fun onCitySelectCallBack(Region: String, Cityname: String) {
        region = Region
        start_city = Cityname
        if (self_city_flag) {
            if(city_list_flag) {
                CityChangeAlert(start_city_txt!!.text.toString(), region + " " + start_city, true)
            } else {
                start_city_layer!!.isVisible = true
                start_city_txt!!.text = region + " " + start_city
                start_city_txt1!!.text = "에서 출발"
                selfClickFlag = true
                SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, -1)
            }
        }
//        heart5_Visible()
        Confirm()
    }

    var arr_1 = ArrayList<String>()
    var arr_2 = ArrayList<String>()
    var air_singleName = ""
    var hotel_singleName = ""
    fun air_setText(name:String, cd: Int) {
        if(TypeAListAdapter1.air_singleSelectNum == cd) {
            air_singleName = name
            arr_1.clear()
            if (!TypeAListAdapter1.single_flag) return //상관없음 선택 해제 했을때
        } else {
            for (i in 0 until arr_1.size) {
                if(air_singleName == arr_1[i]) {
                    arr_1.removeAt(i)//상관없음 리스트에서 삭제
                }
            }
        }

        var cnt = arr_1.size
        if (cnt==0) {
            arr_1.add(name)
        } else {
            var flag = true
            var index = 0
            for (i in 0 until arr_1.size) {
                if(arr_1[i] == name) {
                    flag = false
                    index = i
                }
            }
            if (flag) {
                arr_1.add(name)
            } else {
                arr_1.removeAt(index)
            }
        }
        BaseUtil.LogErr("arr_1>>"+arr_1.toString())
    }

    fun hotel_setText(name:String, cd: Int) {
        if(TypeAListAdapter2.hotel_singleSelectNum == cd) {
            hotel_singleName = name
            arr_2.clear()
            if (!TypeAListAdapter2.single_flag) return
        } else {
            for (i in 0 until arr_2.size) {
                if(hotel_singleName == arr_2[i]) {
                    arr_2.removeAt(i)
                }
            }
        }

        var cnt = arr_2.size
        if (cnt==0) {
            arr_2.add(name)
        } else {
            var flag = true
            var index = 0
            for (i in 0 until arr_2.size) {
                if(arr_2[i] == name) {
                    flag = false
                    index = i
                }
            }
            if (flag) {
                arr_2.add(name)
            } else {
                arr_2.removeAt(index)
            }
        }
        BaseUtil.LogErr("arr_2>>"+arr_2.toString())
    }

    fun Q6_OptionListSave():Boolean {
        var bool = true
        if (arr_1.size==0) return false
        if (arr_2.size==0) return false
        var index1 = 0
//        optionList1.clear()
//        optionList2.clear()
//        optionCD.clear()

        var optionCD_air = ArrayList<Int>()
        var optionCD_hotel = ArrayList<Int>()
        for (i in 0 until arr_1.size) {
            index1 = i
            for (i in 0 until optionList1.size) {
                if(arr_1[index1] == optionList1[i].mcNm) {
                    optionCD_air.add(optionList1[i].mcCd)
                }
            }
        }

        var index2 = 0
        for (i in 0 until arr_2.size) {
            index2 = i
            for (i in 0 until optionList2.size) {
                if(arr_2[index2] == optionList2[i].mcNm) {
                    optionCD_hotel.add(optionList2[i].mcCd)
                }
            }
        }
        optionCD_air.sort()
        optionCD_hotel.sort()
//        optionCD.sort()

        var str1 = ""
        var str2 = ""
        var optionCodeList = ""
        for (i in optionCD_air) {
            optionCodeList += "$i,"
            str1 += "$i,"
        }
        for (i in optionCD_hotel) {
            optionCodeList += "$i,"
            str2 += "$i,"
        }
        var aa = optionCodeList.length
        optionCodeList = optionCodeList.substring(0, aa-1)
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_OPTION, optionCodeList)

        val str: String = java.lang.String.join(", ", arr_1+arr_2)
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_OPTION_NAME_LIST, str)

        //견적수정용데이터
        var s1 = str1.length
        str1 = str1.substring(0, s1-1)
        var s2 = str2.length
        str2 = str2.substring(0, s2-1)
//        var str1: String = java.lang.String.join(",", optionCD_air.toString())
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_OPTION_AIR, str1)
//        var str2: String = java.lang.String.join(",", optionCD_hotel.toString())
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_OPTION_HOTEL, str2)

        return bool
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        Alert_EstimateClose!!.show(supportFragmentManager, "")
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onResume() {
        super.onResume()
        edit_flag = SharedPrefsUtils.getBooleanPreference(mContext!!, Const.ESTIMATE_EDIT, false)
        if(edit_flag) {
            //Q1
            var cd = Integer.parseInt(SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_THEMECD))
            if (cd != null) {
                mAdapter!!.listchange(cd!!)
            }
            mAdapter!!.notifyDataSetChanged()
            //Q2
            var dest_cd = SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_DEST, 0)
            var name = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_DEST_NAME)
            var name_sub = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_DEST_NAME_SUB)
            var img = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_DEST_IMG)
            AreaData(name, name_sub, dest_cd, img)
            //Q3
            DateTypeCheck(SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_DATE_EDIT))
            //Q4
            money_edit!!.setText(SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_MONEY_WON))
            adultCnt = SharedPrefsUtils.getIntegerPreference(mContext, Const.ESTIMATE_ADULT_CNT, 0)
            if(adultCnt > 0) {
                adult_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                adult_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            adult_cnt_txt!!.text = adultCnt.toString()
            childCnt = SharedPrefsUtils.getIntegerPreference(mContext, Const.ESTIMATE_CHILD_CNT, 0)
            if(childCnt > 0) {
                child_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                child_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            child_cnt_txt!!.text = childCnt.toString()
            babyCnt = SharedPrefsUtils.getIntegerPreference(mContext, Const.ESTIMATE_BABY_CNT, 0)
            if(babyCnt > 0) {
                baby_plus!!.setBackgroundResource(R.drawable.ic_add_on)
                baby_minus!!.setBackgroundResource(R.drawable.ic_minus_on)
            }
            baby_cnt_txt!!.text = babyCnt.toString()
            //Q5
            start_city_layer!!.isVisible = true
            start_city_txt!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_START_AREA)
            var position = SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_START_AREA_CD, 0)
            if(position != -1) {//직접선택
                cityAdapter!!.listchange(position)
                cityAdapter!!.notifyDataSetChanged()
            }
            //Q6
            var air_cd = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_OPTION_AIR)
            var optionCD_air = ArrayList<Int>()
            var aa = air_cd.split(",")
            var single_sel_flag = false
            for (i in aa) {
                optionCD_air.add(Integer.parseInt(i))
                if (Integer.parseInt(i) == SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_OPTION_AIR_SINGLE_CD, 0)) {
                    single_sel_flag = true
                }
            }
            if(single_sel_flag) {
                mAdapter1!!.listchangeSingleSelect(optionCD_air[0])//상관없음 선택값
                mAdapter1!!.notifyDataSetChanged()
            } else {
                TypeAListAdapter1.updateCD.clear()
                for (i in 0 until optionCD_air.size) {
                    mAdapter1!!.setUpdate(optionCD_air[i])
                }
                mAdapter1!!.notifyDataSetChanged()
            }

            var hotel_cd = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_OPTION_HOTEL)
            var optionCD_hotel = ArrayList<Int>()
            var aaa = hotel_cd.split(",")
            var single_sel_flag1 = false
            for (i in aaa) {
                optionCD_hotel.add(Integer.parseInt(i))
                if (Integer.parseInt(i) == SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_OPTION_HOTEL_SINGLE_CD, 0)) {
                    single_sel_flag1 = true
                }
            }
            if(single_sel_flag1) {
                mAdapter2!!.listchangeSingleSelect(optionCD_hotel[0])//상관없음 선택값
                mAdapter2!!.notifyDataSetChanged()
            } else {
                TypeAListAdapter2.updateCD.clear()
                for (i in 0 until optionCD_hotel.size) {
                    mAdapter2!!.setUpdate(optionCD_hotel[i])
                }
                mAdapter2!!.notifyDataSetChanged()
            }
//            BaseUtil.LogErr("optionCD_hotel>>"+optionCD_hotel.toString())

//            BaseUtil.LogErr("optionCD_air>>"+optionCD_air.toString())
//            if (optionCD_air.all { it == 99}) {
//
//            }

//            if (cd != null) {
//                mAdapter!!.listchange(cd!!)
//            }
//            mAdapter!!.notifyDataSetChanged()
            Confirm()
        }
    }

    override fun onEstimateClosCallBack() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_STARTDATE1, "")
        SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_ENDDATE1, "")
    }
}
