package id.co.grahaisw.imagemachine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

import id.co.grahaisw.imagemachine.Model.Machine;

public class MachineDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private int machineId;
    private LinearLayout idLayout;
    private LinearLayout mtLayout;
    private TextView idTV;
    private TextView mtTV;
    private AppCompatEditText nameET;
    private AppCompatEditText typeET;
    private AppCompatEditText codeET;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);
        init();
        checkData();
    }

    private void init() {
        db = new DatabaseHelper(this);
        idLayout = findViewById(R.id.id_layout);
        mtLayout = findViewById(R.id.mt_layout);
        idTV = findViewById(R.id.id);
        mtTV = findViewById(R.id.mt);
        nameET = findViewById(R.id.name);
        typeET = findViewById(R.id.type);
        codeET = findViewById(R.id.code);
        AppCompatButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void checkData() {
        machineId = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        if(machineId > 0){
            idLayout.setVisibility(View.VISIBLE);
            mtLayout.setVisibility(View.VISIBLE);

            Machine machine = db.getMachineById(machineId);

            idTV.setText(String.valueOf(machine.getId()));
            nameET.setText(machine.getName());
            typeET.setText(machine.getType());
            codeET.setText(machine.getCode());
            mtTV.setText(machine.getLast_mt());
        }else{
            idLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.submit){
            String name = Objects.requireNonNull(nameET.getText()).toString().trim();
            String type = Objects.requireNonNull(typeET.getText()).toString().trim();
            String code = Objects.requireNonNull(codeET.getText()).toString().trim();

            if(name.equalsIgnoreCase("")){
                Toast.makeText(this, "Name Cannot Empty", Toast.LENGTH_SHORT).show();
            }else if(type.equalsIgnoreCase("")){
                Toast.makeText(this, "Type Cannot Empty", Toast.LENGTH_SHORT).show();
            }else if(code.equalsIgnoreCase("")){
                Toast.makeText(this, "Code Cannot Empty", Toast.LENGTH_SHORT).show();
            }else{
                if(machineId > 0) {
                    db.updateMachine(machineId, name, type, code);
                    Toast.makeText(this, "Update Data Success", Toast.LENGTH_SHORT).show();
                }else{
                    db.insertMachine(name, type, code);
                    Toast.makeText(this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, MachineDataActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MachineDataActivity.class);
        startActivity(intent);
        finish();
    }
}
