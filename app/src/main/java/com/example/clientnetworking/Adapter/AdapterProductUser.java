package com.example.clientnetworking.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientnetworking.R;
import com.example.clientnetworking.models.PostusModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.ViewHolder> {
    List<PostusModel> postusModelList ;
    Context context;

    public AdapterProductUser(List<PostusModel> postusModelList, Context context) {
        this.postusModelList = postusModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_product,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAddressCustomer.setText(postusModelList.get(position).getAddress());
        holder.tvDate.setText(postusModelList.get(position).getDate());
        holder.tvFullNameCustomer.setText(postusModelList.get(position).getFullname());
        holder.tvTitle.setText(postusModelList.get(position).getTitle());
        holder.tvTus.setText(postusModelList.get(position).getContent());
        if (postusModelList.get(position).getActive()){
            holder.tvWait.setText("Approved");
            holder.tvWait.setTextColor(Color.GREEN);
        }else {
            holder.tvWait.setText("Pending");
            holder.tvWait.setTextColor(Color.RED);
        }
        Picasso.get().load("http://192.168.1.163:3000/uploads/"+postusModelList.get(position).getImage()).into(holder.imageTus);

    }

    @Override
    public int getItemCount() {
        return postusModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTus;
        TextView tvTitle,tvDate,tvFullNameCustomer,tvTus,tvAddressCustomer,tvWait;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTus = itemView.findViewById(R.id.imageTus);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFullNameCustomer = itemView.findViewById(R.id.tvFullNameCustomer);
            tvTus = itemView.findViewById(R.id.tvTus);
            tvAddressCustomer = itemView.findViewById(R.id.tvAddressCustomer);
            tvWait = itemView.findViewById(R.id.tvWait);
        }
    }
}
