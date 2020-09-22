package com.example.clientnetworking.views.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.clientnetworking.Adapter.AdapterUser;
import com.example.clientnetworking.RecyclerItemClickListener;
import com.example.clientnetworking.views.activity.DetailUserActivity;
import com.example.clientnetworking.views.activity.EditUserCustomerActivity;
import com.example.clientnetworking.views.activity.LoginActivity;
import com.example.clientnetworking.R;
import com.example.clientnetworking.SharedPreferencesGetKey;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.Data;
import com.example.clientnetworking.models.UserInfor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    View view;
    APIServices apiServices;
    ImageView image_back;
    TextView tvWellCome, tvEmailCustomer, tvPhoneCustomer, tvAddressCustomer, tvBirthdayCustomer;
    EditText edtFitter;
    RelativeLayout relativeLayoutCustomer,relativeLayoutRecy;
    CircleImageView imgPicture;
    FloatingActionButton floatEditCustomer;
    List<UserInfor> listUserInfor = new ArrayList<>();
    List<Data> dataList = new ArrayList<>();
    RecyclerView recyclerView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        getSharedPreferencesGetKey(new SharedPreferencesGetKey(getContext()).isLogin());
        return view;
    }

    private void initView() {
        image_back = view.findViewById(R.id.image_back);
        tvWellCome = view.findViewById(R.id.tvWellCome);
        imgPicture = view.findViewById(R.id.imgPicture);
        tvEmailCustomer = view.findViewById(R.id.tvEmailCustomer);
        tvPhoneCustomer = view.findViewById(R.id.tvPhoneCustomer);
        tvAddressCustomer = view.findViewById(R.id.tvAddressCustomer);
        tvBirthdayCustomer = view.findViewById(R.id.tvBirthdayCustomer);
        recyclerView = view.findViewById(R.id.recycleViews);
        relativeLayoutCustomer = view.findViewById(R.id.relativeLayoutCustomer);
        floatEditCustomer = view.findViewById(R.id.floatEditCustomer);
        relativeLayoutRecy = view.findViewById(R.id.relativeLayoutRecy);
        edtFitter = view.findViewById(R.id.edtFitter);

        image_back.setOnClickListener(this);
        floatEditCustomer.setOnClickListener(this);
    }

    private void getSharedPreferencesGetKey(String login) {
        apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.getKey(login).enqueue(new Callback<UserInfor    >() {
            @Override
            public void onResponse(Call<UserInfor> call, Response<UserInfor> response) {
                tvWellCome.setText(response.body().getFullname());
                relativeLayoutCustomer.setVisibility(View.GONE);
                relativeLayoutRecy.setVisibility(View.VISIBLE);
                if (response.body().getPermission()) {
                    Picasso.get().load(response.body().getImage()).error(R.drawable.logopoly).into(imgPicture);
                    listUserInfor.add(new UserInfor(response.body().getId(),
                            response.body().getFullname(),
                            response.body().getEmail(),
                            response.body().getHash(),
                            response.body().getBirthday(),
                            response.body().getPhone(),
                            response.body().getAddress(),
                            response.body().getImage(),
                            response.body().getPermission(),
                            response.body().getData()));

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        dataList.add(new Data(response.body().getData().get(i).getId(),
                                response.body().getData().get(i).getFullname(),
                                response.body().getData().get(i).getEmail(),
                                response.body().getData().get(i).getHash(),
                                response.body().getData().get(i).getPhone(),
                                response.body().getData().get(i).getAddress(),
                                response.body().getData().get(i).getImage(),
                                response.body().getData().get(i).getPermission()));
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    final AdapterUser adapterUser = new AdapterUser(dataList, getContext());
                    recyclerView.setAdapter(adapterUser);
                    edtFitter.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                adapterUser.getFilter().filter(s);
                            }catch (Exception e){
                                e.getMessage();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putString("_id",dataList.get(position).getId());
                            Intent intent = new Intent(getActivity(), DetailUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
                } else {
                    Picasso.get().load("http://192.168.1.163:3000/uploads/"+response.body().getImage()).error(R.drawable.logopoly).into(imgPicture);
                    relativeLayoutRecy.setVisibility(View.GONE);
                    relativeLayoutCustomer.setVisibility(View.VISIBLE);
                    tvEmailCustomer.setText(response.body().getEmail());
                    tvBirthdayCustomer.setText(response.body().getBirthday());
                    tvPhoneCustomer.setText(response.body().getPhone());
                    tvAddressCustomer.setText(response.body().getAddress());

                    listUserInfor.add(new UserInfor(
                            response.body().getFullname(),
                            response.body().getEmail(),
                            response.body().getBirthday(),
                            response.body().getPhone(),
                            response.body().getAddress(),
                            response.body().getImage(),
                            response.body().getV()));
                }
            }

            @Override
            public void onFailure(Call<UserInfor> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                LogOut();
                break;
            case R.id.floatEditCustomer:
                startActivity(new Intent(getActivity(), EditUserCustomerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
                break;
            default:
        }
    }

    private void LogOut() {
        SharedPreferencesGetKey sharedPreferencesGetKey = new SharedPreferencesGetKey(getContext());
        sharedPreferencesGetKey.LogOut("", "", false);
        startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
        dataList.removeAll(dataList);
        listUserInfor.removeAll(listUserInfor);
    }
}