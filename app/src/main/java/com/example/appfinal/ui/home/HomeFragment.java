package com.example.appfinal.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.appfinal.R;
import com.example.appfinal.RegistrarCuentaActivity;
import com.example.appfinal.User;
import com.example.appfinal.conexiones.ApiConexiones;
import com.example.appfinal.conexiones.RetrofitObject;
import com.example.appfinal.configuraciones.Constantes;
import com.example.appfinal.databinding.FragmentHomeBinding;
import com.example.appfinal.modelos.Respuesta;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser user,currentUser;
    private Respuesta respuesta;
    private FragmentHomeBinding binding;
    private DatabaseReference refUser, refComprobarUser;
    private Retrofit retrofitObject;
    private ApiConexiones conexiones;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imageURL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        respuesta = new Respuesta();

        retrofitObject = RetrofitObject.getConexion();

        conexiones = retrofitObject.create(ApiConexiones.class);

        user = FirebaseAuth.getInstance().getCurrentUser();

        refUser = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Cuenta").child(user.getUid());

        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference().child("imagenes");





        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refUser.child("nickname").setValue(binding.textView2.getText().toString());
                refUser.child("age").setValue(binding.textView3.getText().toString());
                refUser.child("gender").setValue(binding.textView4.getText().toString());
                refUser.child("location").setValue(binding.textView5.getText().toString());
                refUser.child("aboutMe").setValue(binding.textView6.getText().toString());
                if (imageURL != null) {
                    refUser.child("imageUrl").setValue(imageURL);
                }
            }
        });

        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });


        return root;


    }

    @Override
    public void onResume(){
        super.onResume();
        info();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void info(){
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    binding.textView2.setText(user.getNickname());
                    binding.textView3.setText(user.getAge());
                    binding.textView4.setText(user.getGender());
                    binding.textView5.setText(user.getLocation());
                    binding.textView6.setText(user.getAboutMe());
                    binding.textView7.setText(user.getLolAcc());
                    Picasso.get()
                            .load(user.getImageUrl())
                            .resize(200, 200)
                            .centerCrop()
                            .error(R.drawable.ic_launcher_foreground)
                            .placeholder(R.drawable.descarga)
                            .into(binding.imageView3);
                    String name = user.getLolAcc();

                    conexiones.getJugador(name).enqueue(new Callback<Respuesta>() {
                        @Override
                        public void onResponse(retrofit2.Call<Respuesta> call, Response<Respuesta> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                respuesta = response.body();

                            }
                            
                            binding.textView8.setText(Integer.toString(respuesta.getSummonerLevel()));
                            Picasso.get()
                                    .load(Constantes.BASE_URL2 + respuesta.getProfileIconId() + ".png")
                                    .error(R.drawable.ic_launcher_foreground)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(binding.imageView);
                        }
                        @Override
                        public void onFailure(retrofit2.Call<Respuesta> call, Throwable t) {

                        }

                    });
                }else {
                    Intent intent = new Intent(getActivity(), RegistrarCuentaActivity.class);
                    startActivity(intent);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Picasso.get().load(uri)
                    .resize(200, 200)
                    .centerCrop()
                    .into(binding.imageView3);
            String path = mAuth.getUid();
            StorageReference imageRef = storageRef.child(path);
            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageURL = uri.toString();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
    }




}