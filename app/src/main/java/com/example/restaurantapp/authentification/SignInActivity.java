package com.example.restaurantapp.authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.HomeActivity;
import com.example.restaurantapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = getSharedPreferences("myapp",MODE_PRIVATE);
        boolean islogged=ref.getBoolean("isLogged",false);
        if(islogged){
            Intent home =new Intent(this, HomeActivity.class);
            startActivity(home);
            finish();
        }
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }
    SharedPreferences ref;
    @BindView(R.id.rememberme_btn)
    CheckBox rememberButton;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }
    @OnClick(R.id.signin_btn)
    void signin_clicked(){
        String mail=username.getText().toString();
        String pass=password.getText().toString();
        boolean remember_me=rememberButton.isChecked();

        if(mail.equals("fouadchahd@gmail.com") && pass.equals("root")){
            ref.edit().putString("username",mail).apply();
            ref.edit().putString("password",pass).apply();
           if(remember_me) ref.edit().putBoolean("isLogged",true).apply();
            int unicode=0x1F44B;///=hello emoji
            String mymoji = getEmoji(unicode);
            Toast.makeText(this, "Welcome"+mymoji, Toast.LENGTH_SHORT).show();
            Intent home=new Intent(this,HomeActivity.class);
            startActivity(home);
            finish();
        }else{
            Toast.makeText(this, "Invalid information", Toast.LENGTH_SHORT).show();
        }
    }
}