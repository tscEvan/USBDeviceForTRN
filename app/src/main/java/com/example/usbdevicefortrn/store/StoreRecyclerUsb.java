package com.example.usbdevicefortrn.store;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class StoreRecyclerUsb {
    Context context;
    ArrayList<StoreForShowBean> arrayList = new ArrayList();

    public StoreRecyclerUsb(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    class StoreUsbViewAdapter extends RecyclerView.Adapter<StoreUsbViewAdapter.StoreUsbViewHolder> {
        @NonNull
        @Override
        public StoreUsbViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull StoreUsbViewHolder storeUsbViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class StoreUsbViewHolder extends RecyclerView.ViewHolder {
            public StoreUsbViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
