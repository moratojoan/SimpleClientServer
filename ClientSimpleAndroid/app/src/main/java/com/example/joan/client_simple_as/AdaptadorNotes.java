package com.example.joan.client_simple_as;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorNotes extends RecyclerView.Adapter<AdaptadorNotes.ViewHolderNotes> implements View.OnClickListener{

    ArrayList<Notes> llista_notes;
    private View.OnClickListener listener;

    public AdaptadorNotes(ArrayList<Notes> llista_notes){
        this.llista_notes = llista_notes;
    }
    @Override
    public ViewHolderNotes onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota,null,false);
        view.setOnClickListener(this);
        return new ViewHolderNotes(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderNotes holder, int position) {
        holder.Titol.setText(llista_notes.get(position).getTitol_nota());
        holder.Text.setText(llista_notes.get(position).getText_nota());
    }

    @Override
    public int getItemCount() {
        return llista_notes.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderNotes extends RecyclerView.ViewHolder{

        TextView Titol, Text;

        public ViewHolderNotes(View itemView) {
            super(itemView);
            Titol = itemView.findViewById(R.id.txt_in_titol);
            Text = itemView.findViewById(R.id.txt_in_text);
        }
    }
}
