package com.example.usbdevicefortrn.share;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usbdevicefortrn.R;
import com.example.usbdevicefortrn.ownDevice.OwnDeviceBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ShareDeviceRecyclerFunction {

    private final String TAG = ShareDeviceRecyclerFunction.class.getSimpleName();
    Context context;
    ArrayList<OwnDeviceBean> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    String shareToUid, shareToName;

    public ShareDeviceRecyclerFunction(Context context, RecyclerView recyclerView, String uid, String shareToUid, String shareToName) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.shareToUid = shareToUid;
        this.shareToName = shareToName;
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .collection("own")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, e.getMessage() + ", getCode: " + e.getCode());
                            return;
                        }
                        arrayList = new ArrayList<>();
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.getDocuments().size() != 0) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    OwnDeviceBean data = documentSnapshot.toObject(OwnDeviceBean.class);
                                    arrayList.add(data);
                                }
                                Log.d(TAG, "onEvent: " + queryDocumentSnapshots.getDocuments());
                                upData();
                            } else {
                                Log.d(TAG, "onEvent: null");
                            }
                        }
                    }
                });
    }

    private void upData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ShareDeviceRecyclerAdapter adapter = new ShareDeviceRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }


    public class ShareDeviceRecyclerAdapter extends RecyclerView.Adapter<ShareDeviceRecyclerAdapter.ShareDeviceViewHolder> {
        private final String TAG = ShareDeviceRecyclerAdapter.class.getSimpleName();
        private final int USB = 1;//R.integer.device_usb
        private final int DOOR = 2;//R.integer.device_door

        public ShareDeviceRecyclerAdapter() {
        }

        @NonNull
        @Override
        public ShareDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_own_device, viewGroup, false);
            return new ShareDeviceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ShareDeviceViewHolder shareDeviceViewHolder, final int i) {
            final OwnDeviceBean data = arrayList.get(i);
            shareDeviceViewHolder.txName.setText(data.getDeviceName());
            switch (data.getType()) {
                case USB:
                    shareDeviceViewHolder.image.setImageResource(R.drawable.pendrive);
                    break;
                case DOOR:
                    shareDeviceViewHolder.image.setImageResource(R.drawable.door);
                    break;
            }
            shareDeviceViewHolder.infoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //need add something check repeat share function
                    final AlertDialog.Builder checkDialog = new AlertDialog.Builder(context);
                    checkDialog.setMessage("確定要將「" + data.getDeviceName() + "」裝置使用權限分享給 " + shareToName + " 嗎?")
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    OwnDeviceBean newShareData = data;
                                    newShareData.setPermission(context.getResources().getInteger(R.integer.permission_client));
                                    FirebaseFirestore.getInstance()
                                            .collection("users")
                                            .document(shareToUid)
                                            .collection("own")
                                            .add(newShareData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                checkDialog.setMessage("分享完成，請確認").setPositiveButton("確定",null).show();
                                            } else {
                                                checkDialog.setMessage("分享失敗").setPositiveButton("確定",null).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ShareDeviceViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView txName;
            ConstraintLayout infoLayout;

            public ShareDeviceViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.own_device_photo);
                txName = itemView.findViewById(R.id.own_device_name);
                infoLayout = itemView.findViewById(R.id.constraint_layout_info);
            }
        }
    }
}
