package com.example.uyen_ck;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uyen_ck.models.Category;
import com.example.uyen_ck.models.Products;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import com.example.uyen_ck.adapters.CategoryAdapter;
import com.example.uyen_ck.adapters.ProductAdapter;


public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvCategories, rvProducts;
    private TextView tvUserName, tvProductTitle;
    private FirebaseFirestore db;
    private List<Products> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        rvCategories = findViewById(R.id.rvCategories);
        rvProducts = findViewById(R.id.rvProducts);
        tvUserName = findViewById(R.id.tvUserName);
        tvProductTitle = findViewById(R.id.tvProductTitle);

        // ✅ FIX CRASH: LayoutManager
        rvCategories.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(
                        this,
                        androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        rvProducts.setLayoutManager(
                new androidx.recyclerview.widget.GridLayoutManager(this, 2)
        );

        loadCategories();
        loadProducts(null);
    }

    private void loadCategories() {
        db.collection("categories").get().addOnSuccessListener(snapshots -> {
            categoryList.clear();
            categoryList.add(new Category("all", "Tất cả", ""));

            for (QueryDocumentSnapshot doc : snapshots) {
                Category cat = doc.toObject(Category.class);
                cat.setCategoryId(doc.getId());
                categoryList.add(cat);
            }

            CategoryAdapter adapter = new CategoryAdapter(categoryList);
            rvCategories.setAdapter(adapter);
        });
    }


    public void loadProducts(String catId) {
        Query query = db.collection("products");
        if (catId != null && !catId.equals("all")) {
            query = query.whereEqualTo("categoryId", catId);
            tvProductTitle.setText("Kết quả lọc...");
        } else {
            tvProductTitle.setText("Tất cả sản phẩm");
        }

        query.get().addOnSuccessListener(snapshots -> {
            productList.clear();

            for (QueryDocumentSnapshot doc : snapshots) {
                Products p = doc.toObject(Products.class);
                p.setProductId(doc.getId());
                productList.add(p);
            }

            ProductAdapter productAdapter = new ProductAdapter(productList);
            rvProducts.setAdapter(productAdapter);
        });

    }
}