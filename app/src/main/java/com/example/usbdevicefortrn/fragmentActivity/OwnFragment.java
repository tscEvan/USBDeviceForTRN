package com.example.usbdevicefortrn.fragmentActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usbdevicefortrn.R;
import com.example.usbdevicefortrn.ownDevice.OwnDeviceRecyclerFunction;
import com.google.firebase.auth.FirebaseAuth;

public class OwnFragment extends Fragment {
    private static final String TAG = OwnFragment.class.getSimpleName();
    private static OwnFragment instance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.activity_own,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_main);
        OwnDeviceRecyclerFunction recyclerFunction = new OwnDeviceRecyclerFunction(this.getActivity(), recyclerView, FirebaseAuth.getInstance().getUid());
    }

//    public static OwnFragment getInstance() {
//        if (instance == null) {
//            instance = new OwnFragment();
//        }
//        return instance;
//    }
}
