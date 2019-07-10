package com.example.usbdevicefortrn.ownDevice;

import android.bluetooth.BluetoothA2dp;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usbdevicefortrn.R;
import com.example.usbdevicefortrn.chaos.BluetoothSender;
import com.example.usbdevicefortrn.chaos.HenonMap;
import com.example.usbdevicefortrn.chaos.TRNFunction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class OwnDeviceRecyclerAdapter extends RecyclerView.Adapter<OwnDeviceRecyclerAdapter.DeviceViewHolder> {
    private static final String TAG = OwnDeviceRecyclerAdapter.class.getSimpleName();

    Context context;
    ArrayList<OwnDeviceBean> arrayList;
    private final int USB = 1;//R.integer.device_usb
    private final int DOOR = 2;//R.integer.device_door
    private BluetoothSender bluetoothSender = new BluetoothSender();
    HenonMap henonMap = new HenonMap();

    public OwnDeviceRecyclerAdapter(Context context, ArrayList<OwnDeviceBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_own_device, viewGroup, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceViewHolder deviceViewHolder, final int i) {
        deviceViewHolder.txName.setText(arrayList.get(i).getDeviceName());
        switch (arrayList.get(i).getType()) {
            case USB:
                deviceViewHolder.image.setImageResource(R.drawable.pendrive);
                break;
            case DOOR:
                deviceViewHolder.image.setImageResource(R.drawable.door);
                break;
        }
        deviceViewHolder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceViewHolder.btLayout.getVisibility() == View.GONE) {
                    deviceViewHolder.btLayout.setVisibility(View.VISIBLE);
                } else {
                    deviceViewHolder.btLayout.setVisibility(View.GONE);
                }
            }
        });
        deviceViewHolder.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(i).getPermission() == v.getResources().getInteger(R.integer.permission_master)) {
                    new AlertDialog.Builder(context).setMessage("share").setPositiveButton("OK", null).show();
                } else {
                    new AlertDialog.Builder(context).setTitle("權限不足").setMessage("只有裝置擁有者才能使用分享功能").setPositiveButton("OK", null).show();
                }
            }
        });

        deviceViewHolder.btUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "wait load key on cloud..", Snackbar.LENGTH_LONG).show();
                getDeviceKey(v, arrayList.get(i).getDeviceId());
            }
        });
    }

    private void getDeviceKey(final View v, String deviceId) {
        FirebaseFirestore.getInstance()
                .collection("keyBase")
                .document("usb")
                .collection("info")
                .whereEqualTo("deviceId", deviceId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Snackbar.make(v, "creating connect thread..", Snackbar.LENGTH_LONG).show();
                                DevicesKeyBaseBean data = documentSnapshot.toObject(DevicesKeyBaseBean.class);
                                //connect
                                BluetoothSender.ConnectedThread btSocketThread = bluetoothSender.connect(data.macAddress, data.getVersion(), data.getChaosKey());
                                try {
                                    if (btSocketThread != null) {
                                        btSocketThread.setHenonMap(henonMap.getInstance());
                                        btSocketThread.start();
                                        Snackbar.make(v, "connecting..", Snackbar.LENGTH_LONG).show();
                                    }else {
                                        Snackbar.make(v, "fail, please restart", Snackbar.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Snackbar.make(v, "fail, please restart", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txName;
        ConstraintLayout infoLayout, btLayout;
        Button btShare;
        Button btUnLock;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.own_device_photo);
            txName = itemView.findViewById(R.id.own_device_name);
            infoLayout = itemView.findViewById(R.id.constraint_layout_info);
            btLayout = itemView.findViewById(R.id.constraint_layout_button);
            btShare = itemView.findViewById(R.id.own_device_bt_share);
            btUnLock = itemView.findViewById(R.id.own_device_bt_unlock);
        }
    }
}
