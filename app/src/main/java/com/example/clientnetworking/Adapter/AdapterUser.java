package com.example.clientnetworking.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientnetworking.R;
import com.example.clientnetworking.models.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> implements Filterable {
    List<Data> dataList;
    List<Data> dataListFull;
    Context context;

    public AdapterUser(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        dataListFull = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_recycle, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvFullName.setText(dataList.get(position).getFullname());
        holder.tvEmail.setText(dataList.get(position).getEmail());
            Picasso.get().load("http://192.168.1.163:3000/uploads/"+dataList.get(position).getImage()).error(R.drawable.logopoly).into(holder.imageItem);
            holder.tvCustomer.setText("Customer");

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Filter getFilter() {
        return filterUser;
    }
    private Filter filterUser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Data> filter = new ArrayList<>();
            if (constraint == null|| constraint.length()==0){
                filter.addAll(dataListFull);
            }else {
                String filterparen = constraint.toString().toLowerCase().trim();
                for (Data item : dataListFull){
                    if (item.getFullname().toLowerCase().contains(filterparen)){
                        filter.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList.clear();
            dataList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageItem;
        TextView tvFullName, tvEmail,tvCustomer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.imageItem);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);

        }
    }
}
