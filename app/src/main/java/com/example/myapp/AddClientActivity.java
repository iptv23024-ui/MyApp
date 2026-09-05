package com.example.myapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddClientActivity extends AppCompatActivity {

    private EditText nameInput;
    private Spinner appTypeSpinner;
    private EditText customAppInput;
    private EditText macInput;
    private EditText deviceKeyInput;
    private EditText licenseeDateInput;
    private Button saveButton;
    private Button cancelButton;
    private DatabaseHelper db;
    private Client editingClient;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        db = new DatabaseHelper(this);
        selectedCalendar = Calendar.getInstance();

        nameInput = findViewById(R.id.input_name);
        appTypeSpinner = findViewById(R.id.spinner_app_type);
        customAppInput = findViewById(R.id.input_custom_app);
        macInput = findViewById(R.id.input_mac);
        deviceKeyInput = findViewById(R.id.input_device_key);
        licenseeDateInput = findViewById(R.id.input_license_date);
        saveButton = findViewById(R.id.btn_save);
        cancelButton = findViewById(R.id.btn_cancel);

        setupAppTypeSpinner();
        setupDatePicker();

        // Verificar se estamos editando
        if (getIntent().hasExtra("client_id")) {
            int clientId = getIntent().getIntExtra("client_id", -1);
            editingClient = db.getClientById(clientId);
            if (editingClient != null) {
                populateFields();
                setTitle("Editar Cliente");
            }
        } else {
            setTitle("Adicionar Cliente");
        }

        saveButton.setOnClickListener(v -> saveClient());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void setupAppTypeSpinner() {
        String[] appTypes = {"Bob Player", "IBOPlayer", "IBOPro", "Smart One", "Outro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, appTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appTypeSpinner.setAdapter(adapter);

        appTypeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position == 4) { // "Outro" selecionado
                    customAppInput.setVisibility(android.view.View.VISIBLE);
                } else {
                    customAppInput.setVisibility(android.view.View.GONE);
                    customAppInput.setText("");
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void setupDatePicker() {
        licenseeDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedCalendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        licenseeDateInput.setText(dateFormat.format(selectedCalendar.getTime()));
                    },
                    selectedCalendar.get(Calendar.YEAR),
                    selectedCalendar.get(Calendar.MONTH),
                    selectedCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void populateFields() {
        nameInput.setText(editingClient.getName());
        
        String appType = editingClient.getAppType();
        String[] appTypes = {"Bob Player", "IBOPlayer", "IBOPro", "Smart One", "Outro"};
        int position = 4; // Default to "Outro"
        for (int i = 0; i < appTypes.length; i++) {
            if (appTypes[i].equals(appType)) {
                position = i;
                break;
            }
        }
        appTypeSpinner.setSelection(position);

        if (position == 4) {
            customAppInput.setText(appType);
            customAppInput.setVisibility(android.view.View.VISIBLE);
        }

        macInput.setText(editingClient.getMac());
        deviceKeyInput.setText(editingClient.getDeviceKey());
        licenseeDateInput.setText(editingClient.getLicenseDate());
    }

    private void saveClient() {
        String name = nameInput.getText().toString().trim();
        String mac = macInput.getText().toString().trim();
        String deviceKey = deviceKeyInput.getText().toString().trim();
        String licenseDate = licenseeDateInput.getText().toString().trim();

        if (name.isEmpty() || mac.isEmpty() || deviceKey.isEmpty() || licenseDate.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        String appType;
        if (appTypeSpinner.getSelectedItemPosition() == 4) {
            appType = customAppInput.getText().toString().trim();
            if (appType.isEmpty()) {
                Toast.makeText(this, "Digite o nome do app customizado!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            appType = appTypeSpinner.getSelectedItem().toString();
        }

        if (editingClient != null) {
            editingClient.setName(name);
            editingClient.setAppType(appType);
            editingClient.setMac(mac);
            editingClient.setDeviceKey(deviceKey);
            editingClient.setLicenseDate(licenseDate);

            if (db.updateClient(editingClient)) {
                Toast.makeText(this, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar cliente!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Client newClient = new Client(name, appType, mac, deviceKey, licenseDate);
            if (db.addClient(newClient)) {
                Toast.makeText(this, "Cliente adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Erro ao adicionar cliente!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
