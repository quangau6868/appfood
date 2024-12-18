package com.app.appfood.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.activity.PaymentActivity;
import com.app.appfood.adapter.CartAdapter;
import com.app.appfood.dao.CartDao;
import com.app.appfood.dao.ThanhToanDao;
import com.app.appfood.model.CartItem;
import com.app.appfood.model.ThanhToan;

import java.util.Calendar;
import java.util.List;

public class GiohangFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList;
    private TextView emptyCartMessage, totalAmount,giohang;
    private Button buttonCheckout;
    private CartDao cartDao;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);

        // Ánh xạ các view
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyCartMessage = view.findViewById(R.id.emptyCartMessage);
        totalAmount = view.findViewById(R.id.Tongtien);
        giohang = view.findViewById(R.id.giohang);
        buttonCheckout = view.findViewById(R.id.buttonCheckout);

        // Khởi tạo CartDao để lấy giỏ hàng từ cơ sở dữ liệu
        cartDao = new CartDao(getContext());

        // Lấy danh sách sản phẩm trong giỏ hàng từ cơ sở dữ liệu
        cartList = cartDao.layGioHang();

        // Kiểm tra giỏ hàng có sản phẩm không
        if (cartList == null || cartList.isEmpty()) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
            totalAmount.setVisibility(View.GONE);
            giohang.setVisibility(View.GONE);
            buttonCheckout.setVisibility(View.GONE);
        } else {
            emptyCartMessage.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            totalAmount.setVisibility(View.VISIBLE);
            giohang.setVisibility(View.VISIBLE);
            buttonCheckout.setVisibility(View.VISIBLE);

            // Tính tổng tiền cho giỏ hàng
            double total = 0;
            for (CartItem product : cartList) {
                total += product.getGia() * product.getSoLuong();
            }
            totalAmount.setText(String.format("Tổng Tiền: %,.0f VND", total));

            // Thiết lập adapter cho RecyclerView
            cartAdapter = new CartAdapter(cartList, cartDao, this);  // Truyền GiohangFragment vào CartAdapter
            recyclerViewCart.setAdapter(cartAdapter);
        }

        // Xử lý sự kiện bấm nút Thanh Toán
        buttonCheckout.setOnClickListener(v -> showCheckoutDialog());


        return view;
    }

    private void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate layout dialog_checkout.xml
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_checkout, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editPhone = dialogView.findViewById(R.id.editPhone);
        EditText editAddress = dialogView.findViewById(R.id.editAddress);
        TextView tvNote = dialogView.findViewById(R.id.tvNote);
        tvNote.setText("Lưu ý: Chỉ giao địa chỉ ở Hà Nội");

        // Tạo Spinner hoặc ListView cho phép chọn sản phẩm
        Spinner spinnerProducts = dialogView.findViewById(R.id.spinnerProducts);
        ArrayAdapter<CartItem> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cartList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapter);

        // Thêm EditText để chọn ngày thanh toán
        EditText editDate = dialogView.findViewById(R.id.editDate); // Thêm trường ngày thanh toán

        // Lắng nghe sự kiện chọn ngày
        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year1, month1, dayOfMonth) -> {
                        // Chuyển đổi thành định dạng yyyy-MM-dd
                        String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        editDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        builder.setCancelable(false);  // Không cho phép người dùng thoát Dialog bằng cách bấm ra ngoài

        // Tạo đối tượng AlertDialog
        AlertDialog dialog = builder.create();

        // Lấy các button từ layout
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Xử lý sự kiện bấm nút Xác nhận
        btnConfirm.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String date = editDate.getText().toString().trim(); // Lấy ngày thanh toán

            boolean isValid = true; // Biến để kiểm tra tình trạng hợp lệ của tất cả các trường

            // Kiểm tra tên
            if (name.isEmpty()) {
                editName.setError("Tên không được để trống!");
                isValid = false;
            } else if (name.length() < 3) {
                editName.setError("Tên phải có ít nhất 3 ký tự!");
                isValid = false;
            }

            // Kiểm tra số điện thoại
            if (phone.isEmpty()) {
                editPhone.setError("Số điện thoại không được để trống!");
                isValid = false;
            } else if (phone.length() != 10) {
                editPhone.setError("Số điện thoại phải có đúng 10 chữ số!");
                isValid = false;
            } else if (!phone.matches("\\d{10}")) { // Chỉ chứa đúng 10 chữ số
                editPhone.setError("Số điện thoại chỉ được chứa các chữ số!");
                isValid = false;
            }

            // Kiểm tra địa chỉ
            if (address.isEmpty()) {
                editAddress.setError("Địa chỉ không được để trống!");
                isValid = false;
            }

            // Kiểm tra ngày thanh toán
            if (date.isEmpty()) {
                editDate.setError("Ngày thanh toán không được để trống!");
                isValid = false;
            }

            // Kiểm tra nếu thông tin hợp lệ
            if (isValid) {
                CartItem selectedProduct = (CartItem) spinnerProducts.getSelectedItem(); // Lấy sản phẩm được chọn

                // Tạo đối tượng ThanhToan cho sản phẩm được chọn
                ThanhToan thanhToan = new ThanhToan(
                        name,
                        phone,
                        address,
                        selectedProduct.getGia() * selectedProduct.getSoLuong(),  // Tính tổng tiền cho sản phẩm được chọn
                        selectedProduct.getMaSanPham(),
                        selectedProduct.getTenSanPham(),
                        selectedProduct.getSoLuong(),
                        date  // Thêm ngày thanh toán vào đối tượng ThanhToan
                );

                // Lưu thông tin thanh toán vào cơ sở dữ liệu
                ThanhToanDao thanhToanDao = new ThanhToanDao(getContext());
                long id = thanhToanDao.addThanhToan(thanhToan);

                // Thông báo thành công
                if (id > 0) {
                    Intent intent = new Intent(getContext(), PaymentActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("amount", selectedProduct.getGia() * selectedProduct.getSoLuong());
                    intent.putExtra("productName", selectedProduct.getTenSanPham());
                    intent.putExtra("quantity", selectedProduct.getSoLuong());
                    intent.putExtra("date", date);
                    intent.putExtra("status", "Đang xử lý...");
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Đặt hàng không thành công, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();  // Đóng dialog khi thông tin hợp lệ
            } else {
                // Nếu có lỗi, thông báo cho người dùng và không đóng dialog
                Toast.makeText(getContext(), "Đặt hàng không thành công", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện bấm nút Hủy
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();  // Đóng dialog khi người dùng bấm hủy
        });

        // Hiển thị dialog
        dialog.show();
    }



    // Phương thức để cập nhật tổng tiền
    public void updateTotalAmount() {
        double total = 0;
        for (CartItem product : cartList) {
            total += product.getGia() * product.getSoLuong();
        }
        totalAmount.setText(String.format("Tổng Tiền: %,.0f VND", total));
    }


    public void updateCart(List<CartItem> newCartItems) {
        if (newCartItems == null || newCartItems.isEmpty()) {
            cartList.clear();
            cartAdapter.notifyDataSetChanged();

            emptyCartMessage.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
            totalAmount.setVisibility(View.GONE);
            giohang.setVisibility(View.GONE);
            buttonCheckout.setVisibility(View.GONE);
        } else {
            cartList.clear();
            cartList.addAll(newCartItems);
            cartAdapter.notifyDataSetChanged();

            emptyCartMessage.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            totalAmount.setVisibility(View.VISIBLE);
            giohang.setVisibility(View.VISIBLE);
            buttonCheckout.setVisibility(View.VISIBLE);

            // Gọi updateTotalAmount để cập nhật tổng tiền
            updateTotalAmount();
        }
    }


}