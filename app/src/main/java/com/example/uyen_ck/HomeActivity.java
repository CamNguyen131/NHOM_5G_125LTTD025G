package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Good for debugging
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uyen_ck.adapters.CategoryAdapter;
import com.example.uyen_ck.models.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvProducts, rvCategories;
    private FirebaseFirestore db;
    private List<Products> productList = new ArrayList<>();
    private ProductAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // 1. Setup Categories RecyclerView (Horizontal)
        rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categoryList);
        rvCategories.setAdapter(categoryAdapter);

        // 2. Setup Products RecyclerView (Grid)
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(adapter);

        // Load Data
        loadCategories();
        loadProducts();
        setupBottomNavigation();
    }

    private void loadCategories() {
        db.collection("categories").get()
                .addOnSuccessListener(snapshots -> {
                    categoryList.clear();
                    // Add "All" option manually if it's not in Firestore
                    categoryList.add(new Category("all", "Tất cả", ""));

                    for (QueryDocumentSnapshot doc : snapshots) {
                        try {
                            Category c = doc.toObject(Category.class);
                            c.setCategoryId(doc.getId());
                            categoryList.add(c);
                        } catch (Exception e) {
                            Log.e("FirestoreError", "Error parsing category", e);
                        }
                    }
                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to load categories", e));
    }

    private void loadProducts() {
        db.collection("products").get()
                .addOnSuccessListener(snapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        try {
                            Products p = doc.toObject(Products.class);
                            p.setProductId(doc.getId());
                            productList.add(p);
                        } catch (Exception e) {
                            Log.e("FirestoreError", "Error parsing product", e);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to load products", e));
    }

    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Tab Trang chủ -> Chuyển đến HomeActivity.class
        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng Activity hiện tại nếu muốn Home là màn hình chính
            });
        }

        // Tab Giỏ hàng -> Chuyển đến CartActivity.class
        if (tabCart != null) {
            tabCart.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Tab Đơn hàng -> Chuyển đến ListOrderActivity.class
        if (tabOrder != null) {
            tabOrder.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ListOrderActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Tab Tài khoản (đã ở trang này nên không cần làm gì)
        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                // Không làm gì, đã ở HomeActivity
            });
        }
    }
}