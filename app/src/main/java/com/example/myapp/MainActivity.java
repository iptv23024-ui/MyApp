package com.example.myapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClientAdapter.OnClientClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddClient;
    private Button btnRefresh;
    private DatabaseHelper db;
    private ClientAdapter adapter;
    private List<Client> clientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        
        recyclerView = findViewById(R.id.recycler_clients);
        fabAddClient = findViewById(R.id.fab_add_client);
        btnRefresh = findViewById(R.id.btn_refresh);

        setupRecyclerView();
        loadClients();

        fabAddClient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
            startActivityForResult(intent, 100);
        });

        btnRefresh.setOnClickListener(v -> loadClients());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clientList = db.getAllClients();
        adapter = new ClientAdapter(clientList, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadClients() {
        clientList = db.getAllClients();
        if (adapter != null) {
            adapter.updateList(clientList);
        }
    }

    @Override
    public void onClientClick(Client client) {
        Intent intent = new Intent(MainActivity.this, ClientDetailsActivity.class);
        intent.putExtra("client_id", client.getId());
        startActivityForResult(intent, 100);
    }

    @Override
    public void onEditClick(Client client) {
        Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
        intent.putExtra("client_id", client.getId());
        startActivityForResult(intent, 100);
    }

    @Override
    public void onDeleteClick(Client client) {
        new AlertDialog.Builder(this)
                .setTitle("Deletar Cliente")
                .setMessage("Tem certeza que deseja deletar " + client.getName() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    if (db.deleteClient(client.getId())) {
                        Toast.makeText(MainActivity.this, "Cliente deletado!", Toast.LENGTH_SHORT).show();
                        loadClients();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao deletar cliente!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadClients();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClients();
    }
}
