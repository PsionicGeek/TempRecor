package com.psionicgeek.temprecor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdaptor extends FirebaseRecyclerAdapter<ModelClass, MyAdaptor.MyViewHolder> {

    public MyAdaptor(@NonNull FirebaseRecyclerOptions<ModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull  ModelClass constants) {
        System.out.println("yha12");
        System.out.println("This is the information");
        System.out.println(constants.getUserName());
        System.out.println(constants.getCropedface());
        System.out.println(constants.getPhone());
        System.out.println(constants.getTemp());

        myViewHolder.nametext.setText(constants.getUserName());
        myViewHolder.coursetext.setText(constants.getPhone());
        myViewHolder.emailtext.setText(constants.getTemp());
        Glide.with(myViewHolder.img1.getContext()).load(constants.getCropedface()).into(myViewHolder.img1);
        System.out.println("yha33");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
       return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView nametext,coursetext,emailtext;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("yha44");
            img1 = itemView.findViewById(R.id.img1);
            nametext = itemView.findViewById(R.id.nametext);
            coursetext = itemView.findViewById(R.id.coursetext);
            emailtext = itemView.findViewById(R.id.emailtext);
            System.out.println("yha155");
        }
    }

}
