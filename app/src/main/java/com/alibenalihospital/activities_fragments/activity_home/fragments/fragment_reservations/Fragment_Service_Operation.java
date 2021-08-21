package com.alibenalihospital.activities_fragments.activity_home.fragments.fragment_reservations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.activities_fragments.activity_offer_detials.OfferDetialsActivity;
import com.alibenalihospital.activities_fragments.activity_reservation_offer_details.ReservationOfferDetailsActivity;
import com.alibenalihospital.activities_fragments.activity_service_process.ServiceProcessActivity;
import com.alibenalihospital.activities_fragments.activty_reservstion_details.ReservationDetailsActivity;
import com.alibenalihospital.adapters.MyReservationAdapter;
import com.alibenalihospital.adapters.MyReservationOfferAdapter;
import com.alibenalihospital.databinding.FragmentMyDatesBinding;
import com.alibenalihospital.databinding.FragmentServiceOperationBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.ReservationDataModel;
import com.alibenalihospital.models.ReservationModel;
import com.alibenalihospital.models.ReservationOfferDataModel;
import com.alibenalihospital.models.ReservationOfferModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.share.Common;
import com.alibenalihospital.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Service_Operation extends Fragment implements Listeners.ReservationOfferListener {

    private HomeActivity activity;
    private FragmentServiceOperationBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<ReservationOfferModel> list;
    private MyReservationOfferAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    public static Fragment_Service_Operation newInstance() {

        return new Fragment_Service_Operation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_operation, container, false);
        initView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req==1&&result.getResultCode()== Activity.RESULT_OK){
                getData();
            }
        });
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MyReservationOfferAdapter(list, activity, this);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getData);
        getData();


    }

    public void getData() {
        userModel = preferences.getUserData(activity);

        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        if (userModel == null) {
            binding.swipeRefresh.setRefreshing(false);
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoData.setVisibility(View.VISIBLE);
            return;
        }

        Api.getService(Tags.base_url).myReservationOffer(lang, userModel.getUser().getId() + "").
                enqueue(new Callback<ReservationOfferDataModel>() {
                    @Override
                    public void onResponse(Call<ReservationOfferDataModel> call, Response<ReservationOfferDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            }


                        } else {


                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ReservationOfferDataModel> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //  Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }


    @Override
    public void setReservationData(ReservationOfferModel model, int pos, int type) {
        if (type == 1) {
            show(model);
        } else if (type == 2) {
            update(model,pos);
        } else {
            delete(model, pos);
        }

    }



    private void show(ReservationOfferModel model) {
        Intent intent = new Intent(activity, ReservationOfferDetailsActivity.class);
        intent.putExtra("data", model);
        startActivity(intent);
    }
    private void update(ReservationOfferModel model, int pos) {
        req = 1;
        Intent intent = new Intent(activity, OfferDetialsActivity.class);
        intent.putExtra("offerid", model.getOffer_id());
        intent.putExtra("reservation", model);

        launcher.launch(intent);
    }
    private void delete(ReservationOfferModel model, int pos) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteReservation(lang, model.getId() + "")
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                model.setIs_deleted("yes");
                                list.set(pos, model);
                                adapter.notifyItemChanged(pos);
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}
