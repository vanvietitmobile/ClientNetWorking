package com.example.clientnetworking.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientnetworking.MainActivity;
import com.example.clientnetworking.R;
import com.example.clientnetworking.SharedPreferencesGetKey;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.UserModels;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    EditText edtPassWord, edtEmail;
    TextView tvResign,tvforgot;
    APIServices apiServices;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferencesGetKey sharedPreferencesGetKey = new SharedPreferencesGetKey(getApplicationContext());
        if (sharedPreferencesGetKey.checkSaveLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        edtPassWord = findViewById(R.id.edtPassWord);
        edtEmail = findViewById(R.id.edtEmail);
        tvResign = findViewById(R.id.tvResign);
        tvforgot = findViewById(R.id.tvforgot);
        tvResign.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvforgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvResign:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.btnLogin:
                LoginUser(v);
                break;
            case R.id.tvforgot:
                startActivity(new Intent(LoginActivity.this, RespasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            default:
        }
    }

    private void LoginUser(final View v) {
        try {
            if (checkADD(v,edtEmail.getText().toString(),edtPassWord.getText().toString())>0){
                apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
                apiServices.loginUser(edtEmail.getText().toString(), edtPassWord.getText().toString()).enqueue(new Callback<UserModels>() {
                    @Override
                    public void onResponse(Call<UserModels> call, Response<UserModels> response) {
                        assert response.body() != null;
                        if (response.body().getStatus()) {
                            SharedPreferencesGetKey sharedPreferencesGetKey = new SharedPreferencesGetKey(getApplicationContext());
                            sharedPreferencesGetKey.SaveUser(response.body().getData().getEmail(),
                                    response.body().getData().getHash(),
                                    response.body().getStatus());
                            Snackbar snackbar = Snackbar.make(v, "Login in successfully", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        } else {
                            Snackbar snackbar = Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModels> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Fail connect server", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }catch (Exception e){
            e.getMessage();
        }

    }

    public int checkADD(View view,String email, String password) {
        int check = 1;

        if (TextUtils.isEmpty(email)){
            Snackbar snackbar = Snackbar.make(view,"Input your email", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }else if (TextUtils.isEmpty(password)){
            Snackbar snackbar = Snackbar.make(view,"Input your password", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }else if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            Snackbar snackbar = Snackbar.make(view,"Incorrect email format", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }
        return check;
    }
}
    