package id.co.grahaisw.imagemachine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import id.co.grahaisw.imagemachine.Adapter.MachineAdapter;
import id.co.grahaisw.imagemachine.Model.Machine;

public class MachineDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView noDataTV;
    private DatabaseHelper db;
    private MachineAdapter machineAdapter;
    private List<Machine> machineList;
    private String defaultSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_data);
        init();
        checkData();
    }

    private void init(){
        db = new DatabaseHelper(this);
        AppCompatButton addButton = findViewById(R.id.add_button);
        FloatingActionButton sortFAB = findViewById(R.id.sort_fab);
        RecyclerView machineDataRV = findViewById(R.id.machine_data_rv);
        noDataTV = findViewById(R.id.no_data_tv);

        addButton.setOnClickListener(this);
        sortFAB.setOnClickListener(this);

        machineList = new ArrayList<>();
        machineAdapter = new MachineAdapter(this, machineList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        machineDataRV.setLayoutManager(layoutManager);
        machineDataRV.setItemAnimator(new DefaultItemAnimator());
        machineDataRV.setAdapter(machineAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void checkData() {
        if(db.getMachineCount() < 1){
            noDataTV.setVisibility(View.VISIBLE);
        }else{
            noDataTV.setVisibility(View.GONE);
            defaultSort = getString(R.string.name);
            machineList.addAll(db.getMachineAll(defaultSort));
            machineAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sortData(){
        if(db.getMachineCount() > 0) {
            if (defaultSort.equals(getString(R.string.name))) {
                defaultSort = getString(R.string.type);
                Toast.makeText(this, "Sorted by machine type", Toast.LENGTH_SHORT).show();
            } else {
                defaultSort = getString(R.string.name);
                Toast.makeText(this, "Sorted by machine name", Toast.LENGTH_SHORT).show();
            }
            machineList.clear();
            machineList.addAll(db.getMachineAll(defaultSort));
            machineAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "No displayed data to sort", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_button){
            Intent intent = new Intent(this, MachineDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("id", 0);
            intent.putExtra("menu", "machine_data");
            startActivity(intent);
            finish();
        }else if(view.getId() == R.id.sort_fab){
            sortData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}