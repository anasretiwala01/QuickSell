package com.example.quicksell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quicksell.databinding.ActivityLoginEmailBinding;
import com.example.quicksell.databinding.ActivityRegisterEmailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterEmailActivity extends AppCompatActivity {

    private ActivityRegisterEmailBinding binding;


    private ProgressDialog progressDialog;
    //private ProgressBar progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "REGISTER_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        progressDialog = new ProgressDialog(this);
        //progressDialog = new ProgressBar(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                //getOnBackPressedDispatcher().onBackPressed();
            }
        });
        binding.haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                //startActivity(new Intent(RegisterEmailActivity.this,LoginEmailActivity.class));
            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }
    private String email, password;
    private String cPassword;
    private void validateData(){
        email= binding.emailEt.getText().toString().trim();
        password= binding.passwordEt.getText().toString();
        cPassword=binding.cPasswordEt.getText().toString();
        Log.d(TAG,"validateData : Email : "+email);
        Log.d(TAG,"validateData : Password : "+password);
        Log.d(TAG,"validateData : CPassword : "+cPassword);

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Invalid Email");
            binding.emailEt.requestFocus();
        } else if (password.isEmpty()) {
            binding.passwordEt.setError("Enter Password");
            binding.emailEt.requestFocus();
        } else if (!password.equals(cPassword)) {
            binding.cPasswordEt.setError("Password doesn't match");
            binding.emailEt.requestFocus();
        }else{
            registerUser();
        }
    }

    private void registerUser(){
        progressDialog.setMessage("Signing Up");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG,"onSuccess: Register success.");
                updateUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"onFailure",e);
                Utils.toast(RegisterEmailActivity.this,"Failed due to "+e.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void updateUserInfo(){

        progressDialog.setMessage("Saving User Info");

        long timestamp = Utils.getTimestamp();
        String registerUserEmail = firebaseAuth.getCurrentUser().getEmail();
        String registerUserUid = firebaseAuth.getUid();

        HashMap<String, Object>hashMap = new HashMap<>();
        hashMap.put("name","");
        hashMap.put("phoneCode","");
        hashMap.put("phoneNumber","");
        hashMap.put("profileImageUrl","");
        hashMap.put("dob","");
        hashMap.put("userType","Email");
        hashMap.put("typingTo","");
        hashMap.put("timestamp",timestamp);
        hashMap.put("onlineStatus",true);
        hashMap.put("email",registerUserEmail);
        hashMap.put("uid",registerUserUid);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(registerUserUid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"OnSuccess: Info saved..");
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterEmailActivity.this,MainActivity.class));
                        finishAffinity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFaliure : ",e);
                        progressDialog.dismiss();
                        Utils.toast(RegisterEmailActivity.this,"Failed to save info due to "+e.getMessage());
                    }
                });

    }
}