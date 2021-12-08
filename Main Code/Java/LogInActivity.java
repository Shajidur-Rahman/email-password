package com.example.emalpass;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.emalpass.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null){
//            startActivity(new Intent(LogInActivity.this, MainActivity.class));
//        }

        binding.logSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.logEmail.getText().toString().isEmpty()){
                    binding.logEmail.setError("Email is required");
                    return;
                }
                else if (binding.logPassword.getText().toString().isEmpty()){
                    binding.logPassword.setError("Password is must");
                    return;
                }
                else if (binding.logPassword.getText().toString().length() < 6){
                    binding.logPassword.setError("Password must be above 6 char");
                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.logTitleFirst.setVisibility(View.GONE);
                        binding.logCardView.setVisibility(View.GONE);
                        binding.logTitle.setVisibility(View.VISIBLE);
                        binding.logAnimation.setVisibility(View.VISIBLE);
                        binding.logLoading.setVisibility(View.VISIBLE);
                    }
                }, 500);
//                binding.logAnimation.setAnimation(R.raw.success);
//                binding.logAnimation.playAnimation();
//                binding.logAnimation.setRepeatCount(0);
//                binding.logLoading.setText("Success");

                auth.signInWithEmailAndPassword(binding.logEmail.getText().toString(),
                                                binding.logPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        binding.logAnimation.setAnimation(R.raw.success);
                        binding.logAnimation.playAnimation();
                        binding.logAnimation.setRepeatCount(0);
                        binding.logLoading.setText("Successfully loged in");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            }
                        }, 2000);
                    }
                });

            }
        });

        binding.logSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });

    }
}