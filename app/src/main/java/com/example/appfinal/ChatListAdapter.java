package com.example.appfinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ListChatVH>{

    private List<User> objects;

    private int resource;

    private Context context;

    private DatabaseReference refUser;

    public ChatListAdapter(List<User> users, int resource, Context context, DatabaseReference refUser) {
        this.objects = users;
        this.resource = resource;
        this.context = context;
        this.refUser = refUser;
    }


    @NonNull
    @Override
    public ListChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListChatVH(LayoutInflater.from(context).inflate(resource, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatVH holder, int position) {
        User user = objects.get(position);
        holder.lblPersonaChatLista.setText(user.getNickname());
        Picasso.get().load(user.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .resize(500,0)
                .into(holder.imgPersonaChatLista);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("nicknamePersona", user.getNickname());
                intent.putExtra("imagenPersona", user.getImageUrl());
                intent.putExtra("uidPersona", user.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ListChatVH extends RecyclerView.ViewHolder {
        ImageView imgPersonaChatLista;
        TextView lblPersonaChatLista;
        public ListChatVH(@NonNull View itemView) {
            super(itemView);
            imgPersonaChatLista = itemView.findViewById(R.id.imgPersonaChatLista);
            lblPersonaChatLista = itemView.findViewById(R.id.lblPersonaChatLista);
        }
    }
}
