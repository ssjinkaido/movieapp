package com.example.movieapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.MainActivity;
import com.example.movieapp.R;
import com.example.movieapp.databinding.ActivityRegisterBinding;
import com.example.movieapp.viewmodel.LoginViewModel;
import com.example.movieapp.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private Button mBtnBackToLogin;
    private Button mBtnRegister;
    private ProgressBar mProgressBar;
    private RegisterViewModel mRegisterViewModel;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mBtnBackToLogin = (Button) findViewById(R.id.btn_back_to_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvError = (TextView) findViewById(R.id.register_failed_text);
        mRegisterViewModel = new ViewModelProvider(RegisterActivity.this).get(RegisterViewModel.class);
        binding.setRegisterViewModel(mRegisterViewModel);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                mRegisterViewModel.onRegisterClicked();
            }
        });
        mBtnBackToLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        observeRegister();
    }

    private void observeRegister() {
        mRegisterViewModel.getIsSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSuccessful) {
                if (isSuccessful) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}