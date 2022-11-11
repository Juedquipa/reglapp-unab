package com.example.reglappunab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EnsenanzaActivity extends AppCompatActivity {

    ArrayList<SeccionEnsenanza> listDatos;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensenanza);

        recyclerView = findViewById(R.id.recycler_id);

        listDatos = new ArrayList<SeccionEnsenanza>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ensenanza")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            for (int i = 0; i < document.getData().size(); i++) {
                                HashMap data = (HashMap) document.get(String.valueOf(i));

                                listDatos.add(new SeccionEnsenanza((String) data.get("name"), (String) data.get("content")));
                                //Log.e("TAG", listDatos.get(i).getNombre(), task.getException());


                            }

                            AdapterDatos adapterDatos = new AdapterDatos(listDatos);
                            recyclerView.setAdapter(adapterDatos);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                            recyclerView.setHasFixedSize(true);

                        }
                    } else {
                        Log.e("TAG", "Error getting documents.", task.getException());
                    }
                });
    }
}