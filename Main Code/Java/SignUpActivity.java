package com.example.emalpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.emalpass.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.signAlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

        binding.signBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                if (binding.signName.getText().toString().isEmpty()){
                    binding.signName.setError("Name is required");
                    return;
                }
                else if (binding.signEmail.getText().toString().isEmpty()){
                    binding.signEmail.setError("Email is required");
                    return;
                }
                else if (binding.signPassword.getText().toString().isEmpty()){
                    binding.signPassword.setError("Password is must");
                    return;
                }
                else if (binding.signPassword.getText().toString().length() < 6){
                    binding.signPassword.setError("Password must be above 6 char");
                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.cardViewsign.setVisibility(View.GONE);
                        binding.signAnimation.setVisibility(View.VISIBLE);
                        binding.signLoading.setVisibility(View.VISIBLE);
                        binding.signConstrain.setBackgroundColor(R.color.teal_200);
                    }
                }, 500);


                auth.createUserWithEmailAndPassword(binding.signEmail.getText().toString(), binding.signPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Bio bio = new Bio(binding.signName.getText().toString(),
                                            binding.signEmail.getText().toString(),
                                            binding.signPassword.getText().toString());

                            database.getReference().child("users").child("bio").child(binding.signName.getText().toString()).setValue(bio);

                            binding.signAnimation.setAnimation(R.raw.success);
                            binding.signAnimation.playAnimation();
                            binding.signAnimation.setRepeatCount(0);
                            binding.signLoading.setText("Successfully Account created");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                }
                            }, 2000);
                        }
                        else {
                            binding.signAnimation.setAnimation(R.raw.failed);
                            binding.signAnimation.playAnimation();
                            binding.signAnimation.setRepeatCount(0);
                            binding.signLoading.setText("Account creating failed");
                        }
                    }
                });


            }
        });

    }
}