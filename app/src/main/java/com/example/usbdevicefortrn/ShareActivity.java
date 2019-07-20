package com.example.usbdevicefortrn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usbdevicefortrn.dialog.LoadingDialog;
import com.example.usbdevicefortrn.share.ShareDeviceRecyclerFunction;
import com.example.usbdevicefortrn.userInformation.UserInformationBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = ShareActivity.class.getSimpleName();
    private TextInputLayout inputLayout;
    private EditText edSearch;
    private String uid;
    private ImageView userPhoto;
    private ConstraintLayout searchResultLayout;
    private TextView shareToName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        uid = FirebaseAuth.getInstance().getUid();
        inputLayout = findViewById(R.id.inputLayoutSearch);
        edSearch = findViewById(R.id.ed_search);
        userPhoto = findViewById(R.id.image_user_photo);
        shareToName = findViewById(R.id.search_share_to_name);
        searchResultLayout = findViewById(R.id.search_result_layout);
    }

    public void emailSearch(View view) {
        if(searchResultLayout.getVisibility()!=View.GONE) searchResultLayout.setVisibility(View.GONE);
        String edEmail = edSearch.getText().toString();
        if (!Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.+[a-zA-Z]{2,4}$").matcher(edEmail).matches()) {
            inputLayout.setError("請輸入正確的Email");
        } else {
            inputLayout.setError(null);
            final LoadingDialog loadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "搜尋中請稍後", false);
            loadingDialog.show();
            FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", edEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            final UserInformationBean searchUser = documentSnapshot.toObject(UserInformationBean.class);
                            try {
                                final File file = File.createTempFile("images", "jpg");
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReferenceFromUrl("gs://usbdevicefortrn.appspot.com/users").child(searchUser.getUid() + "/mugshot.jpg");
                                storageRef.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                            userPhoto.setImageBitmap(bitmap);
                                        }
                                        loadingDialog.dismiss();
                                        shareToName.setText(searchUser.getNickname());
                                        searchResultLayout.setVisibility(View.VISIBLE);
                                        Button btCheck = findViewById(R.id.search_check);
                                        btCheck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShareActivity.this);
                                                LayoutInflater layoutInflater = LayoutInflater.from(ShareActivity.this);
                                                View addNewView = layoutInflater.inflate(R.layout.recycler_view_layout, null);
                                                RecyclerView recyclerView = addNewView.findViewById(R.id.recycler);
                                                new ShareDeviceRecyclerFunction(ShareActivity.this,recyclerView,uid,searchUser.getUid(),searchUser.getNickname());
                                                alertDialog.setView(addNewView).show();
                                            }
                                        });
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    public void qrcode(View view) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (0 == requestCode && null != data && data.getExtras() != null) {
            final String result = data.getExtras().getString("la.droid.qr.result");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disable_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.close_activity) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
