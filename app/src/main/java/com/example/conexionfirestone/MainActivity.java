package com.example.conexionfirestone;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    private List<Person> persons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
agregarDatosIniciales();
        initializeData();
    }
    public void agregarDatosIniciales(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
// Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Alan");
        user.put("age", "90 years old");
// Add a new document with a generated ID
        db.collection("products")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(),
                                "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Error adding document", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void initializeData(){
        persons = new ArrayList<>();
// si quisieramos pasar datos directos
//persons.add(new Person("Emma Wilson", "23 years old", R.drawable.person));
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore
                .collection("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document:queryDocumentSnapshots){
// Agregamos a nuestro ArrayList los datos de persona,accedemos a nuestro documento y indicamos que campos queremos traer
                            persons.add(new
                                    Person(document.get("name").toString(),document.get("age").toString(),R.drawable.empleados));
                        }
//Agregamos a nuestro adaptador el array listcon los datos que seencuentra en persosns
                        RVAdapter adapter = new RVAdapter(persons);
// cargamos nuestros datos al recycleview
                        rv.setAdapter(adapter);
                    }
                } )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getBaseContext(), "Error reading",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }}
