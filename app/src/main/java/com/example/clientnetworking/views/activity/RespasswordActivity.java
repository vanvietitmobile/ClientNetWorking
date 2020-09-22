package com.example.clientnetworking.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.clientnetworking.R;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.RestModel;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespasswordActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack;
    EditText edtEmailRest, edtCode, edtNewPass, edtNewPassConfirm;
    RelativeLayout relativeLayoutEmail, relativeLayoutCode, relativeLayoutNewPass;
    Button btnSendEmail, btnSendCode, btnNewPass;
    APIServices apiServices;
    int REQUEST_CODE =0 ;
    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respassword);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        edtEmailRest = findViewById(R.id.edtEmailRest);
        edtCode = findViewById(R.id.edtCode);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtNewPassConfirm = findViewById(R.id.edtNewPassConfirm);
        relativeLayoutEmail = findViewById(R.id.relativeLayoutEmail);
        relativeLayoutCode = findViewById(R.id.relativeLayoutCode);
        relativeLayoutNewPass = findViewById(R.id.relativeLayoutNewPass);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSendCode = findViewById(R.id.btnSendCode);
        btnNewPass = findViewById(R.id.btnNewPass);
        imgBack.setOnClickListener(this);
        btnSendEmail.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        btnNewPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                startActivity(new Intent(RespasswordActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.btnSendEmail:
                restEmail(v);
                break;
            case R.id.btnSendCode:
                enterCOde(v);
                break;
            case R.id.btnNewPass:
                newPass(v);
                break;
            default:
        }
    }

    private int checkEmail(View view,String email) {
        if (TextUtils.isEmpty(email)) {
            Snackbar snackbar = Snackbar.make(view,"Please enter email your", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }else if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            Snackbar snackbar = Snackbar.make(view,"Incorrect email format", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }
        return 1;
    }
    private int checkCode(View view,String code){
        if (TextUtils.isEmpty(code)){
            Snackbar snackbar = Snackbar.make(view,"Incorrect email format", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return-1;
        }
        return 1;
    }
    private int checkNewEmail(View view,String pass, String confirm){
        if (TextUtils.isEmpty(pass)){
            Snackbar snackbar = Snackbar.make(view,"Please enter your new password", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }else if (TextUtils.isEmpty(confirm)){
            Snackbar snackbar = Snackbar.make(view,"Please enter your confirm new password", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }else if (!pass.equals(confirm)){
            Snackbar snackbar = Snackbar.make(view,"Incorrect confirm passowrd", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }
        return 1;
    }


    private void restEmail(final View view){
        try {
            if (checkEmail(view,edtEmailRest.getText().toString())>0){
              apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
              apiServices.sendEmail(edtEmailRest.getText().toString().trim()).enqueue(new Callback<RestModel>() {
                  @Override
                  public void onResponse(Call<RestModel> call, Response<RestModel> response) {
                      if (response.body().getStatus()){
                          relativeLayoutEmail.setVisibility(View.GONE);
                          relativeLayoutNewPass.setVisibility(View.GONE);
                          relativeLayoutCode.setVisibility(View.VISIBLE);
                          REQUEST_CODE = response.body().getRequestCode();
                          _id = response.body().getData().getId();
                          Log.d("TAG", "onResponse: " + REQUEST_CODE);
                      }else {
                          Snackbar snackbar = Snackbar.make(view,response.body().getMessage(), Snackbar.LENGTH_SHORT);
                          snackbar.show();
                      }
                  }

                  @Override
                  public void onFailure(Call<RestModel> call, Throwable t) {
                      Toast.makeText(RespasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              });
            }

        }catch (Exception e){
            Log.d("TAG", "restEmail: "+ e.getMessage());
        }
    }
    private void enterCOde(View view){
        try {
            if (checkCode(view,edtCode.getText().toString())>0){
               if (Integer.parseInt(edtCode.getText().toString()) == REQUEST_CODE){
                   relativeLayoutEmail.setVisibility(View.GONE);
                   relativeLayoutNewPass.setVisibility(View.VISIBLE);
                   relativeLayoutCode.setVisibility(View.GONE);
               }else {
                   Snackbar snackbar = Snackbar.make(view,"Wrong code", Snackbar.LENGTH_SHORT);
                   snackbar.show();
               }
            }

        }catch (Exception e){
            Log.d("TAG", "enterCOde: " +e.getMessage());
        }
    }
    private void newPass(final View view){
        try {
            if (checkNewEmail(view,edtNewPass.getText().toString(),edtNewPassConfirm.getText().toString())>0){
                apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
                apiServices.changPassByID(_id,edtNewPass.getText().toString()).enqueue(new Callback<RestModel>() {
                    @Override
                    public void onResponse(Call<RestModel> call, Response<RestModel> response) {
                        if (response.body().getStatus()){
                            Snackbar snackbar = Snackbar.make(view,response.body().getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }else {
                            Snackbar snackbar = Snackbar.make(view,"Successfully failed change", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RestModel> call, Throwable t) {

                    }
                });

            }

        }catch (Exception e){
            Log.d("TAG", "newPass: " + e.getMessage());
        }
    }

}