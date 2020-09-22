package com.example.clientnetworking.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clientnetworking.MainActivity;
import com.example.clientnetworking.R;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.Data;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack,imageCustomer;
    TextView tvFullNameCustomer,tvBirthdayCustomer,tvAddressCustomer,tvPhoneCustomer,tvEmailCustomer,tvPasswordCustomer,tvFont;
    String _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        tvFullNameCustomer = findViewById(R.id.tvFullNameCustomer);
        tvBirthdayCustomer = findViewById(R.id.tvBirthdayCustomer);
        tvAddressCustomer = findViewById(R.id.tvAddressCustomer);
        tvPhoneCustomer = findViewById(R.id.tvPhoneCustomer);
        tvEmailCustomer = findViewById(R.id.tvEmailCustomer);
        tvPasswordCustomer = findViewById(R.id.tvPasswordCustomer);
        imageCustomer = findViewById(R.id.imageCustomer);
        Bundle b = getIntent().getExtras();
        _id = b.getString("_id");
        imgBack.setOnClickListener(this);
        APIServices apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.detailUser(_id).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                tvAddressCustomer.setText(response.body().getAddress());
                tvBirthdayCustomer.setText(response.body().getBirthday());
                tvEmailCustomer.setText(response.body().getEmail());
                tvFullNameCustomer.setText(response.body().getFullname());
                tvPasswordCustomer.setText(response.body().getHash());
                tvPhoneCustomer.setText(response.body().getPhone());
                Log.d("TAG", "onResponse: "+ tvPasswordCustomer.getText().toString());
                Picasso.get().load("http://192.168.1.163:3000/uploads/"+response.body().getImage()).error(R.drawable.logopoly).into(imageCustomer);

        }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                startActivity(new Intent(DetailUserActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            default:
        }
    }
}
