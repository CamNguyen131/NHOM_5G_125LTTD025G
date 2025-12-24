package com.example.uyen_ck;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uyen_ck.adapters.ProductAdapter;
import com.example.uyen_ck.models.Products;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private FirebaseFirestore db;
    private List<Products> productList = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        db = FirebaseFirestore.getInstance();

        adapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(adapter);

        loadProducts();
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
                            e.printStackTrace(); // nếu data bẩn → bỏ qua
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}
