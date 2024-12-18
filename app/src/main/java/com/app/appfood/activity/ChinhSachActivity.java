package com.app.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.app.appfood.R;

public class ChinhSachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sach);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Chính sách bảo mật
        Button btnBaoMat = findViewById(R.id.button_chinh_sach_bao_mat);
        btnBaoMat.setOnClickListener(v -> openDetailActivity(
                "Chính sách bảo mật",
                "Đây là nội dung của chính sách bảo mật. Mọi thông tin cá nhân sẽ được bảo vệ nghiêm ngặt. " +
                        "Chúng tôi cam kết không chia sẻ hoặc bán thông tin cá nhân của bạn cho bất kỳ bên thứ ba nào nếu không có sự đồng ý rõ ràng từ bạn. " +
                        "Dữ liệu của bạn sẽ được mã hóa và lưu trữ trong hệ thống bảo mật cao, đảm bảo tránh khỏi nguy cơ bị truy cập trái phép. " +
                        "Ngoài ra, chúng tôi luôn tuân thủ các quy định của pháp luật liên quan đến quyền riêng tư và bảo vệ dữ liệu cá nhân.",
                "06-12-2024"
        ));

        // Quy chế
        Button btnQuyChe = findViewById(R.id.button_quy_che);
        btnQuyChe.setOnClickListener(v -> openDetailActivity(
                "Quy chế",
                "Đây là nội dung quy chế. Vui lòng tuân thủ các quy định được đưa ra. " +
                        "Người dùng cần đảm bảo cung cấp thông tin chính xác khi sử dụng dịch vụ và không thực hiện các hành vi gây ảnh hưởng tiêu cực đến hệ thống. " +
                        "Chúng tôi có quyền đình chỉ hoặc chấm dứt tài khoản nếu phát hiện hành vi vi phạm nghiêm trọng. " +
                        "Ngoài ra, mọi hoạt động liên quan đến dịch vụ đều phải tuân thủ các quy định của pháp luật hiện hành.",
                "06-12-2024"
        ));

        // Điều khoản sử dụng
        Button btnDieuKhoan = findViewById(R.id.button_dieu_khoan_su_dung);
        btnDieuKhoan.setOnClickListener(v -> openDetailActivity(
                "Điều khoản sử dụng",
                "Đây là nội dung điều khoản sử dụng. Việc sử dụng ứng dụng đồng nghĩa với việc chấp nhận điều khoản này. " +
                        "Bạn chịu trách nhiệm về thông tin mình cung cấp và việc sử dụng dịch vụ. " +
                        "Mọi hành động sử dụng dịch vụ với mục đích gian lận, phá hoại, hoặc vi phạm pháp luật đều bị nghiêm cấm. " +
                        "Chúng tôi cam kết cung cấp dịch vụ ổn định, nhưng không chịu trách nhiệm cho các gián đoạn bất khả kháng như thiên tai hoặc sự cố kỹ thuật nghiêm trọng.",
                "06-12-2024"
        ));

        // Chính sách giải quyết tranh chấp
        Button btnTranhChap = findViewById(R.id.button_chinh_sach_giai_quyet_tranh_chap);
        btnTranhChap.setOnClickListener(v -> openDetailActivity(
                "Chính sách giải quyết tranh chấp",
                "Đây là nội dung chính sách giải quyết tranh chấp. Chúng tôi cam kết giải quyết tranh chấp nhanh chóng và công bằng. " +
                        "Mọi tranh chấp phát sinh từ việc sử dụng dịch vụ sẽ được giải quyết trên tinh thần hợp tác và tôn trọng lẫn nhau. " +
                        "Nếu tranh chấp không thể giải quyết qua thương lượng, hai bên có thể nhờ đến sự can thiệp của cơ quan pháp lý có thẩm quyền. " +
                        "Trong mọi trường hợp, chúng tôi ưu tiên bảo vệ quyền lợi hợp pháp của người dùng.",
                "06-12-2024"
        ));
    }

    // Hàm mở Activity hiển thị chi tiết điều khoản
    private void openDetailActivity(String title, String content, String date) {
        Intent intent = new Intent(ChinhSachActivity.this, DieuKhoanActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}