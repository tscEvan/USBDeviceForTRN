package com.example.usbdevicefortrn;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.example.usbdevicefortrn.ownDevice.OwnDeviceBean;
import com.example.usbdevicefortrn.store.ProductBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddDeviceActivity extends AppCompatActivity {

    private static final String TAG = AddDeviceActivity.class.getSimpleName();
    private RadioGroup type;
    private TextInputEditText inputKey;
    private TextInputLayout inputKeyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        type = findViewById(R.id.add_device_type);
        inputKey = findViewById(R.id.add_device_product_key_input);
        inputKeyLayout = findViewById(R.id.add_device_input_layout);
    }

    public void add(View view){
        boolean inputState = true;
        String key = null;
        if (type.getCheckedRadioButtonId() == -1) {
            inputState = false;
            new AlertDialog.Builder(this).setMessage("請選擇您購買的裝置類別").setPositiveButton("OK",null).show();
        }
        if (inputKey.getText()==null) {
            inputState = false;
            inputKeyLayout.setError("請輸入你的產品金鑰");
        }else {
            key = inputKey.getText().toString();
            if (key.length()!=20) {
                inputState=false;
                inputKeyLayout.setError("產品金鑰格式錯誤");
            }
        }
        if (inputState) {
            switch (type.getCheckedRadioButtonId()) {
                case R.id.add_device_type_usb:
                    search("usb", key);
                    break;
                case R.id.add_device_type_Door:
                    search("door", key);
                    break;
            }
        }
    }

    private void search(final String type, final String key){
        final CollectionReference reference = FirebaseFirestore.getInstance().collection("products").document("forSell").collection(type);
        reference.whereEqualTo("productKey",key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    ProductBean data = documentSnapshot.toObject(ProductBean.class);
                    reference.document(documentSnapshot.getId()).update("onUse",true);
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .collection("own")
                                    .add(new OwnDeviceBean(getResources().getInteger(R.integer.device_usb)
                                            ,data.getDeviceId()
                                            ,type
                                            ,getResources().getInteger(R.integer.permission_master))
                                    );
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
        if (item.getItemId() == R.id.close_activity) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
