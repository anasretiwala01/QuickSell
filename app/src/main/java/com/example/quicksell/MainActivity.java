package com.example.quicksell;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.quicksell.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



       // sharedPreferences2 = getSharedPreferences("QUICKSELL",MODE_PRIVATE);


        super.onCreate(savedInstanceState);

        binding =  ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());



        firebaseAuth = FirebaseAuth.getInstance();

        //cleanuser();
        if(firebaseAuth.getCurrentUser() == null){

            startLoginOptions();

        }
        showHomeFragement();



        //String current_user = sharedPreferences2.getString("user",null);

        //if(sharedPreferences2.contains("user")){

        //}





        binding.bottomNv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.menu_home){
                    showHomeFragement();
                    return true;
                }
                else if(itemId == R.id.menu_chats){
                    if(firebaseAuth.getCurrentUser() == null){
                        Utils.toast(MainActivity.this,"Login Required");
                        startLoginOptions();
                        return false;
                    }
                    else{
                        showChatsFragement();
                        return true;
                    }

                }
                else if(itemId == R.id.menu_my_ads){
                    if(firebaseAuth.getCurrentUser() == null){
                        Utils.toast(MainActivity.this,"Login Required");
                        startLoginOptions();
                        return false;
                    }
                    else{
                        showMyAdsFragement();
                        return true;
                    }

                }
                else if(itemId == R.id.menu_account){
                    if(firebaseAuth.getCurrentUser() == null){
                        Utils.toast(MainActivity.this,"Login Required");
                        startLoginOptions();
                        return false;
                    }
                    else{
                        showAccountFragement();
                        return true;
                    }

                }
                else{
                    return false;
                }

            }
        });




    }


    private void cleanuser(){
        //editor = sharedPreferences2.edit();
        //editor.clear();
        //editor.commit();
        firebaseAuth.getInstance().signOut();
    }
    private void showHomeFragement() {
        binding.toolbarTitleTv.setText("Home");
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),fragment,"HomeFragment");
        fragmentTransaction.commit();
    }
    private void showChatsFragement() {
        binding.toolbarTitleTv.setText("Chats");
        ChatsFragment fragment = new ChatsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),fragment,"ChatsFragment");
        fragmentTransaction.commit();
    }
    private void showMyAdsFragement() {
        binding.toolbarTitleTv.setText("My Ads");
        MyAdsFragment fragment = new MyAdsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),fragment,"MyAdsFragment");
        fragmentTransaction.commit();
    }
    private void showAccountFragement() {
        binding.toolbarTitleTv.setText("Account");
        AccountFragment fragment = new AccountFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),fragment,"AccountFragment");
        fragmentTransaction.commit();
    }

    private void startLoginOptions(){
        startActivity(new Intent(this,LoginOptionsActivity.class));
    }
}