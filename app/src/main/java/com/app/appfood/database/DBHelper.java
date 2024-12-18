package com.app.appfood.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final String db_Name = "food.db";
    private static final int db_Version = 1;
    private final Context context;
    private static final String DB_PATH = "/data/data/%s/databases/";

    public DBHelper(Context context) {
        super(context, db_Name, null, db_Version);
        this.context = context;
        copyDatabaseIfNotExists();
    }

    private void copyDatabaseIfNotExists() {
        String dbPath = String.format(DB_PATH, context.getPackageName()) + db_Name;
        File dbFile = new File(dbPath);

        if (!dbFile.exists()) {
            try {
                // Tạo thư mục chứa database nếu chưa tồn tại
                File dbFolder = new File(dbFile.getParent());
                if (!dbFolder.exists()) {
                    dbFolder.mkdirs();
                }

                // Sao chép database từ thư mục assets
                InputStream input = context.getAssets().open(db_Name);
                OutputStream output = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();

                System.out.println("Database copied successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần tạo bảng, database đã có sẵn
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp database nếu cần
    }
}
