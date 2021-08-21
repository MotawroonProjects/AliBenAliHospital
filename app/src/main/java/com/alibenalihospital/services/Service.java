package com.alibenalihospital.services;

import com.alibenalihospital.adapters.SingleDoctorModel;
import com.alibenalihospital.models.AllDepartmentModel;
import com.alibenalihospital.models.AllOfferModel;
import com.alibenalihospital.models.DiseasesDataModel;
import com.alibenalihospital.models.DoctorsDataModel;
import com.alibenalihospital.models.NotificationDataModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.PlaceGeocodeData;
import com.alibenalihospital.models.PlaceMapDetailsData;
import com.alibenalihospital.models.ReservationDataModel;
import com.alibenalihospital.models.ReservationOfferDataModel;
import com.alibenalihospital.models.SliderDataModel;
import com.alibenalihospital.models.SettingModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;

import java.util.List;

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
                                        @Query("user_id") String user_id,
                                        @Query("reservation_id") String reservation_id


    );

    @GET("api/search_doctor")
    Call<DoctorsDataModel> searchDoctors(@Header("language") String language,
                                         @Query("name") String name,
                                         @Query("user_id") String user_id


    );

    @GET("api/doctor")
    Call<SingleDoctorModel> doctorById(@Header("language") String language,
                                       @Query("user_id") String user_id,
                                       @Query("doctor_id") String doctor_id,
                                       @Query("reservation_id") String reservation_id


    );

    @FormUrlEncoded
    @POST("api/favourite_doctor")
    Call<StatusResponse> fav_un_fav(@Header("language") String language,
                                    @Field("user_id") String user_id,
                                    @Field("doctor_id") String doctor_id


    );

    @GET("api/favourite_doctors")
    Call<DoctorsDataModel> getFavorites(@Header("language") String language,
                                        @Query("user_id") String user_id


    );

    @GET("api/slider")
    Call<SliderDataModel> getSlider();

    @GET("api/search_department")
    Call<AllDepartmentModel> searchDepartments(@Header("language") String language,
                                               @Query("name") String name

    );


    @GET("api/my_reservations")
    Call<ReservationDataModel> myReservation(@Header("language") String language,
                                             @Query("user_id") String user_id

    );

    @GET("api/my_offer_reservations")
    Call<ReservationOfferDataModel> myReservationOffer(@Header("language") String language,
                                                       @Query("user_id") String user_id

    );

    @GET("api/delete_reservation")
    Call<StatusResponse> deleteReservation(@Header("language") String language,
                                           @Query("reservation_id") String reservation_id

    );

    @GET("api/diseases")
    Call<DiseasesDataModel> getDiseases(@Header("language") String language

    );

    @FormUrlEncoded
    @POST("api/reservation")
    Call<StatusResponse> reserveDoctor(@Header("language") String language,
                                       @Field("doctor_id") String doctor_id,
                                       @Field("date_id") String date_id,
                                       @Field("hour_id") String hour_id,
                                       @Field("user_id") String user_id,
                                       @Field("name") String name,
                                       @Field("phone") String phone,
                                       @Field("call_type") String call_type,
                                       @Field("gender") String gender,
                                       @Field("age") String age,
                                       @Field("reservation_diseases[]") List<String> diseases


    );

    @Multipart
    @POST("api/reservation")
    Call<StatusResponse> reserveDoctorWithFiles(@Header("language") String language,
                                                @Part("doctor_id") RequestBody doctor_id,
                                                @Part("date_id") RequestBody date_id,
                                                @Part("hour_id") RequestBody hour_id,
                                                @Part("user_id") RequestBody user_id,
                                                @Part("name") RequestBody name,
                                                @Part("phone") RequestBody phone,
                                                @Part("call_type") RequestBody call_type,
                                                @Part("gender") RequestBody gender,
                                                @Part("age") RequestBody age,
                                                @Part("reservation_diseases[]") List<RequestBody> diseases,
                                                @Part List<MultipartBody.Part> images


    );

    @GET("api/doctor_department_id")
    Call<DoctorsDataModel> searchDoctorsByDepartment(@Header("language") String language,
                                                     @Query("name") String name,
                                                     @Query("department_id") String department_id


    );


    @FormUrlEncoded
    @POST("api/offer_reservation")
    Call<StatusResponse> reserveOffer(@Header("language") String language,
                                      @Field("offer_id") String offer_id,
                                      @Field("date_id") String date_id,
                                      @Field("hour_id") String hour_id,
                                      @Field("user_id") String user_id,
                                      @Field("name") String name,
                                      @Field("phone") String phone,
                                      @Field("call_type") String call_type,
                                      @Field("gender") String gender,
                                      @Field("age") String age,
                                      @Field("reservation_diseases[]") List<String> diseases


    );

    @Multipart
    @POST("api/offer_reservation")
    Call<StatusResponse> reserveOfferWithFiles(@Header("language") String language,
                                               @Part("offer_id") RequestBody offer_id,
                                               @Part("date_id") RequestBody date_id,
                                               @Part("hour_id") RequestBody hour_id,
                                               @Part("user_id") RequestBody user_id,
                                               @Part("name") RequestBody name,
                                               @Part("phone") RequestBody phone,
                                               @Part("call_type") RequestBody call_type,
                                               @Part("gender") RequestBody gender,
                                               @Part("age") RequestBody age,
                                               @Part("reservation_diseases[]") List<RequestBody> diseases,
                                               @Part List<MultipartBody.Part> images


    );

    @FormUrlEncoded
    @POST("api/contact")
    Call<StatusResponse> contactUs(@Header("language") String language,
                                   @Field("name") String name,
                                   @Field("email") String email,
                                   @Field("phone") String phone,
                                   @Field("message") String message
    );

    @GET("api/notifications")
    Call<NotificationDataModel> getNotifications(@Header("language") String language,
                                                 @Query("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("api/inser_token")
    Call<StatusResponse> updateFirebaseToken(@Header("language") String language,
                                             @Field("user_id") String user_id,
                                             @Field("firebase_token") String firebase_token,
                                             @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api/inser_token")
    Call<StatusResponse> logout(@Header("language") String language,
                                @Field("user_id") String user_id,
                                @Field("firebase_token") String firebase_token
    );

    @FormUrlEncoded
    @POST("api/update_profile")
    Call<UserModel> updateProfile(@Field("user_id") String user_id,
                                  @Field("name") String name
    );

    @FormUrlEncoded
    @POST("api/update_reservation")
    Call<StatusResponse> updateReserveDoctor(@Header("language") String language,
                                             @Field("reservation_id") String reservation_id,
                                             @Field("date_id") String date_id,
                                             @Field("hour_id") String hour_id,
                                             @Field("name") String name,
                                             @Field("phone") String phone,
                                             @Field("call_type") String call_type


    );

    @FormUrlEncoded
    @POST("api/rate_doctor")
    Call<StatusResponse> addRateDoctor(@Header("language") String language,
                                       @Field("user_id") String user_id,
                                       @Field("doctor_id") String doctor_id,
                                       @Field("title") String comment,
                                       @Field("rate") float rate

    );

    @FormUrlEncoded
    @POST("api/rate_offer")
    Call<StatusResponse> addRateOffer(@Header("language") String language,
                                      @Field("user_id") String user_id,
                                      @Field("offer_id") String offer_id,
                                      @Field("title") String comment,
                                      @Field("rate") float rate

    );
}