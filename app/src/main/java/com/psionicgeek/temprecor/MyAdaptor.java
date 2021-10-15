package com.psionicgeek.temprecor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.MyViewHolder> {
    Context context;
    ArrayList<ModelClass> list;

    public MyAdaptor(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singlerowdesign,parent,false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelClass modelClass = list.get(position);
//        System.out.println(modelClass.getCropedface());
        new ImageLoadTask(modelClass.getCropedface(), holder).execute();
        holder.nametext.setText(modelClass.getUserName());
        holder.phone.setText(modelClass.getPhone());
        holder.temp.setText(modelClass.getTemp());
        holder.dateandtime.setText(modelClass.getDateandtime());


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView nametext,phone,temp,dateandtime;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("yha44");
            img1 = itemView.findViewById(R.id.img1);
            nametext = itemView.findViewById(R.id.nametext);
            phone = itemView.findViewById(R.id.phone);
            temp = itemView.findViewById(R.id.temp);
            dateandtime=itemView.findViewById(R.id.datetime);
            System.out.println("yha155");
        }
    }
}




