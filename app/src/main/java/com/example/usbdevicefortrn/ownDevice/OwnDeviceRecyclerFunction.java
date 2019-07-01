package com.example.usbdevicefortrn.ownDevice;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class OwnDeviceRecyclerFunction {
    private final String TAG = OwnDeviceRecyclerFunction.class.getSimpleName();
    Context context;
    ArrayList<OwnDeviceBean> arrayList = new ArrayList<>();
    RecyclerView recyclerView;

    public OwnDeviceRecyclerFunction(Context context, RecyclerView recyclerView, String uid) {
        this.context = context;
        this.recyclerView = recyclerView;
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .collection("own")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        Log.d(TAG, "onEvent: work");
                        if (e != null) {
                            Log.d(TAG, e.getMessage() +", getCode: " + e.getCode());
                            return;
                        }
                        arrayList = new ArrayList<>();
                        if (queryDocumentSnapshots.getDocuments().size()!=0) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                OwnDeviceBean data = documentSnapshot.toObject(OwnDeviceBean.class);
                                arrayList.add(data);
                            }
                            Log.d(TAG, "onEvent: " + queryDocumentSnapshots.getDocuments());
                        }else{
                            Log.d(TAG, "onEvent: null");
                        }
                        upData();
                    }
                });
    }

    private void upData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        OwnDeviceRecyclerAdapter adapter = new OwnDeviceRecyclerAdapter(context, arrayList);
        recyclerView.setAdapter(adapter);
    }
}
