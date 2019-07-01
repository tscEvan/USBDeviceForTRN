package com.example.usbdevicefortrn.ownDevice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usbdevicefortrn.R;

import java.util.ArrayList;

public class OwnDeviceRecyclerAdapter extends RecyclerView.Adapter<OwnDeviceRecyclerAdapter.DeviceViewHolder>{
    private static final String TAG = OwnDeviceRecyclerAdapter.class.getSimpleName();

    Context context;
    ArrayList<OwnDeviceBean> arrayList;
    private final int USB = 1;//R.integer.device_usb
    private final int DOOR = 2;//R.integer.device_door

    public OwnDeviceRecyclerAdapter(Context context, ArrayList<OwnDeviceBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_own_device,viewGroup,false);
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
                Snackbar.make(v,"SHARE"+i,Snackbar.LENGTH_LONG).show();
            }
        });

        deviceViewHolder.btUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"UNLOCK"+i,Snackbar.LENGTH_LONG).show();
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
