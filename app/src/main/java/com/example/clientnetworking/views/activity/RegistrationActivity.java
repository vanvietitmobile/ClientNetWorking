package com.example.clientnetworking.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clientnetworking.R;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.UserModels;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtFullName, edtEmail, edtPassWord, edtConfirmPassWord;
    Button btnRegistration;
    TextView tvLogin;
    APIServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
    }

    private void initView() {
        tvLogin = findViewById(R.id.tvLogin);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassWord = findViewById(R.id.edtPassWord);
        edtConfirmPassWord = findViewById(R.id.edtConfirmPassWord);
        btnRegistration = findViewById(R.id.btnRegistration);
        btnRegistration.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.btnRegistration:
                addUser(v);
                break;
            default:
        }
    }

    public int checkADD(View v) {
        int check = 1;
        if (TextUtils.isEmpty(edtFullName.getText().toString())) {
            Snackbar snackbar = Snackbar.make(v, "You need input FullName", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        } else if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            Snackbar snackbar = Snackbar.make(v, "You need input Email", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        } else if (TextUtils.isEmpty(edtPassWord.getText().toString())) {
            Snackbar snackbar = Snackbar.make(v, "You need input PassWord", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        } else if (TextUtils.isEmpty(edtConfirmPassWord.getText().toString())) {
            Snackbar snackbar = Snackbar.make(v, "You need input ConfirmPassWord", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        } else if (!edtConfirmPassWord.getText().toString().equals(edtPassWord.getText().toString())) {
            Snackbar snackbar = Snackbar.make(v, "Incorrect password", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }else if (!edtEmail.getText().toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            Snackbar snackbar = Snackbar.make(v, "Incorrect email format", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }
        return check;
    }
    private void addUser(final View view) {
        try {
            if (checkADD(view) > 0) {
                apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
                apiServices.postUser(edtFullName.getText().toString(),
                        edtEmail.getText().toString(), edtPassWord.getText().toString()).enqueue(new Callback<UserModels>() {
                    @Override
                    public void onResponse(Call<UserModels> call, Response<UserModels> response) {
                        if (response.body().getStatus()) {
                            Snackbar snackbar = Snackbar.make(view, "Sign Up Success", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            edtFullName.setText("");
                            edtEmail.setText("");
                            edtPassWord.setText("");
                            edtConfirmPassWord.setText("");
                        } else {
                            Snackbar snackbar = Snackbar.make(view, response.body().getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModels> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}