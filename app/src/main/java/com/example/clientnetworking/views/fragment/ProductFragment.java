package com.example.clientnetworking.views.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clientnetworking.Adapter.AdapterPostus;
import com.example.clientnetworking.Adapter.AdapterUser;
import com.example.clientnetworking.MainActivity;
import com.example.clientnetworking.R;
import com.example.clientnetworking.SharedPreferencesGetKey;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.ModelgetPost;
import com.example.clientnetworking.models.PostusModel;
import com.example.clientnetworking.models.RestModel;
import com.example.clientnetworking.models.UserInfor;
import com.example.clientnetworking.views.activity.CreateProductActivity;
import com.example.clientnetworking.views.activity.ListProductActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment implements View.OnClickListener {

    View rootView;
    RecyclerView recyclerView;
    TextView tvPostus,tvUser,tvAdmin;
    APIServices apiServices;
    List<PostusModel> postusModelList =  new ArrayList<>();

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product, container, false);
        initView();
        getSharedPreferencesGetKey(new SharedPreferencesGetKey(getContext()).isLogin());

        return rootView;
    }

    private void getSharedPreferencesGetKey(String login) {
        apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.getKey(login).enqueue(new Callback<UserInfor>() {
            @Override
            public void onResponse(Call<UserInfor> call, Response<UserInfor> response) {
                if (response.body().getPermission()){
                    tvAdmin.setVisibility(View.VISIBLE);
                    tvUser.setVisibility(View.GONE);
                }else {
                    tvUser.setVisibility(View.VISIBLE);
                    tvAdmin.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<UserInfor> call, Throwable t) {

            }
        });
    }

    private void initView() {
        recyclerView = rootView.findViewById(R.id.recycleViewsTus);
        tvPostus = rootView.findViewById(R.id.tvPostus);
        tvAdmin = rootView.findViewById(R.id.tvAdmin);
        tvUser = rootView.findViewById(R.id.tvUser);
        tvPostus.setOnClickListener(this);
        tvAdmin.setOnClickListener(this);
        tvUser.setOnClickListener(this);
        apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.getAllProduct().enqueue(new Callback<ModelgetPost>() {
            @Override
            public void onResponse(Call<ModelgetPost> call, Response<ModelgetPost> response) {
                for (int i =0; i<response.body().getPostusModel().size();i++){
                    if (response.body().getPostusModel().get(i).getActive()){
                        postusModelList.add(new PostusModel(
                                response.body().getPostusModel().get(i).getEmail(),
                                response.body().getPostusModel().get(i).getFullname(),
                                response.body().getPostusModel().get(i).getTitle(),
                                response.body().getPostusModel().get(i).getContent(),
                                response.body().getPostusModel().get(i).getAddress(),
                                response.body().getPostusModel().get(i).getDate(),
                                response.body().getPostusModel().get(i).getImage(),
                                response.body().getPostusModel().get(i).getActive()));
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                AdapterPostus adapterPost = new AdapterPostus(postusModelList, getContext());
                recyclerView.setAdapter(adapterPost);
            }

            @Override
            public void onFailure(Call<ModelgetPost> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvPostus:
                startActivity(new Intent(getActivity(), CreateProductActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
                break;
            case R.id.tvAdmin:
                startActivity(new Intent(getActivity(), ListProductActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
                break;
            case R.id.tvUser:
                startActivity(new Intent(getActivity(), ListProductActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
                break;
            default:
        }
    }

}