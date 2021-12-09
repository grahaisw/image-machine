package id.co.grahaisw.imagemachine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatButton machineDataButton = findViewById(R.id.machine_data);
        AppCompatButton codeReaderButton = findViewById(R.id.code_reader);
        machineDataButton.setOnClickListener(this);
        codeReaderButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(view.getId() == R.id.machine_data){
            intent = new Intent(this, MachineDataActivity.class);
        }else{
            intent = new Intent(this, CodeReaderActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
