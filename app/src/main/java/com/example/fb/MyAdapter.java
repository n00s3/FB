package com.example.fb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    Context context;
    ArrayList<Item> items = new ArrayList<Item>();
    static OnItemClickListener listener;

    public MyAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Item getItem(int position) {
        return items.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.setItem(item);
    }

    public void addItem(Item item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt1;
        TextView txt2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.item_text1);
            txt2 = itemView.findViewById(R.id.item_text2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Item item){
            txt1.setText(item.text1);
            txt2.setText(item.text2);
        }
    }

    
    // 리스너
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }
}