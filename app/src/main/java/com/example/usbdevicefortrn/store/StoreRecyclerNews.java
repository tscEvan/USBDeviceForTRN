package com.example.usbdevicefortrn.store;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usbdevicefortrn.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class StoreRecyclerNews {
    private static final String TAG = StoreRecyclerNews.class.getSimpleName();
    Context context;
    RecyclerView recyclerView;
    ArrayList<StoreForShowBean> arrayList = new ArrayList<>();

    public StoreRecyclerNews(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
//
//        FirebaseFirestore.getInstance().collection("products").document("forSell").collection("usb").add(new StoreForSellBean(false,"123","123"));
//        FirebaseFirestore.getInstance().collection("products").document("forSell").collection("usb").add(new StoreForSellBean(false,"1234","1234"));
//        FirebaseFirestore.getInstance().collection("products").document("forSell").collection("usb").add(new StoreForSellBean(false,"12345","12345"));
//
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("news").add(new StoreForShowBean("混沌加密隨身碟","展示文字","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v0.jpg?alt=media&token=ea1ffb73-354e-46f9-ba4c-b6f56008073c"));
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("news").add(new StoreForShowBean("混沌電子門鎖","1234","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v1.jpg?alt=media&token=f2d4ad58-ac74-4cd6-8837-ddce360ca3d7"));
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("news").add(new StoreForShowBean("Modbus微電網","12345","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v2.jpg?alt=media&token=17e91fec-60fb-4439-9469-fde952c2fd7f"));

        FirebaseFirestore.getInstance().collection("products")
                .document("forShow")
                .collection("usb")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    arrayList.clear();
                    if (queryDocumentSnapshots.getDocuments().size() !=0) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            StoreForShowBean data = documentSnapshot.toObject(StoreForShowBean.class);
                            arrayList.add(data);
                        }
                    }
                    upData();
                }
        });
    }

    private void upData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2, GridLayout.HORIZONTAL,false));
        StoreNewsViewAdapter adapter = new StoreNewsViewAdapter();
        recyclerView.setAdapter(adapter);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private class StoreNewsViewAdapter extends RecyclerView.Adapter<StoreNewsViewAdapter.StoreNewsViewHolder> {
        @NonNull
        @Override
        public StoreNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_store,viewGroup,false);
            return new StoreNewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoreNewsViewHolder storeViewHolder, int i) {
            StoreForShowBean data = arrayList.get(i);
            storeViewHolder.txTitle.setText(data.getTitle());
            storeViewHolder.txDepiction.setText(data.getDepiction());
            Picasso.get().load(data.getImgUrl()).placeholder(R.drawable.loading).into(storeViewHolder.image);
//            storeViewHolder.image.setImageResource();
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class StoreNewsViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView txTitle;
            TextView txDepiction;
            public StoreNewsViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.store_image);
                txTitle = itemView.findViewById(R.id.store_title);
                txDepiction = itemView.findViewById(R.id.store_depiction);
            }
        }
    }
}
