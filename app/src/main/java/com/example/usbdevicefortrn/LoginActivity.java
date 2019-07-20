package com.example.usbdevicefortrn;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.usbdevicefortrn.dialog.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText edAccount;
    private EditText edPasswd;
    private LoadingDialog loadingDialog;
    private TextInputLayout txLayoutAccount;
    private TextInputLayout txLayoutPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    setResult(RESULT_OK);
                    Intent mainPage = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainPage);
                    LoginActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);
                }
            }
        });

        edAccount = findViewById(R.id.ed_account);
        txLayoutAccount = findViewById(R.id.inputLayoutAccount);
        txLayoutPasswd = findViewById(R.id.inputLayoutPasswd);
        edPasswd = findViewById(R.id.ed_passwd);
        loadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "登入中",false);
    }

    public void login(final View view){
        loadingDialog.show();
        boolean inputState=true;
        View foucsView = null;
        String account = edAccount.getText().toString();
        String passwd = edPasswd.getText().toString();
        if (!Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{6,12}$").matcher(passwd).matches()) {
            txLayoutPasswd.setError("請使用6~12位中英文組合");
            foucsView = edPasswd;
            inputState = false;
        }else{
            txLayoutPasswd.setError(null);
        }

        if (!Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.+[a-zA-Z]{2,4}$").matcher(account).matches()) {
            txLayoutAccount.setError("請輸入正確的Email");
            foucsView = edAccount;
            inputState = false;
        }else{
            txLayoutAccount.setError(null);
        }
        if (inputState) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(account, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(view, "Login Successful", Snackbar.LENGTH_LONG).show();
                    }
                    loadingDialog.dismiss();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登入失敗")
                            .setMessage("請確認輸入資料是否正確")
                            .setPositiveButton("好",null).show();
                    loadingDialog.dismiss();
                }
            });
        }else {
            foucsView.requestFocus();
            loadingDialog.dismiss();
        }
    }

    public void register(View view){
        Intent registerPage = new Intent(this,RegisterActivity.class);
        startActivity(registerPage);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);
    }
}
