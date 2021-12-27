package com.devcomm.firebasejavalastapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.Viewholder> {


    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
      public void update(String filename , String url ){

          items.add(filename);
          urls.add(url);
          notifyDataSetChanged();

      }
    public MyAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items , ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item , parent , false);


        return new Viewholder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.nameOfitem.setText(items.get(position));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView nameOfitem;
        public Viewholder(@NonNull View itemView) {




            super(itemView);



            nameOfitem =  itemView.findViewById( R.id.textView_itemname);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = recyclerView.getChildLayoutPosition(v);
                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls.get(position)));

                   context.startActivity(intent);



                }
            });

        }
    }


}
