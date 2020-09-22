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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterProductAdmin extends RecyclerView.Adapter<AdapterProductAdmin.ViewHolder>  {
    List<PostusModel> list;
    Context context;

    public AdapterProductAdmin(List<PostusModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final APIServices apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvAddressCustomer.setText(list.get(position).getAddress());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvFullNameCustomer.setText(list.get(position).getFullname());
        holder.tvTus.setText(list.get(position).getContent());
        Picasso.get().load("http://192.168.1.163:3000/uploads/"+list.get(position).getImage()).into(holder.imageTus);
        holder.imageAccepte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                apiServices.changeActive(list.get(position).getId(),true).enqueue(new Callback<ModelgetPost>() {
                    @Override
                    public void onResponse(Call<ModelgetPost> call, Response<ModelgetPost> response) {
                        if (response.body().getStatus()){
                            Snackbar snackbar = Snackbar.make(v,"Accepte succesfully", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelgetPost> call, Throwable t) {

                    }
                });
            }
        });
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
            apiServices.deleteProduct(list.get(position).getId()).enqueue(new Callback<PostusModel>() {
                @Override
                public void onResponse(Call<PostusModel> call, Response<PostusModel> response) {
                    Snackbar snackbar = Snackbar.make(v,"Delete succesfully", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    list.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<PostusModel> call, Throwable t) {

                }
            });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTus,imageDelete,imageAccepte;
        TextView tvTitle,tvDate,tvFullNameCustomer,tvTus,tvAddressCustomer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTus = itemView.findViewById(R.id.imageTus);
            imageDelete = itemView.findViewById(R.id.imageDelete);
            imageAccepte = itemView.findViewById(R.id.imageAccepte);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFullNameCustomer = itemView.findViewById(R.id.tvFullNameCustomer);
            tvTus = itemView.findViewById(R.id.tvTus);
            tvAddressCustomer = itemView.findViewById(R.id.tvAddressCustomer);
        }
    }
}
