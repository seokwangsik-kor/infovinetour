package com.infovine.tour.retrofit

import com.google.gson.JsonObject
import com.infovine.tour.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitAPI {

    @POST("api/v1/Auth/Register")
    fun register(@Body resister: Resister?) : Call<AuthResponseData?>?

    @POST("api/v1/Auth/Login")
    fun login(@Body login: Login?) : Call<LoginResonse?>?

    @POST("api/v1/Auth/Refresh")
    fun tokenRefresh(@Body tokenRefresh: TokenRefresh?) : Call<AuthResponseData?>?
//
//    @GET("api/v1/users/setting")
//    fun users_setting(@Header("Authorization") token: String) : Call<UserSettingData?>?
//
//    @GET("api/v1/posts")
//    fun tip(@Header("Authorization") token: String, @Query("pageNum") pageNum:Int, @Query("pageSize")pageSize:Int, @Query("postGubun")postGubun:Int) : Call<TipData?>?
//
//    @POST("api/v1/posts")
//    fun tipDetail(@Header("Authorization") token: String, @Query("postcode") postcode :String) : Call<TipDetailData?>?
//
//    @GET("/api/v1/quizs/apps")
//    fun Quiz(@Header("Authorization") token: String) : Call<QuizData?>?
//
//    @POST("/api/v1/quizs")
//    fun QuizDetail(@Header("Authorization") token: String, @Query("appcode") appcode:Int, @Query("quizcode") quizcode :Int) : Call<QuizDetailData?>?
//
//    @GET("/api/v1/clicks")
//    fun Click(@Header("Authorization") token: String) : Call<ClickData?>?
//
//    @PUT("/api/v1/clicks/click")
//    fun ClickPoint(@Header("Authorization") token: String, @Body request: ClickPointRequest) : Call<ClickPointData?>?
//
//    @PUT("/api/v1/apps/mypick")
//    fun MyPickAdd(@Header("Authorization") token: String, @Query("appcatecode") appcatecode:String) : Call<MypicAdd?>?
//
//    @GET("/api/v1/users/details")
//    fun LoginInfo(@Header("Authorization") token: String) : Call<LoginInfoData?>?
//
//    @DELETE("/api/v1/users")
//    fun UserDelete(@Header("Authorization") token: String) : Call<UserDel?>?
//
//    @GET("/api/v1/users/push")
//    fun PushConfig(@Header("Authorization") token: String) : Call<PushConfigData?>?
//
//    @PUT("/api/v1/users/push")
//    fun PushConfigUpdate(@Header("Authorization") token: String, @Body request: PushConfigUpdate) : Call<PushConfigData?>?
//
//    @GET("/api/v1/users/nicks")
//    fun NickNameCheck(@Header("Authorization") token: String, @Query("nickname") nickname:String) : Call<NickNameCheck?>?
//
//    @Multipart
//    @POST("/api/v1/profile/upload")
//    fun ProfileImage(@Header("Authorization") token: String, @Part body: MultipartBody.Part) : Call<JsonObject?>?
//
//    @PUT("/api/v1/users")
//    fun ProfileUpdate(@Header("Authorization") token: String, @Body request: ProfileUpdate) : Call<NomalResponse?>?
//
//    @GET("/api/v1/apps/ver")
//    fun AppUpdate(@Header("Authorization") token: String, @Query("appver") ver:String) : Call<AppUpdate?>?
//
//    @POST("/api/v1/apps")
//    fun AppDetiil(@Header("Authorization") token: String, @Query("appcode") appcode:Int) : Call<AppDetail?>?
//
//    @GET("api/v1/apps/mypick")
//    fun getMypickList(@Header("Authorization") token: String, @Query("dedupe") dedupe:String) : Call<MyPickList?>?
//
//    @PUT("api/v1/apps/mypick/click")
//    fun sendAppPlay(@Header("Authorization") token: String, @Query("appcode") appcode:Int) : Call<JsonObject?>?
//
//    @GET("/api/v1/posts/badge")
//    fun BadgeInfo(@Header("Authorization") token: String) : Call<BadgeData?>?
//
    @GET("/api/v1/events")
    fun EventList(@Header("Authorization") token: String, @Query("eventKind") eventKind :Int, @Query("pageNum") pageNum:Int, @Query("pageSize") pageSize:Int) : Call<EventPopupData?>?

//    @GET("/api/v1/admin/push")
//    fun AdminPushInfo(@Header("Authorization") token: String) : Call<AdminPushConfig?>?
//
//    @PUT("api/v1/users")
//    fun InstallVersion(@Header("Authorization") token: String, @Body request: InstallVersion) : Call<JsonObject?>?
//
    @POST("api/v1/Auth/ReqNumber")
    fun sendReqNumCert(@Body json: JsonObject?) : Call<JsonObject?>?

    @POST("api/v1/Auth/AuthNumber")
    fun PhoneNumChange(@Body request: PhoneNumReg) : Call<DataResponse?>?

    @GET("ThemeLists")
//    fun TehmeList() : Call<ThemeListData?>?
    suspend fun TehmeList() : Response<ThemeListData?>

    @GET("Class1Lists")
//    fun EstimateOption() : Call<EstimateOption?>?
    suspend fun EstimateOption() : Response<EstimateOption?>

    @GET("Class2Lists")
//    fun EstimateOptionDetail(@Query("LcCd") LcCd:Int) : Call<EstimateOptionDetail?>?
    suspend fun EstimateOptionDetail(@Query("LcCd") LcCd:Int) : Response<EstimateOptionDetail?>

    @GET("Class2Lists")
//    fun EstimateOptionDetail(@Query("LcCd") LcCd:Int) : Call<EstimateOptionDetail?>?
    suspend fun SearchList(@Query("SearchText") SearchText:String) : Response<Search?>

    @GET("TourVersion")
    fun getVersion() : Call<ApiVersion?>?

    @GET("TargetNationLists")
    suspend fun Area() : Response<Area?>

//    @GET("DestLists")
//    suspend fun Dest(@Query("AreaCd") AreaCd :Int) : Response<Dest?>
    @GET("Dest")
    suspend fun Dest(@Query("AreaCd") AreaCd :Int) : Response<Dest?>

    @POST("api/v1/Estimate/Estimate")
    fun Estimate(@Body estimate: Estimate?) : Call<EstimateResponse?>?


    @POST("api/v1/Estimate/Estimates")
    fun EstimateList(@Body data: Token?) : Call<EstimateList?>?

    @POST("api/v1/Estimate/EstimateContent")
    fun EstimateContent(@Body data: EstimateC?) : Call<MyEstimateDetail?>?
}