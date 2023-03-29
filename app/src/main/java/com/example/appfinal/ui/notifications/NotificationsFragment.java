package com.example.appfinal.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinal.ChatListAdapter;
import com.example.appfinal.R;
import com.example.appfinal.User;
import com.example.appfinal.UserAdapter;
import com.example.appfinal.conexiones.ApiConexiones;
import com.example.appfinal.configuraciones.Constantes;
import com.example.appfinal.databinding.FragmentDashboardBinding;
import com.example.appfinal.databinding.FragmentHomeBinding;
import com.example.appfinal.databinding.FragmentNotificationsBinding;
import com.example.appfinal.modelos.Respuesta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ChatListAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference refUser, refChat;
    private List<User> usuarios;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;

    private String uidPersona;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        refUser = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Cuenta");
        refChat = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Mensajes");

        mAuth = FirebaseAuth.getInstance();

        CurrentUser = mAuth.getCurrentUser();

        usuarios = new ArrayList<>();

        recyclerView = binding.recyclerChatList;

        adapter = new ChatListAdapter(usuarios, R.layout.chat_lista, getContext(), refUser);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(lm);

        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

        uidPersona = Constantes.uid;

        comprobar();

        comprobar2();

        comprobar3();

        return root;
    }

    private void comprobar(){
        Query query2 = refUser
                .orderByChild("uid")
                .equalTo(uidPersona);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    User user = chatSnapshot.getValue(User.class);
                    String uid2 = chatSnapshot.child("uid").getValue(String.class);
                    if (uid2.equals(uidPersona)){
                        usuarios.add(user);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        }

        private void comprobar2(){
        Query query = refChat
                .orderByChild("uidPersona")
                .equalTo(CurrentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> usersWithChats = new ArrayList<>();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String uid2 = chatSnapshot.child("uid").getValue(String.class);
                    usersWithChats.add(uid2);
                }
                refUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            User user = userSnapshot.getValue(User.class);
                            if (usersWithChats.contains(user.getUid())) {
                                usuarios.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        }


        private void comprobar3(){
            Query query2 = refChat
                    .orderByChild("uid")
                    .equalTo(CurrentUser.getUid());
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> usersWithChats = new ArrayList<>();
                    for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                        String uid2 = chatSnapshot.child("uidPersona").getValue(String.class);
                        usersWithChats.add(uid2);
                    }
                    refUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (usersWithChats.contains(user.getUid())) {
                                    usuarios.add(user);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    public void onResume() {
        super.onResume();
        comprobar();
        usuarios.clear();
        comprobar2();
        comprobar3();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}