package com.example.appfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.appfinal.databinding.ActivityChatBinding;
import com.example.appfinal.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, mReferenceMensaje;
    private String uid, uidPersona, nombrePersona, fotoPersona;

    private ChatAdapter adapter;
    private List<Mensaje> mensajes;

    private RecyclerView.LayoutManager lm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mensajes = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerChat);

        lm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(lm);

        recyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/");

        mReference = mDatabase.getReference("Cuenta");

        mReferenceMensaje = mDatabase.getReference().child("Mensajes");

        uid = mAuth.getCurrentUser().getUid();

        uidPersona = getIntent().getStringExtra("uidPersona");

        nombrePersona = getIntent().getStringExtra("nicknamePersona");

        fotoPersona = getIntent().getStringExtra("imagenPersona");

        binding.lblNombrePersona.setText(nombrePersona);


        Picasso.get().load(fotoPersona).
                resize(100, 100).
                centerCrop().
                into(binding.imgPersona);

        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = binding.txtChat.getText().toString();
                if (!mensaje.isEmpty()) {
                    Mensaje m = new Mensaje();
                    m.setMensaje(mensaje);
                    m.setUid(uid);
                    m.setUidPersona(uidPersona);
                    m.getFechaHora(new Date().getTime());
                    mDatabase.getReference().child("Mensajes").push().setValue(m);
                    binding.txtChat.setText("");
                }
            }
        });

        leerMensajes();

    }
    private void leerMensajes(){
        mReferenceMensaje.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mensajes.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Mensaje m = ds.getValue(Mensaje.class);
                    if (m.getUid().equals(uid) && m.getUidPersona().equals(uidPersona) ||
                            m.getUid().equals(uidPersona) && m.getUidPersona().equals(uid)){
                        mensajes.add(m);
                    }
                    adapter = new ChatAdapter(ChatActivity.this, mensajes, fotoPersona);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}