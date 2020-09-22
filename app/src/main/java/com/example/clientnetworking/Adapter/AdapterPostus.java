package com.example.clientnetworking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientnetworking.R;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.ModelgetPost;
import com.example.clientnetworking.models.PostusModel;
import com.example.clientnetworking.models.RestModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterPostus extends RecyclerView.Adapter<AdapterPostus.ViewHolder> {
    List<PostusModel> list;
    Context context;

    public AdapterPostus(List<PostusModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterPostus.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_recycel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterPostus.ViewHolder holder, int position) {
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvAddressCustomer.setText(list.get(position).getAddress());
        holder.tvFullNameCustomer.setText(list.get(position).getFullname());
        holder.tvTus.setText(list.get(position).getContent());
        Picasso.get().load("http://192.168.1.163:3000/uploads/" + list.get(position).getImage()).into(holder.imageTus);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTus, imageMessage;
        TextView tvDate, tvFullNameCustomer, tvTus, tvAddressCustomer, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTus = itemView.findViewById(R.id.imageTus);
            imageMessage = itemView.findViewById(R.id.imageMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFullNameCustomer = itemView.findViewById(R.id.tvFullNameCustomer);
            tvTus = itemView.findViewById(R.id.tvTus);
            tvAddressCustomer = itemView.findViewById(R.id.tvAddressCustomer);
            tvTitle = itemView.findViewById(R.id.tvTitle);

        }
    }
}
