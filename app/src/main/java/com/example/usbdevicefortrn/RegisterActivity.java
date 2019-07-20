package com.example.usbdevicefortrn;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.usbdevicefortrn.dialog.LoadingDialog;
import com.example.usbdevicefortrn.userInformation.UserInformationBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText edEmail;
    private EditText edPasswd;
    private EditText edCheckPw;
    private EditText edUserName;
    private Button btRegister;
    private CheckBox checkBox;
    LoadingDialog loadingDialog;
    private TextInputLayout txLayoutEmail;
    private TextInputLayout txLayoutPasswd;
    private TextInputLayout txLayoutCheckPw;
    private TextInputLayout txLayoutUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //  find view
        findView();
        loadingDialog = new LoadingDialog(this,R.style.LoadingDialog,"正在註冊中..",false);
    }

    private void findView() {
        txLayoutEmail = findViewById(R.id.inputLayoutRegisterEmail);
        txLayoutPasswd = findViewById(R.id.inputLayoutRegisterPasswd);
        txLayoutCheckPw = findViewById(R.id.inputLayoutRegisterCheckPw);
        txLayoutUserName = findViewById(R.id.inputLayoutRegisterUserName);
        edEmail = findViewById(R.id.edRegisterEmail);
        edPasswd = findViewById(R.id.edRegisterPasswd);
        edCheckPw = findViewById(R.id.edRegisterCheckPw);
        edUserName = findViewById(R.id.edRegisterUserName);
        btRegister = findViewById(R.id.btStartRegister);
        checkBox = findViewById(R.id.cbAgreeUse);
    }

    public void register(View view){
        if (!checkBox.isChecked()) {
            checkBox.setError("依照法規，我們需要您同意我們使用您的資料");
        }else{
            loadingDialog.show();
            View focusView = null;
            Boolean checkInput = true;

            final String userName = edUserName.getText().toString().trim();
            if (userName.equals("")) {
                txLayoutUserName.setError("暱稱不能為空");
                checkInput = false;
                focusView = edUserName;
            }else{
                txLayoutUserName.setError(null);
            }

            String passwd = edPasswd.getText().toString();
            String ckPasswd = edCheckPw.getText().toString();
            if (!ckPasswd.equals(passwd)) {
                txLayoutCheckPw.setError("與輸入的密碼不同");
                checkInput = false;
                focusView = edCheckPw;
            }else{
                txLayoutCheckPw.setError(null);
            }

            if (!Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{6,12}$").matcher(passwd).matches()) {
                txLayoutPasswd.setError("請輸入6~12位的中英文組合");
                checkInput = false;
                focusView = edPasswd;
            }else {
                txLayoutPasswd.setError(null);
            }


            final String email = edEmail.getText().toString();
            if (!Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.+[a-zA-Z]{2,4}$").matcher(email).matches()) {
                txLayoutEmail.setError("請輸入正確的Email");
                checkInput = false;
                focusView = edEmail;
            }else {
                txLayoutEmail.setError(null);
            }

            if (checkInput) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = task.getResult().getUser();
                            final String userUid = user.getUid();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
                            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .document(userUid)
                                                .set(new UserInformationBean(userName,userUid,email))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            loadingDialog.dismiss();
                                                            finish();
                                                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    user.delete();
                                                    loadingDialog.dismiss();
                                                    new AlertDialog.Builder(RegisterActivity.this)
                                                            .setTitle("註冊失敗")
                                                            .setMessage(e.getMessage())
                                                            .setPositiveButton("好",null).show();
                                                }
                                            });
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    loadingDialog.dismiss();
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("註冊失敗").setMessage(e.getMessage()).show();
                    }
                });
            } else {
                focusView.requestFocus();
                loadingDialog.dismiss();
            }
        }
    }
}
