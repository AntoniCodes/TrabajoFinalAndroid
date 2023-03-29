package com.example.appfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.appfinal.conexiones.ApiConexiones;
import com.example.appfinal.conexiones.RetrofitObject;
import com.example.appfinal.databinding.ActivityRegistrarCuentaBinding;
import com.example.appfinal.modelos.Respuesta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrarCuentaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference refUser, refLolAcc, nuevaCuenta;
    private Retrofit retrofitObject;
    private ApiConexiones conexiones;
    private  boolean correcto = false;
    private ActivityRegistrarCuentaBinding binding;
    private Respuesta respuesta;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imageURL, uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        respuesta = new Respuesta();
        retrofitObject = RetrofitObject.getConexion();
        conexiones = retrofitObject.create(ApiConexiones.class);


        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance("https://appfinal-6f71d-default-rtdb.europe-west1.firebasedatabase.app/");

        refUser = mDatabase.getReference().child("Cuenta").child(mAuth.getUid());

        refLolAcc = mDatabase.getReference().child("CuentaLoL");

        nuevaCuenta = refLolAcc.push();

        storage = FirebaseStorage.getInstance();
        
        storageRef = storage.getReference().child("imagenes");

        uid = mAuth.getUid();

        binding.btnRegister.setEnabled(false);



        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                comprobarParametros();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        binding.editTextNickname.addTextChangedListener(textWatcher);
        binding.editTextAge.addTextChangedListener(textWatcher);
        binding.editTextLocation.addTextChangedListener(textWatcher);
        binding.editTextAboutMe.addTextChangedListener(textWatcher);
        binding.txtLol.addTextChangedListener(textWatcher);




        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);

            }
        });


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nickname = binding.editTextNickname.getText().toString();
                String edad = binding.editTextAge.getText().toString();
                String localizacion = binding.editTextLocation.getText().toString();
                String sobreMi = binding.editTextAboutMe.getText().toString();
                String genero = "";
                String lolAcc = binding.txtLol.getText().toString();
                int selectedGenderId = binding.radioGroupGender.getCheckedRadioButtonId();
                switch (selectedGenderId) {
                    case R.id.radio_button_male:
                        genero = "male";
                        break;
                    case R.id.radio_button_female:
                        genero = "female";
                        break;
                    case R.id.radio_button_other:
                        genero = "other";
                        break;
                }



                conexiones.getJugador(lolAcc).enqueue(new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                        if (response.isSuccessful()) {
                            respuesta = response.body();
                            Toast.makeText(RegistrarCuentaActivity.this, "Cuenta Encontrada", Toast.LENGTH_SHORT).show();
                            comprobador().show();

                        }  else {
                            Toast.makeText(RegistrarCuentaActivity.this, "Cuenta No Encontrada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Respuesta> call, Throwable t) {
                        Toast.makeText(RegistrarCuentaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                User user = new User(nickname, edad, genero, localizacion, sobreMi, imageURL, lolAcc, uid);

                comprobarCorrecto(user);

            }
        });
    }

    private void comprobarParametros() {
        String nickname = binding.editTextNickname.getText().toString();
        String edad = binding.editTextAge.getText().toString();
        String localizacion = binding.editTextLocation.getText().toString();
        String sobreMi = binding.editTextAboutMe.getText().toString();
        String lolAcc = binding.txtLol.getText().toString();
        int selectedGenderId = binding.radioGroupGender.getCheckedRadioButtonId();
        if (!nickname.isEmpty() && !edad.isEmpty() && !localizacion.isEmpty() && !sobreMi.isEmpty() && !lolAcc.isEmpty() && selectedGenderId != -1 && !(imageURL == null)) {
            binding.btnRegister.setEnabled(true);
        }else {
            binding.btnRegister.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Picasso.get().load(uri)
                    .resize(200, 200)
                    .centerCrop()
                    .into(binding.imageView2);
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
                            comprobarParametros();
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


    private void comprobarCorrecto(User user) {
        if (correcto) {
            refUser.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegistrarCuentaActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrarCuentaActivity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
            nuevaCuenta.setValue(user.getLolAcc());

        }
    }

    private AlertDialog comprobador() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarCuentaActivity.this);
        builder.setTitle("Autenticación");
        builder.setCancelable(true);
        builder.setMessage("Cambia tu icono de perfil de League of Legends al siguiente:");
        builder.setIcon(R.drawable.i0);
        builder.setPositiveButton("Comprobar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (respuesta.getProfileIconId() == 0) {
                    correcto = true;
                    Toast.makeText(RegistrarCuentaActivity.this, "Cuenta Verificada, ya puedes registrarte", Toast.LENGTH_SHORT).show();
                    comprobador().dismiss();
                } else {
                    Toast.makeText(RegistrarCuentaActivity.this, "No has cambiado tu icono de perfil", Toast.LENGTH_SHORT).show();
                    correcto = false;
                }
            }
        });
        return builder.create();
    }

    private void comprobarCuentalOL(){
        refLolAcc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getValue().equals(binding.txtLol.getText().toString())) {
                            Toast.makeText(RegistrarCuentaActivity.this, "Cuenta de lol ya registrada", Toast.LENGTH_SHORT).show();
                        } else {
                            comprobador().show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }




}


