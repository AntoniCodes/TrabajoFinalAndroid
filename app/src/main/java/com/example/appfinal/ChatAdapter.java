package com.example.appfinal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firework.imageloading.glide.GlideImageLoaderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Mensaje> chats;
    private String imageurl;
    private FirebaseUser fuser;

    public ChatAdapter(Context context, List<Mensaje> chats, String imageurl) {
        this.context = context;
        this.chats = chats;
        this.imageurl = imageurl;
    }




    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_derecha, parent, false);
            return new ChatVH(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_izquierda, parent, false);
            return new ChatVH(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        String mensaje = chats.get(position).getMensaje();
        holder.lblPersona.setText(mensaje);
        /*Picasso.get().load(imageurl).into(holder.imgPersonaConectada);*/


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
       fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getUid().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public class ChatVH extends RecyclerView.ViewHolder {

        ImageView imgPersonaConectada;
        TextView lblPersona;
        public ChatVH(@NonNull View itemView) {
            super(itemView);
            imgPersonaConectada = itemView.findViewById(R.id.imgPersonaConectadaChat);
            lblPersona = itemView.findViewById(R.id.lblPersona);

        }

    }
}
