package com.example.uyen_ck.utils;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class FirestoreHelper {

    public interface IdCallback {
        void onIdGenerated(String newId);
        void onError(Exception e);
    }

    // Hàm dùng chung để lấy ID tự tăng
    public static void getNextAutoId(FirebaseFirestore db, String counterDocName, String prefix, IdCallback callback) {
        DocumentReference counterRef = db.collection("counters").document(counterDocName);

        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(counterRef);
                    long nextValue = 1;
                    if (snapshot.exists() && snapshot.contains("currentValue")) {
                        nextValue = snapshot.getLong("currentValue") + 1;
                    }

                    transaction.update(counterRef, "currentValue", nextValue);
                    return String.format("%s_%02d", prefix, nextValue);
                }).addOnSuccessListener(callback::onIdGenerated)
                .addOnFailureListener(callback::onError);
    }
}