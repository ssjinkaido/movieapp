package com.example.movieapp.utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class Util {
    public static String getUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getUid();
    }

    /**
     * Return the database reference of any passed in string reference
     * @param reference the name of the database reference in the database table.
     * @return
     */
    public static DatabaseReference getDatabaseReference(String reference) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(reference);
    }

    /**
     * Return the storage reference of any passed in string reference
     * @param reference the name of the storage reference in the storage table.
     * @return
     */
    public static StorageReference getStorageReference(String reference) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return storage.getReference(reference);
    }
}
