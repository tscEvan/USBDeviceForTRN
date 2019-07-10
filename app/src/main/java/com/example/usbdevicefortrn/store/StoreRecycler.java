package com.example.usbdevicefortrn.store;

import android.content.Context;
import android.os.Handler;
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

public class StoreRecycler {
    private static final String TAG = StoreRecycler.class.getSimpleName();
    Context context;
    RecyclerView recyclerView;
    ArrayList<StoreForShowBean> arrayList = new ArrayList<>();

    public StoreRecycler(Context context, RecyclerView recyclerView, String firebasePath) {
        this.context = context;
        this.recyclerView = recyclerView;
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.recyclerView);
//
        FirebaseFirestore.getInstance().collection("products")
                .document("forShow")
                .collection(firebasePath)
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
    }

    private class StoreNewsViewAdapter extends RecyclerView.Adapter<StoreNewsViewAdapter.StoreNewsViewHolder> {
        @NonNull
        @Override
        public StoreNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_store,viewGroup,false);
            return new StoreNewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final StoreNewsViewHolder storeViewHolder, int i) {
            final StoreForShowBean data = arrayList.get(i);
            storeViewHolder.txTitle.setText(data.getTitle());
            storeViewHolder.txDepiction.setText(data.getDepiction());
            new Handler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(data.getImgUrl()).placeholder(R.drawable.loading).into(storeViewHolder.image);
                    }
                }
            );
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
