package com.app.appfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.model.FAQ;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private Context context;
    private List<FAQ> faqList;

    public FAQAdapter(Context context, List<FAQ> faqList) {
        this.context = context;
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQ faq = faqList.get(position);
        holder.tvQuestion.setText(faq.getQuestion());

        // Thêm sự kiện click vào mỗi mục FAQ
        holder.itemView.setOnClickListener(v -> {
            // Hiển thị một dialog thay vì Toast
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Hỗ trợ")
                    .setMessage("Vui lòng chat để được hỗ trợ thêm!")
                    .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss()) // Tùy chọn đóng dialog
                    .create()
                    .show();
        });
    }


    @Override
    public int getItemCount() {
        return faqList.size();
    }

    // Cập nhật danh sách câu hỏi
    public void updateFAQList(List<FAQ> newFAQList) {
        this.faqList = newFAQList; // Cập nhật danh sách mới
        notifyDataSetChanged();    // Thông báo RecyclerView làm mới
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.faqTitle);  // Ánh xạ đến TextView hiển thị câu hỏi
        }
    }
}
