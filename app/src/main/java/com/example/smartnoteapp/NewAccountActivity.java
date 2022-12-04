package com.example.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewAccountActivity extends AppCompatActivity {

    EditText emailEditTxt, passwordEditTxt,confirmPassEditTxt;
    Button signUpBn;
    ProgressBar progressB;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        emailEditTxt = findViewById(R.id.emailEdit);
        passwordEditTxt = findViewById(R.id.passwordEdit);
        confirmPassEditTxt = findViewById(R.id.confirmPasswordEdit);
        signUpBn = findViewById(R.id.signUpBtn);
        progressB = findViewById(R.id.progressBar);
        loginBtnTextView = findViewById(R.id.loginTextvBtn);


        signUpBn.setOnClickListener(v-> createAccount());

    }

    void createAccount(){
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();
        String confirmPassword = confirmPassEditTxt.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if (!isValidated){
            return;
        }

        createFireBaseAccount(email,password);
    }

    void createFireBaseAccount (String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(NewAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            //Account created!
                            Toast.makeText(NewAccountActivity.this, "Account created successfully! To verify, check your e-mail", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            //Not created

                            Toast.makeText(NewAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressB.setVisibility(View.VISIBLE);
            signUpBn.setVisibility(View.GONE);
        }else{
            progressB.setVisibility(View.GONE);
            signUpBn.setVisibility(View.VISIBLE);
        }
    }




    boolean validateData(String email,String password,String confirmPassword){

        //used to see if user's data is correct

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTxt.setError("Invalid e-mail");
            return false;

        }
        if (password.length() < 6){
            passwordEditTxt.setError("Password is too short");
            return false;
        }

        if (!password.equals(confirmPassword)){
            confirmPassEditTxt.setError("Passwords not matching!");
            return false;
        }
        return true;
    }

}