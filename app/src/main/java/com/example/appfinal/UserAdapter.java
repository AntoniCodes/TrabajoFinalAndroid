package com.example.appfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinal.configuraciones.Constantes;
import com.example.appfinal.ui.notifications.NotificationsFragment;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH> {

    private List<User> objects;

    private int resource;

    private Context context;

    private DatabaseReference refUser;

    public UserAdapter(List<User> users, int resource, Context context, DatabaseReference refUser) {
        this.objects = users;
        this.resource = resource;
        this.context = context;
        this.refUser = refUser;

    }


    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserVH(LayoutInflater.from(context).inflate(resource, null));
    }



    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        User user = objects.get(position);
        String uidPersona = user.getUid();
        String imagenPersona = user.getImageUrl();
        String nicknamePersona = user.getNickname();
        holder.lblNickname.setText(user.getNickname());
        holder.lblAge.setText(user.getAge());
        holder.lblGenero.setText(user.getGender());
        holder.lblSobreMi.setText(user.getAboutMe());
        holder.lblLocalidad.setText(user.getLocation());
        Picasso.get().load(user.getImageUrl())
                .placeholder(R.drawable.descarga)
                .error(R.drawable.ic_launcher_background)
                .resize(1000,1000)
                .centerCrop()
                .into(holder.imgFoto);

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uidPersona", uidPersona);
                intent.putExtra("imagenPersona", imagenPersona);
                intent.putExtra("nicknamePersona", nicknamePersona);
                Constantes.uid = uidPersona;
                context.startActivity(intent);

            }
        });
    }




    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class UserVH extends RecyclerView.ViewHolder {
        ImageView imgFoto;

        Button btnChat;
        TextView lblNickname,lblAge, lblGenero,lblSobreMi,lblLocalidad;
        public UserVH(@NonNull View itemView) {
            super(itemView);
            lblNickname = itemView.findViewById(R.id.txtNombre);
            lblAge = itemView.findViewById(R.id.txtEdad);
            lblGenero = itemView.findViewById(R.id.txtGenero);
            lblSobreMi = itemView.findViewById(R.id.txtSobreMi);
            lblLocalidad = itemView.findViewById(R.id.txtLocalidad);
            imgFoto = itemView.findViewById(R.id.imageView4);
            btnChat = itemView.findViewById(R.id.btnChat);
        }
    }




}
