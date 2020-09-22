package com.example.clientnetworking.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.clientnetworking.Adapter.AdapterProductAdmin;
import com.example.clientnetworking.Adapter.AdapterProductUser;
import com.example.clientnetworking.Adapter.AdapterUser;
import com.example.clientnetworking.MainActivity;
import com.example.clientnetworking.R;
import com.example.clientnetworking.RecyclerItemClickListener;
import com.example.clientnetworking.SharedPreferencesGetKey;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.ModelgetPost;
import com.example.clientnetworking.models.PostusModel;
import com.example.clientnetworking.models.UserInfor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack;
    RecyclerView recycleViewsCheckList;
    APIServices apiServices;
    List<PostusModel> postusModelList = new ArrayList<>();
    String email ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        getSharedPreferencesGetKey(new SharedPreferencesGetKey(getApplicationContext()).isLogin());
        initView();
    }

    private void getSharedPreferencesGetKey(final String login) {
        apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.getKey(login).enqueue(new Callback<UserInfor>() {
            @Override
            public void onResponse(Call<UserInfor> call, Response<UserInfor> response) {
                if (response.body().getPermission()) {
                    apiServices.getAllProduct().enqueue(new Callback<ModelgetPost>() {
                        @Override
                        public void onResponse(Call<ModelgetPost> call, Response<ModelgetPost> response) {

                            for (int i = 0; i < response.body().getPostusModel().size(); i++) {
                                if (!response.body().getPostusModel().get(i).getActive()) {
                                    postusModelList.add(new PostusModel(
                                            response.body().getPostusModel().get(i).getId(),
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
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recycleViewsCheckList.setLayoutManager(layoutManager);
                            final AdapterProductAdmin adapterProductAdmin = new AdapterProductAdmin(postusModelList, getApplicationContext());
                            recycleViewsCheckList.setAdapter(adapterProductAdmin);


                        }

                        @Override
                        public void onFailure(Call<ModelgetPost> call, Throwable t) {

                        }
                    });
                }else {
                    apiServices.getProductUser(response.body().getEmail()).enqueue(new Callback<ModelgetPost>() {
                        @Override
                        public void onResponse(Call<ModelgetPost> call, Response<ModelgetPost> response) {
                            for (int i = 0; i<response.body().getPostusModel().size();i++){
                                    postusModelList.add(new PostusModel(
                                            response.body().getPostusModel().get(i).getId(),
                                            response.body().getPostusModel().get(i).getEmail(),
                                            response.body().getPostusModel().get(i).getFullname(),
                                            response.body().getPostusModel().get(i).getTitle(),
                                            response.body().getPostusModel().get(i).getContent(),
                                            response.body().getPostusModel().get(i).getAddress(),
                                            response.body().getPostusModel().get(i).getDate(),
                                            response.body().getPostusModel().get(i).getImage(),
                                            response.body().getPostusModel().get(i).getActive()));
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recycleViewsCheckList.setLayoutManager(layoutManager);
                            final AdapterProductUser adapterProductUser = new AdapterProductUser(postusModelList, getApplicationContext());
                            recycleViewsCheckList.setAdapter(adapterProductUser);
                            recycleViewsCheckList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recycleViewsCheckList, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(ListProductActivity.this,UpdateProductsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("_id",postusModelList.get(position).getId());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }
                            ));

                        }

                        @Override
                        public void onFailure(Call<ModelgetPost> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<UserInfor> call, Throwable t) {

            }
        });
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        recycleViewsCheckList = findViewById(R.id.recycleViewsCheckList);
        imgBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                startActivity(new Intent(ListProductActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            default:
        }
    }
}