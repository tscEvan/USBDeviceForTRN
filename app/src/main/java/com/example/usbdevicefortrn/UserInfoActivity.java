package com.example.usbdevicefortrn;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usbdevicefortrn.userInformation.UserInformationBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import javax.annotation.Nullable;

public class UserInfoActivity extends AppCompatActivity {

    private final String TAG = UserInformationBean.class.getSimpleName();
    private final int GET_PHOTO_RESULT = 20;
    private ImageView myPhoto;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView txName;
    private TextView txEmail;
    private ImageView myQRCode;
    private String uid;
    private ImageButton editMyName;
    private ImageButton editMyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        uid = FirebaseAuth.getInstance().getUid();
        //findView
        findView();
        //set user photo
        setUserPhoto();
        //set my share qrcode
        setMyShareQrcode(uid);


    }

    public void unableFun(View view){
        Snackbar.make(view,"該功能尚未開放",Snackbar.LENGTH_LONG).show();
    }

    private void findView() {
        txName = findViewById(R.id.tx_my_name);
        txEmail = findViewById(R.id.tx_my_email);
        myQRCode = findViewById(R.id.image_qrcode);
        myPhoto = findViewById(R.id.image_my_photo);
    }

    private void setMyShareQrcode(final String uid) {
        FirebaseFirestore.getInstance().collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                UserInformationBean data = documentSnapshot.toObject(UserInformationBean.class);
                Log.d(TAG, "onEvent: "+ data.getEmail()+" , "+data.getNickname());
                txName.setText(data.getNickname());
                txEmail.setText(data.getEmail());
                BarcodeEncoder encoder = new BarcodeEncoder();
                try {
                    Bitmap bit = encoder.encodeBitmap(uid, BarcodeFormat.QR_CODE,
                            200, 200);
                    myQRCode.setImageBitmap(bit);
                } catch (Exception e1) {
                }
            }
        });
    }

    private void setUserPhoto() {
        sharedPreferences = getSharedPreferences("IMAGE_FILE", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String UserImage = sharedPreferences.getString("USER_IMAGE", "noFile");
        if (!UserImage.equals("noFile")) {
            byte[] decodeByte = Base64.decode(UserImage, 0);
            myPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length));
        } else {
            Log.d("ImageFile", "Is no have old image");
        }
        //set click listener
        myPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setMessage("想要變更照片嗎?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                                Intent destIntent = Intent.createChooser(intent,null);
                                startActivityForResult(destIntent, GET_PHOTO_RESULT);
                            }
                        })
                        .setNeutralButton("否",null)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_PHOTO_RESULT:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            final Uri uri = data.getData();
                            // upload new photo
                            uploadNewPhoto(uri);
                            final ContentResolver cr = this.getContentResolver();
                            // change photo thread
                            changePhotoThread(uri, cr);
                        }
                        break;
                    case RESULT_CANCELED:
                        //在選擇時取消
                        new AlertDialog.Builder(UserInfoActivity.this).setMessage("已取消變更").show();
                        break;
                }
                break;
        }
    }

    private void changePhotoThread(final Uri uri, final ContentResolver cr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                    byte[] imageByte = baos.toByteArray();
                    String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
                    editor.putString("USER_IMAGE", imageEncoded);
                    editor.commit();
                    myPhoto.post(new Runnable() {
                        @Override
                        public void run() {
                            myPhoto.setImageBitmap(bitmap);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void uploadNewPhoto(Uri uri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageRef.child("users/" + uid + "/mugshot.jpg");
        riversRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult().getUploadSessionUri();

                    Log.d(TAG, "onComplete: "+ downloadUrl);
                    FirebaseFirestore.getInstance().collection("users").document(uid).update("photo",downloadUrl.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disable_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.close_activity) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
