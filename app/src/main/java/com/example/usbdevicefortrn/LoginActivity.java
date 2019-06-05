package com.example.usbdevicefortrn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edAccount;
    private EditText edPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edAccount = findViewById(R.id.ed_account);
        edPasswd = findViewById(R.id.ed_passwd);
    }

    public void login(View view){
        String account = edAccount.getText().toString();
        String passwd = edPasswd.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(account, passwd);
    }

    public void quit(View view){
        finish();
    }
}
