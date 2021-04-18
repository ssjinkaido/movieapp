package com.example.movieapp.ui;

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
import com.example.movieapp.databinding.ActivityLoginBinding;
import com.example.movieapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private Button mBtnRegister;
    private Button mBtnForgotPassword;
    private ProgressBar mProgressBar;
    private LoginViewModel mLoginViewModel;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnForgotPassword = (Button) findViewById(R.id.btn_reset_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvError = (TextView) findViewById(R.id.failed_identity);
        mLoginViewModel = new ViewModelProvider(LoginActivity.this).get(LoginViewModel.class);
        binding.setLoginViewModel(mLoginViewModel);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mLoginViewModel.onLoginClick();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        authenticationHandling();
        observeLogin();

    }

    private void observeLogin() {
        mLoginViewModel.getIsSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessful) {
                if (isSuccessful) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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


    private void authenticationHandling() {
        if (mLoginViewModel.getIsLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}