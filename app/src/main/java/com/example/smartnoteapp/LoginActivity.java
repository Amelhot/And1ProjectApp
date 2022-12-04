package com.example.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditTxt, passwordEditTxt;
    Button loginBn;
    ProgressBar progressB;
    TextView newAccountBtnTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditTxt = findViewById(R.id.emailEdit);
        passwordEditTxt = findViewById(R.id.passwordEdit);
        loginBn = findViewById(R.id.loginBtn);
        progressB = findViewById(R.id.progressBar);
        newAccountBtnTextView = findViewById(R.id.newAccountBtn);

        loginBn.setOnClickListener((v)-> userLogin());
        newAccountBtnTextView.setOnClickListener((v)->  startActivity(new Intent(LoginActivity.this,NewAccountActivity.class)));
    }

    void userLogin(){
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();


        boolean isValidated = validateData(email,password);
        if (!isValidated){
            return;
        }

        firebaseLogin(email,password);

    }

    void firebaseLogin(String email,String password){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //Successful login!
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //Main activity

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    }else{

                        Utility.showToast(LoginActivity.this,"E-mail not yet verified, Please check you mail for verification link");

                    }

                }else{
                    //Failed login :/
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());


                }

            }
        });



    }
    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressB.setVisibility(View.VISIBLE);
            loginBn.setVisibility(View.GONE);
        }else{
            progressB.setVisibility(View.GONE);
            loginBn.setVisibility(View.VISIBLE);
        }
    }




    boolean validateData(String email, String password){

        //used to see if user's data is correct

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTxt.setError("Invalid e-mail");
            return false;

        }
        if (password.length() < 6){
            passwordEditTxt.setError("Password is too short");
            return false;
        }

        return true;
    }

}