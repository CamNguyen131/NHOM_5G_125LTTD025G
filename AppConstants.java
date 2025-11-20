package com.example.beauty;

/**
 * Lớp chứa các hằng số toàn cục được sử dụng trong ứng dụng.
 */
public class AppConstants {

    // Tên của các tệp Shared Preferences
    public static final String PREF_NAME = "MyAppPreferences";

    // Khóa cho các giá trị được lưu trong Shared Preferences
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_AUTH_TOKEN = "auth_token";

    // Khóa API (Chỉ là placeholder, cần thay thế bằng khóa thực tế nếu có)
    public static final String BASE_API_URL = "https://api.example.com/v1/";
    public static final String API_KEY = "YOUR_SECRET_API_KEY_HERE";

    // Hằng số cho các mã yêu cầu (Request Codes)
    public static final int REQUEST_CODE_LOGIN = 1001;
    public static final int REQUEST_CODE_PERMISSION_STORAGE = 1002;
}