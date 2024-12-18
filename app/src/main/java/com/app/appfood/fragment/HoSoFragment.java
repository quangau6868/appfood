package com.app.appfood.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.appfood.R;
import com.app.appfood.activity.ChinhSachActivity;
import com.app.appfood.activity.TrungtamTrogiupActivity;
import com.app.appfood.activity.ForgotPasswordActivity;
import com.app.appfood.activity.LoginActivity;
import com.app.appfood.activity.PayActivity;
import com.app.appfood.activity.TroGiupActivity;

public class HoSoFragment extends Fragment {

    private View mView;
    private TextView text_user, text_gmail;
    private Button hoadon, logout, button_update_password, button_danh_gia,
            button_chinh_sach, button_facebook, button_chia_se, button_ve_toi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_ho_so, container, false);

        // Khởi tạo các thành phần
        text_user = mView.findViewById(R.id.text_user);
        text_gmail = mView.findViewById(R.id.text_email);
        hoadon = mView.findViewById(R.id.button_hoa_don);
        logout = mView.findViewById(R.id.button_logout);
        button_danh_gia = mView.findViewById(R.id.button_danh_gia);
        button_update_password = mView.findViewById(R.id.button_update_password);
        button_chinh_sach = mView.findViewById(R.id.button_chinh_sach);
        button_facebook = mView.findViewById(R.id.button_facebook);
        button_chia_se = mView.findViewById(R.id.button_chia_se);
        button_ve_toi = mView.findViewById(R.id.button_ve_toi);

        // Hiển thị username và email từ Intent
        if (getActivity() != null) {
            String username = getActivity().getIntent().getStringExtra("username");
            String email = getActivity().getIntent().getStringExtra("email");

            if (username != null) {
                text_user.setText(username);
            }
            if (email != null) {
                text_gmail.setText(email);
            }
        }

        // Xử lý sự kiện cho các nút
        button_update_password.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
            startActivity(intent);
        });

        button_danh_gia.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TroGiupActivity.class);
            startActivity(intent);
        });

        hoadon.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PayActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        button_chinh_sach.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChinhSachActivity.class);
            startActivity(intent);
        });

        button_facebook.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61570138672846"));
            startActivity(browserIntent);
        });

        button_chia_se.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "AppFood");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Tải ứng dụng AppFood tại: https://example.com");
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua:"));
        });

        button_ve_toi.setOnClickListener(view -> {
            showAboutAppDialog();
        });

        return mView;
    }

    private void showAboutAppDialog() {
        // Tạo dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_about_app, null);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();

        // Ánh xạ các view trong layout dialog
        TextView title = dialogView.findViewById(R.id.dialogTitle);
        TextView introduction = dialogView.findViewById(R.id.dialogIntroduction);
        TextView ownerInfo = dialogView.findViewById(R.id.dialogOwnerInfo);
        TextView supportEmail = dialogView.findViewById(R.id.dialogSupportEmail);
        Button btnUnderstand = dialogView.findViewById(R.id.dialogButtonUnderstand);

        // Thiết lập nội dung
        title.setText("AppFood");
        introduction.setText("AppFood là ứng dụng cung cấp giải pháp đặt món ăn nhanh chóng và tiện lợi, giúp người dùng tiết kiệm thời gian và tận hưởng các món ăn ngon.");
        ownerInfo.setText("Chủ sở hữu: Công ty TNHH AppFood Việt Nam");
        supportEmail.setText("Email hỗ trợ: support@appfood.vn");

        // Xử lý khi nhấn nút "Đã hiểu"
        btnUnderstand.setOnClickListener(v -> dialog.dismiss());

        // Hiển thị dialog
        dialog.setCancelable(false); // Không cho phép thoát dialog khi nhấn ra ngoài
        dialog.show();
    }

}