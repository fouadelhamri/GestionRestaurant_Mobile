package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.restaurantapp.authentification.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        ref = getSharedPreferences("myapp", MODE_PRIVATE);
    }
    private SharedPreferences ref;

    @OnClick(R.id.ready_btn)
    void ready_clicked(){
        boolean already_logged=ref.getBoolean("isLogged",false);

        if(already_logged){
            Intent home =new Intent(this, HomeActivity.class);
            startActivity(home);
            finish();
        }else{
            Intent loginactivity =new Intent(this, SignInActivity.class);
            startActivity(loginactivity);
            finish();
        }

        }

}