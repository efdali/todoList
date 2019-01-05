package com.efdalincesu.todolist.Ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.efdalincesu.todolist.Model.Result;
import com.efdalincesu.todolist.Model.User;
import com.efdalincesu.todolist.R;
import com.efdalincesu.todolist.RestApi.ManagerAll;
import com.efdalincesu.todolist.Utils.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextInputLayout emailLayout,sifreLayout;
    TextInputEditText emailText,sifreText;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initViews();


    }

    public void initViews(){


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Giriş Yap");


        emailLayout=findViewById(R.id.emailLayout);
        sifreLayout=findViewById(R.id.sifreLayout);
        emailText=findViewById(R.id.emailText);
        sifreText=findViewById(R.id.sifreText);
        rememberMe=findViewById(R.id.rememberMe);

    }

    public void sign(View view){

        emailLayout.setError(null);
        sifreLayout.setError(null);

        final String email=emailText.getText().toString();
        String sifre=sifreText.getText().toString();
        boolean remember=rememberMe.isChecked();

        View focusView=null;
        boolean cancel=false;


        if (TextUtils.isEmpty(email) || !validateEmail(email)){
            emailLayout.setError("Lütfen doğru mail giriniz.");
            focusView=emailText;
            cancel=true;
        }else if (TextUtils.isEmpty(sifre) || !validateSifre(sifre)){
            sifreLayout.setError("Lütfen 6 karakterden uzun giriniz.");
            focusView=emailText;
            cancel=true;
        }

        if (cancel)
            focusView.requestFocus();
        else{

            Call<Result> call=ManagerAll.getInstance().sign(email,sifre);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    String result=response.body().getResult();
                    if (!result.equals("no")){
                        Toast.makeText(getApplicationContext(),"Giriş Başarılı",Toast.LENGTH_LONG).show();
                        Auth.init(getApplicationContext()).setLogin(true,new User(result,email));
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.d("eklendi hata",t.getLocalizedMessage());
                }
            });

        }

    }

    public void forgetPass(View view){

    }

    public void register(View view){

    }

    public boolean validateEmail(String email){
        return email.contains("@");
    }

    public boolean validateSifre(String pass){
        return pass.length()>6;
    }


}
