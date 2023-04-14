package com.example.doctowatch;



import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class PDFAdaptor extends RecyclerView.Adapter<PDFAdaptor.MyviewHolder> {

    private Context mContext;
    private ArrayList<File> mFiles;
    String[] items;

    public PDFAdaptor(Context mContext, ArrayList<File> mFiles, String[] items) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.items = items;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.pdf_item,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {
        holder.file_name.setText(items[position]);

        holder.pdf_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PdfView.class);

                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView img_icon;
        RelativeLayout pdf_item;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.pdf_file_name);
            img_icon = itemView.findViewById(R.id.img_pdf);
            pdf_item = itemView.findViewById(R.id.pdf_item);
        }
    }
}
