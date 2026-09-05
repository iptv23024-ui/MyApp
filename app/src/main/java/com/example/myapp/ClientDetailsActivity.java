package com.example.myapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClientDetailsActivity extends AppCompatActivity {

    private TextView nameText;
    private TextView appTypeText;
    private TextView macText;
    private TextView deviceKeyText;
    private TextView licenseDateText;
    private TextView statusText;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnBack;
    private DatabaseHelper db;
    private Client client;
    private int clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        db = new DatabaseHelper(this);

        nameText = findViewById(R.id.text_detail_name);
        appTypeText = findViewById(R.id.text_detail_app_type);
        macText = findViewById(R.id.text_detail_mac);
        deviceKeyText = findViewById(R.id.text_detail_device_key);
        licenseDateText = findViewById(R.id.text_detail_license_date);
        statusText = findViewById(R.id.text_detail_status);
        btnEdit = findViewById(R.id.btn_edit_detail);
        btnDelete = findViewById(R.id.btn_delete_detail);
        btnBack = findViewById(R.id.btn_back);

        clientId = getIntent().getIntExtra("client_id", -1);

        if (clientId != -1) {
            client = db.getClientById(clientId);
            if (client != null) {
                displayClientDetails();
            }
        }

        btnEdit.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, AddClientActivity.class);
            intent.putExtra("client_id", clientId);
            startActivityForResult(intent, 100);
        });

        btnDelete.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Deletar Cliente")
                    .setMessage("Tem certeza que deseja deletar " + client.getName() + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        if (db.deleteClient(clientId)) {
                            Toast.makeText(ClientDetailsActivity.this, "Cliente deletado!", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void displayClientDetails() {
        setTitle(client.getName());
        nameText.setText("Nome: " + client.getName());
        appTypeText.setText("Tipo de App: " + client.getAppType());
        macText.setText("MAC: " + client.getMac());
        deviceKeyText.setText("Device Key: " + client.getDeviceKey());
        licenseDateText.setText("Data de Validade: " + client.getLicenseDate());

        String statusMessage = LicenseUtils.getStatusMessage(client.getLicenseDate());
        int statusColor = LicenseUtils.getStatusColor(LicenseUtils.getStatus(client.getLicenseDate()));

        statusText.setText(statusMessage);
        statusText.setTextColor(statusColor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            client = db.getClientById(clientId);
            if (client != null) {
                displayClientDetails();
            }
        }
    }
}
