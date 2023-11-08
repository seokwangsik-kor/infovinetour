package com.infovine.tour.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import androidx.compose.material3.Text
import com.infovine.tour.data.*

class TourDB(context: Context?, name:String?, factory: SQLiteDatabase.CursorFactory?, version: Int?) : SQLiteOpenHelper(context, name, factory,version!!) {

    //테이블명
    val TB_THEME_LIST = "TB_THEME_LIST"
    val TB_OPTION_CATEGORY = "TB_OPTION_CATEGORY"
    val TB_OPTION_DETAIL = "TB_OPTION_DETAIL"
    val TB_AREA = "TB_AREA"
    val TB_AREA_DETAIL = "TB_AREA_DETAIL"
    val TB_DEST = "TB_DEST"
    val TB_SEARCH = "TB_SEARCH"
    val SEARCH_SAVE_LIST = "SEARCH_SAVE_LIST"

    override fun onCreate(db: SQLiteDatabase?) {

        //테마리스트
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_THEME_LIST(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, themeCd INTEGER, themeNm TEXT, themeImgUrl TEXT, sortNo INTEGER);")
        //옵션 대분류
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_OPTION_CATEGORY(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, lcCd INTEGER, lcNm TEXT, lcImgUrl TEXT, sortNo INTEGER, memo TEXT);")
        //옵션 중분류
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_OPTION_DETAIL(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, mcCd INTEGER, lcCd INTEGER, mcNm TEXT, mcImgUrl TEXT, sortNo INTEGER, memo TEXT);")
        //여행지역
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_AREA(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, areaCd INTEGER, areaNm TEXT, areaImgUrl TEXT, areaGb INTEGER, memo TEXT);")
        //여행지역 상세
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_AREA_DETAIL(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, areaCd INTEGER, areaNm TEXT, areaImgUrl TEXT, areaGb INTEGER, memo TEXT);")
        //여행목적지
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_DEST(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, destCd INTEGER, destNm TEXT, destImgUrl TEXT, destDesc TEXT, area_CD INTEGER, destRequestCnt INTEGER, memo TEXT, areaGb INTEGER, recommend INTEGER);")
        //목적지검색
        db!!.execSQL("CREATE TABLE IF NOT EXISTS TB_SEARCH(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, num INTEGER, cd INTEGER, nm TEXT, tUrl TEXT, gb INTEGER, tableCd INTEGER, txt TEXT);")
        //여행지검색목록
        db!!.execSQL("CREATE TABLE IF NOT EXISTS SEARCH_SAVE_LIST(SEQ INTEGER PRIMARY KEY AUTOINCREMENT, searchName TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db!!.execSQL("DROP TABLE IF EXISTS $table;")
        onCreate(db)
    }

    fun insertTheme(data:ThemeList) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("themeCd", data.themeCd)
        contentValues.put("themeNm", data.themeNm)
        contentValues.put("themeImgUrl", data.themeImgUrl)
        contentValues.put("sortNo", data.sortNo)
        db.insert(TB_THEME_LIST, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getTheme(): ArrayList<ThemeList>? {
        val db = readableDatabase
        var result = ArrayList<ThemeList>()
        val cursor = db.rawQuery("SELECT * FROM TB_THEME_LIST", null) ?: return null
        if (cursor.moveToFirst()) {
            do {
                var themeCd = cursor.getInt(cursor.getColumnIndex("themeCd"))
                var themeNm = cursor.getString(cursor.getColumnIndex("themeNm"))
                var themeImgUrl = cursor.getString(cursor.getColumnIndex("themeImgUrl"))
                var sortNo = cursor.getInt(cursor.getColumnIndex("sortNo"))
                result.add(ThemeList(themeCd, themeNm, themeImgUrl, sortNo))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun insertOptionCategory(data: EstimateOptionCategory) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("lcCd", data.lcCd)
        contentValues.put("lcNm", data.lcNm)
        contentValues.put("lcImgUrl", data.lcImgUrl)
        contentValues.put("sortNo", data.sortNo)
        contentValues.put("memo", data.memo)
        db.insert(TB_OPTION_CATEGORY, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getOptionCategory(): ArrayList<EstimateOptionCategory>? {
        val db = readableDatabase
        var result = ArrayList<EstimateOptionCategory>()
        val cursor = db.rawQuery("SELECT * FROM TB_OPTION_CATEGORY", null) ?: return null
        if (cursor.moveToFirst()) {
            do {
                var lcCd = cursor.getInt(cursor.getColumnIndex("lcCd"))
                var lcNm = cursor.getString(cursor.getColumnIndex("lcNm"))
                var lcImgUrl = cursor.getString(cursor.getColumnIndex("lcImgUrl"))
                var sortNo = cursor.getInt(cursor.getColumnIndex("sortNo"))
                var memo = cursor.getString(cursor.getColumnIndex("memo"))
                result.add(EstimateOptionCategory(lcCd, lcNm, lcImgUrl, sortNo, memo))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun insertOptionDetail(data: EstimateTypeA) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("mcCd", data.mcCd)
        contentValues.put("lcCd", data.lcCd)
        contentValues.put("mcNm", data.mcNm)
        contentValues.put("mcImgUrl", data.sortNo)
        contentValues.put("sortNo", data.sortNo)
        contentValues.put("memo", data.memo)
        db.insert(TB_OPTION_DETAIL, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getOptionDetail(lcCd:Int): ArrayList<EstimateTypeA>? {
        val db = readableDatabase
        var result = ArrayList<EstimateTypeA>()
        val cursor = db.rawQuery("SELECT * FROM TB_OPTION_DETAIL WHERE lccd=$lcCd", null) ?: return null
        if (cursor.moveToFirst()) {
            do {
                var mcCd = cursor.getInt(cursor.getColumnIndex("mcCd"))
                var lcCd = cursor.getInt(cursor.getColumnIndex("lcCd"))
                var mcNm = cursor.getString(cursor.getColumnIndex("mcNm"))
                var mcImgUrl = cursor.getString(cursor.getColumnIndex("mcImgUrl"))
                var sortNo = cursor.getInt(cursor.getColumnIndex("sortNo"))
                var memo = cursor.getString(cursor.getColumnIndex("memo"))
                result.add(EstimateTypeA(mcCd, lcCd, mcNm, mcImgUrl, sortNo, memo))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun insertArea(data: AreaList) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("areaCd", data.areaCd)
        contentValues.put("areaNm", data.areaNm)
        contentValues.put("areaImgUrl", data.areaImgUrl)
        contentValues.put("areaGb", data.areaGb)
        contentValues.put("memo", data.memo)
        db.insert(TB_AREA, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getArea(areaName:String): ArrayList<AreaList>? {
        val db = readableDatabase
        var result = ArrayList<AreaList>()

        var cursor: Cursor?

        if(areaName == "word") {
//            cursor = db.rawQuery("SELECT * FROM TB_AREA WHERE areaNm NOT IN('한국')", null) ?: return null
            cursor = db.rawQuery("SELECT * FROM TB_AREA WHERE areaGb NOT IN('2')", null) ?: return null
        } else {
//            cursor = db.rawQuery("SELECT * FROM TB_AREA WHERE areaNm='$areaName'", null) ?: return null
            cursor = db.rawQuery("SELECT * FROM TB_AREA WHERE areaGb='2'", null) ?: return null
        }

        if (cursor.moveToFirst()) {
            do {
                var areaCd = cursor.getInt(cursor.getColumnIndex("areaCd"))
                var areaNm = cursor.getString(cursor.getColumnIndex("areaNm"))
                var areaImgUrl = cursor.getString(cursor.getColumnIndex("areaImgUrl"))
                var areaGb = cursor.getInt(cursor.getColumnIndex("areaGb"))
                var memo = cursor.getString(cursor.getColumnIndex("memo"))
                result.add(AreaList(areaCd, areaNm, areaImgUrl, areaGb, memo))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

//    fun insertAreaDetail(data: AreaList) {
//        val db = writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put("areaCd", data.areaCd)
//        contentValues.put("areaNm", data.areaNm)
//        contentValues.put("areaImgUrl", data.areaImgUrl)
//        contentValues.put("areaGb", data.areaGb)
//        contentValues.put("memo", data.memo)
//        db.insert(TB_AREA_DETAIL, null, contentValues)
//        db.close()
//    }

    fun insertDest(data: DestList) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("destCd", data.destCd)
        contentValues.put("destNm", data.destNm)
        contentValues.put("destImgUrl", data.destImgUrl)
        contentValues.put("destDesc", data.destDesc)
        contentValues.put("area_CD", data.area_CD)
        contentValues.put("destRequestCnt", data.destRequestCnt)
        contentValues.put("memo", data.memo)
        contentValues.put("areaGb", data.areaGb)
        contentValues.put("recommend", data.recommend)
        db.insert(TB_DEST, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getDest(dest:String?): ArrayList<DestList>? {
        val db = readableDatabase
        var result = ArrayList<DestList>()
        var cursor: Cursor?
        if(TextUtils.isEmpty(dest)) {
            cursor = db.rawQuery("SELECT * FROM TB_DEST", null) ?: return null
        } else {
//            cursor = db.rawQuery("SELECT * FROM TB_DEST WHERE destNm LIKE '%" + "$dest%'", null) ?: return null
            cursor = db.rawQuery("SELECT * FROM TB_DEST WHERE destNm LIKE '%" + "$dest%'" + " or " + "memo LIKE '%" + "$dest%'", null) ?: return null

//            cursor = db.rawQuery("SELECT * FROM TB_DEST WHERE destNm like '%" + "$dest%'" + " and " + "destDesc LIKE '%" + "$dest%'", null) ?: return null
        }

        if (cursor.moveToFirst()) {
            do {
                var destCd = cursor.getInt(cursor.getColumnIndex("destCd"))
                var destNm = cursor.getString(cursor.getColumnIndex("destNm"))
                var destImgUrl = cursor.getString(cursor.getColumnIndex("destImgUrl"))
                var destDesc = cursor.getString(cursor.getColumnIndex("destDesc"))
                var area_CD = cursor.getInt(cursor.getColumnIndex("area_CD"))
                var destRequestCnt = cursor.getInt(cursor.getColumnIndex("destRequestCnt"))
                var memo = cursor.getString(cursor.getColumnIndex("memo"))
                var areaGb = cursor.getInt(cursor.getColumnIndex("areaGb"))
                var recommend = cursor.getInt(cursor.getColumnIndex("recommend"))
                result.add(DestList(destCd, destNm, destImgUrl, destDesc, area_CD, destRequestCnt, memo, areaGb, recommend))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun insertSearch(data: SearchList) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("num", data.num)
        contentValues.put("cd", data.cd)
        contentValues.put("tUrl", data.tUrl)
        contentValues.put("gb", data.gb)
        contentValues.put("tableCd", data.tableCd)
        contentValues.put("txt", data.txt)
        db.insert(TB_SEARCH, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getSearch(dest:String?): ArrayList<SearchList>? {
        val db = readableDatabase
        var result = ArrayList<SearchList>()
        var cursor: Cursor?
        if(TextUtils.isEmpty(dest)) {
            cursor = db.rawQuery("SELECT * FROM TB_SEARCH", null) ?: return null
        } else {
            cursor = db.rawQuery("SELECT * FROM TB_SEARCH WHERE nm LIKE '%" + "$dest%'" + " or " + "txt LIKE '%" + "$dest%'", null) ?: return null
        }

        if (cursor.moveToFirst()) {
            do {
                var num = cursor.getInt(cursor.getColumnIndex("num"))
                var cd = cursor.getInt(cursor.getColumnIndex("cd"))
                var nm = cursor.getString(cursor.getColumnIndex("nm"))
                var tUrl = cursor.getString(cursor.getColumnIndex("tUrl"))
                var gb = cursor.getInt(cursor.getColumnIndex("gb"))
                var tableCd = cursor.getInt(cursor.getColumnIndex("tableCd"))
                var txt = cursor.getString(cursor.getColumnIndex("txt"))
                result.add(SearchList(num, cd, nm, tUrl, gb, tableCd, txt))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun insertSearchSave(name: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("searchName", name)
        db.insert(SEARCH_SAVE_LIST, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun getSearchSaveList(): ArrayList<String>? {
        val db = readableDatabase
        var result = ArrayList<String>()
        val cursor = db.rawQuery("SELECT DISTINCT searchName FROM SEARCH_SAVE_LIST ORDER BY SEQ DESC LIMIT 10", null) ?: return null
        if (cursor.moveToFirst()) {
            do {
                var searchName = cursor.getString(cursor.getColumnIndex("searchName"))
                result.add(searchName.toString())
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    @SuppressLint("Range")
    fun delSearchSaveList(name:String) {
        val db = writableDatabase
        var selection = ""
        if(TextUtils.isEmpty(name)) {
            selection = ""
        } else {
            selection = "searchName ='$name'"
        }

        db.delete("SEARCH_SAVE_LIST", selection, null)
    }


    fun DeleteTable(table:String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM $table;")
        db.close()
    }
}
