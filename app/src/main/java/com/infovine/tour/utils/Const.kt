package com.infovine.tour.utils

import android.util.Log

class Const {

    companion object {

        const val URL_DOMAIN = "https://fe-em.infovine.co.kr:3312/#/"
        const val URL_THREESTEP = "https://fe-em.infovine.co.kr:3312/#/firstset"
        const val URL_APPLIST = "https://fe-em.infovine.co.kr:3312/#/Apps?tab=category"
        const val URL_APPLIST2 = "https://fe-em.infovine.co.kr:3312/#/Apps?tab=profit"

//        const val URL_DOMAIN = "https://tfe-em.infovine.co.kr:3312/#/"
//        const val URL_THREESTEP = "https://tfe-em.infovine.co.kr:3312/#/firstset"
//        const val URL_APPLIST = "https://tfe-em.infovine.co.kr:3312/#/Apps?tab=category"
//        const val URL_APPLIST2 = "https://tfe-em.infovine.co.kr:3312/#/Apps?tab=profit"

//        const val URL_DOMAIN = "http://tfe-moa.infovine.co.kr:3312/#/"
//        const val URL_THREESTEP = "http://tfe-moa.infovine.co.kr:3312/#/firstset"
//        const val URL_APPLIST = "http://tfe-moa.infovine.co.kr:3312/#/Apps?tab=category"
//        const val URL_APPLIST2 = "http://tfe-moa.infovine.co.kr:3312/#/Apps?tab=profit"

        const val PREF_JWT_TOKEN = "pref_jwt_token"//JWT토큰
        const val USER_SETTING_CHECK = "pref_user_setting"

        const val PREF_SNSLOGIN_TYPE = "pref_snslogin_type"//sns구분 1: Kakao 2:Google 3: Apple
        const val PREF_SNSLOGIN_OS_TYPE = "pref_snslogin_os_type"//os type/ aos: android ios: ios
        const val PREF_SNSLOGIN_ACCESS_TOKEN = "snslogin_access_Token"
        const val PREF_SNSLOGIN_EMAIL = "snslogin_email"
        const val PREF_SNSLOGIN_NICKNAME = "snslogin_nickname"
        const val PREF_SNSLOGIN_THUMNAIL_IMG = "snslogin_thumnail_img"
        const val PREF_TERMS_JOIN = "pref_terms_join"//가입동의 1: 동의 0: 비동의
        const val PREF_TERMS_INFO = "pref_terms_info"//개인정보제공동의 1: 동의 0: 비동의
        const val PREF_TERMS_GPS_YN = "pref_terms_gps_yn"//위치서비스 동의1: 동의 0: 비동의
        const val PREF_TERMS_PUSH_YN = "pref_terms_push_yn"//푸시 동의1: 동의 0: 비동의
        const val PREF_UPDATE_CODE = "pref_update_code"//
        const val PREF_UPDATE_MSG = "pref_update_msg"//
        const val PREF_UPDATE_URL = "pref_update_url"//
        const val PREF_UPDATE_VER = "pref_update_ver"//
        const val PREF_UPDATE_SEVENDAY = "pref_update_sevenday"//

        const val PREF_MEMBER_CD = "pref_member_cd"//회원번호
        const val PREF_MEMBER_PHONE_NUMBER = "pref_member_phone_number"//회원전화번호
        //견적서정보 REQUEST
        const val ESTIMATE_THEMECD = "estimate_themecd"//테마리스트
        const val ESTIMATE_DEST = "estimate_dest"//목적지
        const val ESTIMATE_DATETYPE = "estimate_datetype"//여행일자 타입 저렴, 빠른, 추천
        const val ESTIMATE_STARTDATE = "estimate_startdate"//여행출발일
        const val ESTIMATE_ENDDATE = "estimate_enddate"//여행도착일
        const val ESTIMATE_MONEY = "estimate_money"//예상금액
        const val ESTIMATE_ADULT_CNT = "estimate_adult_cnt"//성인인원
        const val ESTIMATE_CHILD_CNT = "estimate_child_cnt"//아동인원
        const val ESTIMATE_BABY_CNT = "estimate_baby_cnt"//소아인원
        const val ESTIMATE_AIRLINE = "estimate_airline"//항공사 옵션
        const val ESTIMATE_OPTION = "estimate_option"//숙박, 이동수단 옵션
        const val ESTIMATE_START_AREA = "estimate_start_area"//여행 출발지역

        //견적서수정
        const val ESTIMATE_EDIT = "estimate_edit"
        const val ESTIMATE_DEST_NAME = "estimate_dest_name"
        const val ESTIMATE_DEST_NAME_SUB = "estimate_dest_name_sub"
        const val ESTIMATE_DEST_IMG = "estimate_dest_img"
        const val ESTIMATE_DATE_EDIT = "estimate_date_edit"
        const val ESTIMATE_START_AREA_CD = "estimate_start_area_cd"
        const val ESTIMATE_OPTION_AIR_SINGLE_CD = "estimate_option_air_single_cd"
        const val ESTIMATE_OPTION_HOTEL_SINGLE_CD = "estimate_option_hotel_single_cd"

        const val ESTIMATE_MONEY_WON = "estimate_money_won"//예상금액
        const val ESTIMATE_DESTINATION_NAME = "estimate_destination_name"//목적지
        const val ESTIMATE_FULL_DATE = "estimate_full_date"//여행 기간표시
        const val ESTIMATE_OPTION_AIR = "estimate_option_air"//항공사
        const val ESTIMATE_OPTION_HOTEL = "estimate_option_hotel"//숙박
        const val ESTIMATE_OPTION_MOVE = "estimate_option_move"//이동수단
        const val ESTIMATE_OPTION_NAME_LIST = "estimate_option_name_list"//숙박, 이동수단 옵션
        const val SEARCH_SAVE_STATUS = "SEARCH_SAVE_STATUS"//여행지검색설정
        
        //api version
        const val THEME_VER = "theme_ver" //여행테마
        const val AREA_VER = "area_ver"//여행지역
        const val DEST_VER = "dest_ver"//여행목적지
        const val OPTION_CATEGORY = "option_category"//대분류 (항공사, 숙박, 렌트)
        const val OPTION_DETAIL = "option_detail"//중분류 (항공사 상세...)
        const val OPTION_VER = "option_ver"//옵션
        const val OPTION_DETAIL_VER = "option_detail_ver"//옵션상세
        const val SEARCH_VER = "search_ver" //검색

        const val ESTIMATE_STARTDATE1 = "estimate_startdate1"//여행출발일 dumy
        const val ESTIMATE_ENDDATE1 = "estimate_enddate1"//여행도착일 dumy

        const val BADGE_FLAG = "badge_flag"//
        const val PREF_EVENTPOPUP_ONEDAY = "pref_eventpopup_oneday"
        const val PUSH_MESSEAGE_CNT = "push_messeage_cnt"
        const val APP_IS_FIRST = "app_is_first"
        const val IMAGE_DOMAIN_URL = "image_domain_url"


        const val PREF_PUSH_ID = "pref_push_id"//PUSH KEY

        const val TEMRS_1 = "https://teverym-home.infovine.co.kr/rule_tour.html"
        const val TEMRS_2 = "https://teverym-home.infovine.co.kr/private_tour.html"

        //API 주소
        const val AUTH_DOMAIN = "https://t-tourauth.infovine.co.kr/"
        const val SERVICE_DOMAIN = "https://t-tour.infovine.co.kr/"

//        const val AUTH_DOMAIN = "https://auth-em.infovine.co.kr:3309/"
//        const val SERVICE_DOMAIN = "https://service-em.infovine.co.kr:3310/"

        const val IMAGE_DOMAIN = "https://t-tour-image.infovine.co.kr/"
    }

}