package com.example.appfinal.ui.dashboard;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinal.ChatAdapter;
import com.example.appfinal.ChatListAdapter;
import com.example.appfinal.R;
import com.example.appfinal.User;
import com.example.appfinal.UserAdapter;
import com.example.appfinal.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference refUser, refChat;
    private List<User> usuarios;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        refUser = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Cuenta");

        refChat = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Mensajes");

        mAuth = FirebaseAuth.getInstance();

        CurrentUser = mAuth.getCurrentUser();

        usuarios = new ArrayList<>();

        recyclerView = binding.recycler;

        adapter = new UserAdapter(usuarios,R.layout.card_view_user, getContext(), refUser);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());

        lm.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(lm);

        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

        listarPersonas();


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        listarPersonas();


    }

    private void listarPersonas(){
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarios.clear();
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        User user = ds.getValue(User.class);
                        if (!user.getUid().equals(CurrentUser.getUid()))
                            usuarios.add(user);

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    }







