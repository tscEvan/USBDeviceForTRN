package com.example.usbdevicefortrn.menu;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usbdevicefortrn.AddDeviceActivity;
import com.example.usbdevicefortrn.R;
import com.example.usbdevicefortrn.ShareActivity;
import com.example.usbdevicefortrn.UserInfoActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuRecycler {
    ArrayList<MenuBean> arrayList = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;

    public MenuRecycler(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;

        arrayList.add(new MenuBean(R.drawable.resume, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),"查看個人資料"));
        arrayList.add(new MenuBean(R.drawable.link, "新增裝置",null));
        arrayList.add(new MenuBean(R.drawable.share,"分享裝置",null));
        arrayList.add(new MenuBean(R.drawable.logout,"登出用戶",null));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        MenuViewAdapter adapter = new MenuViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class MenuViewAdapter extends RecyclerView.Adapter<MenuViewAdapter.MenuViewHolder> {
        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_menu,viewGroup,false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, int i) {
            final MenuBean data = arrayList.get(i);
            menuViewHolder.image.setImageResource(data.getImage());
            menuViewHolder.title.setText(data.title);
            if (data.getDepiction() != null) {
                menuViewHolder.depiction.setText(data.depiction);
            }else{
                menuViewHolder.depiction.setVisibility(View.GONE);
            }
            menuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (data.image) {
                        case R.drawable.resume:
                            context.startActivity(new Intent(context, UserInfoActivity.class));
                            break;
                        case R.drawable.link:
                            context.startActivity(new Intent(context, AddDeviceActivity.class));
                            break;
                        case R.drawable.share:
                            context.startActivity(new Intent(context, ShareActivity.class));
                            break;
                        case R.drawable.logout:
                            FirebaseAuth.getInstance().signOut();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MenuViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title, depiction;
            public MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.menu_image);
                title = itemView.findViewById(R.id.menu_title);
                depiction = itemView.findViewById(R.id.menu_depiction);
            }
        }
    }
}
