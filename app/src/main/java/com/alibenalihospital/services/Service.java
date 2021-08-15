package com.alibenalihospital.services;

import com.alibenalihospital.adapters.SingleDoctorModel;
import com.alibenalihospital.models.AllDepartmentModel;
import com.alibenalihospital.models.AllOfferModel;
import com.alibenalihospital.models.DoctorsDataModel;
import com.alibenalihospital.models.NotificationDataModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.PlaceGeocodeData;
import com.alibenalihospital.models.PlaceMapDetailsData;
import com.alibenalihospital.models.SliderDataModel;
import com.alibenalihospital.models.SettingModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone,
                          @Field("password") String password

    );


    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUpWithoutImage(@Field("name") String name,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("password") String password,
                                       @Field("confirm_password") String confirm_password
    );

    @GET("api/offers")
    Call<AllOfferModel> getOffers(
            @Header("language") String language

    );

    @GET("api/offer_department_id")
    Call<AllOfferModel> getOffers(
            @Header("language") String language,
            @Query("department_id") String department_id

    );

    @GET("api/departments")
    Call<AllDepartmentModel> getDepartments(
            @Header("language") String language

    );

    @GET("api/one_offer")
    Call<OfferDataModel> getSingleOffer(@Header("language") String language,
                                        @Query("offer_id") String offer_id,
                                        @Query("user_id") String user_id


    );

    @GET("api/search_doctor")
    Call<DoctorsDataModel> searchDoctors(@Header("language") String language,
                                         @Query("name") String name,
                                         @Query("user_id") String user_id


    );

    @GET("api/doctor")
    Call<SingleDoctorModel> doctorById(@Header("language") String language,
                                       @Query("user_id") String user_id,
                                       @Query("doctor_id") String doctor_id


    );

    @FormUrlEncoded
    @POST("api/favourite_doctor")
    Call<StatusResponse> fav_un_fav(@Header("language") String language,
                                    @Query("user_id") String user_id,
                                    @Query("doctor_id") String doctor_id


    );

    @FormUrlEncoded
    @POST("api/favourite_doctors")
    Call<DoctorsDataModel> getFavorites(@Header("language") String language,
                                        @Query("user_id") String user_id


    );


}