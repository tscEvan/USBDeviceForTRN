package com.example.usbdevicefortrn.fragmentActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usbdevicefortrn.R;
import com.example.usbdevicefortrn.store.ProductBean;
import com.example.usbdevicefortrn.store.StoreRecycler;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_store,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Thread threadNews = new Thread(new Runnable() {
            @Override
            public void run() {
                StoreRecycler news = new StoreRecycler(getContext(), (RecyclerView) getActivity().findViewById(R.id.store_recycler_news), "news");
            }
        });
        Thread threadUsb = new Thread(new Runnable() {
            @Override
            public void run() {
                StoreRecycler usb = new StoreRecycler(getContext(), (RecyclerView) getActivity().findViewById(R.id.store_recycler_usb), "usb");
            }
        });
        threadNews.start();
        threadUsb.start();

//        addTestData();

    }

    private void addTestData() {
        CollectionReference products = FirebaseFirestore.getInstance().collection("products").document("forSell").collection("usb");
        CollectionReference keyBase = FirebaseFirestore.getInstance().collection("keyBase").document("usb").collection("info");
//        CollectionReference history = FirebaseFirestore.getInstance().collection("keyBase").document("usb").collection("history");
        for (int i = 0; i < 3; i++) {
            String productKey = FirebaseFirestore.getInstance().collection("pkey").document().getId();
            String deviceId = FirebaseFirestore.getInstance().collection("deviceId").document().getId();
            products.add(new ProductBean(false
                            ,productKey
                            ,deviceId
                    )
                );
//            keyBase.add(new DevicesKeyBaseBean(deviceId,"1",new Double[]{1.234, 12.34, 123.4 }));
//            history.add(new HIstoryBean(FieldValue.serverTimestamp(),));
        }

//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("news").add(new StoreForShowBean("混沌加密隨身碟","展示文字","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v2.jpg?alt=media&token=cbb7bdf3-426b-47f4-800b-01f3a8800743"));
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("news").add(new StoreForShowBean("混沌電子門鎖","1234","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v0.jpg?alt=media&token=a95e2be7-7cef-41b2-b331-2edb7d76fb69"));
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("news").add(new StoreForShowBean("Modbus微電網","12345","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v1.jpg?alt=media&token=be0db200-7752-417a-be93-58f945259fa8"));
//
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("usb").add(new StoreForShowBean("混沌加密隨身碟","展示文字","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v2.jpg?alt=media&token=cbb7bdf3-426b-47f4-800b-01f3a8800743"));
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("usb").add(new StoreForShowBean("混沌電子門鎖","1234","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v0.jpg?alt=media&token=a95e2be7-7cef-41b2-b331-2edb7d76fb69"));
//        FirebaseFirestore.getInstance().collection("products").document("forShow").collection("usb").add(new StoreForShowBean("加密隨身碟","12345","https://firebasestorage.googleapis.com/v0/b/usbdevicefortrn.appspot.com/o/image_show_usb_v1.jpg?alt=media&token=be0db200-7752-417a-be93-58f945259fa8"));
    }

}
