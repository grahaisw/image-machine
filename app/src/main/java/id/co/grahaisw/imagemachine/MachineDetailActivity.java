package id.co.grahaisw.imagemachine;

import static android.content.Intent.ACTION_OPEN_DOCUMENT;
import static android.text.TextUtils.isEmpty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import id.co.grahaisw.imagemachine.Adapter.MachineImageAdapter;
import id.co.grahaisw.imagemachine.Model.Machine;

public class MachineDetailActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private int machineId;
    private String previousMenu;
    private LinearLayout idLayout;
    private TextView idTV;
    private AppCompatEditText nameET;
    private AppCompatEditText typeET;
    private AppCompatEditText codeET;
    private AppCompatEditText lastMTET;
    private int datePickerInput;
    private ArrayList<Uri> uriArrayList;
    private MachineImageAdapter machineImageAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);
        init();
        initDatePicker();
        onActivityResult();
        checkData();
    }

    private void init() {
        db = new DatabaseHelper(this);
        idLayout = findViewById(R.id.id_layout);
        idTV = findViewById(R.id.id);
        nameET = findViewById(R.id.name);
        typeET = findViewById(R.id.type);
        codeET = findViewById(R.id.code);
        lastMTET = findViewById(R.id.last_mt);
        RecyclerView listImageRV = findViewById(R.id.list_image_rv);
        AppCompatButton machineImage = findViewById(R.id.machine_image);
        AppCompatButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        machineImage.setOnClickListener(this);

        uriArrayList = new ArrayList<>();
        machineImageAdapter = new MachineImageAdapter(this, uriArrayList);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        listImageRV.setLayoutManager(layoutManager);
        listImageRV.setItemAnimator(new DefaultItemAnimator());
        listImageRV.setAdapter(machineImageAdapter);
        listImageRV.setNestedScrollingEnabled(false);
    }

    private void initDatePicker() {
        lastMTET.setFocusable(false);
        lastMTET.setOnClickListener( v -> {
            Calendar newCalendar = Calendar.getInstance();
            if(!isEmpty(Objects.requireNonNull(lastMTET.getText()).toString())) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat f = new SimpleDateFormat( "yyyy-MM-dd" );
                String start_date = lastMTET.getText().toString();
                Date d = null;
                try {
                    d = f.parse(start_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (d != null) {
                    newCalendar.setTimeInMillis(d.getTime());
                }
            }

            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance( this, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setThemeDark(false);
            datePickerDialog.setAccentColor( Color.parseColor("#222222"));
            datePickerInput = v.getId();

            if (getFragmentManager() != null) {
                datePickerDialog.show(getSupportFragmentManager(), "Date Picker Dialog");
            }
        } );

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = dateFormat.format(c.getTime());
        lastMTET.setText(formatedDate);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onActivityResult() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri pictureURI;
                            ClipData clipData = data.getClipData();
                            if(clipData == null){
                                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                pictureURI = data.getData();
                                if(uriArrayList.size() > 9) {
                                    Toast.makeText(this, "You only can choose maximum 10 images.", Toast.LENGTH_SHORT).show();
                                }else{
                                    uriArrayList.add(pictureURI);
                                    machineImageAdapter.notifyDataSetChanged();
                                }
                            }else {
                                int totalImage = clipData.getItemCount();
                                if(totalImage > 10){
                                    Toast.makeText(this, "You only can choose maximum 10 images.", Toast.LENGTH_SHORT).show();
                                } else {
                                    int checkTotal = (uriArrayList.size() + 1) + totalImage;
                                    if(checkTotal > 10){
                                        Toast.makeText(this, "You only can choose maximum 10 images.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        for (int i = 0; i < totalImage; i++) {
                                            getContentResolver().takePersistableUriPermission(clipData.getItemAt(i).getUri(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            pictureURI = clipData.getItemAt(i).getUri();
                                            uriArrayList.add(pictureURI);
                                            machineImageAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void checkData() {
        machineId = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        previousMenu = Objects.requireNonNull(getIntent().getExtras()).getString("menu");
        if(machineId > 0){
            idLayout.setVisibility(View.VISIBLE);

            Machine machine = db.getMachineById(machineId);

            idTV.setText(String.valueOf(machine.getId()));
            nameET.setText(machine.getName());
            typeET.setText(machine.getType());
            codeET.setText(String.valueOf(machine.getCode()));
            lastMTET.setText(machine.getLast_mt());

            if(db.getImageMachineCountById(machineId) > 0){
                uriArrayList.addAll(db.getMachineImageAll(machineId));
                machineImageAdapter.notifyDataSetChanged();
            }
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
            String last_mt = Objects.requireNonNull(lastMTET.getText()).toString().trim();

            if(name.equalsIgnoreCase("")){
                Toast.makeText(this, "Name Cannot Empty", Toast.LENGTH_SHORT).show();
            }else if(type.equalsIgnoreCase("")){
                Toast.makeText(this, "Type Cannot Empty", Toast.LENGTH_SHORT).show();
            }else if(code.equalsIgnoreCase("")){
                Toast.makeText(this, "Code Cannot Empty", Toast.LENGTH_SHORT).show();
            }else{
                if(machineId > 0) {
                    if(db.getMachineIdByCodeAndId(machineId, Integer.parseInt(code)) > 0){
                        Toast.makeText(this, "Machine code already used.", Toast.LENGTH_SHORT).show();
                    } else {
                        db.updateMachine(machineId, name, type, code, last_mt);
                        if (uriArrayList.size() > 0) {
                            db.deleteMachineImage(machineId);
                            db.insertMachineImage(machineId, uriArrayList);
                        }
                        Toast.makeText(this, "Update Data Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MachineDataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    if(db.getMachineIdByCode(Integer.parseInt(code)) > 0){
                        Toast.makeText(this, "Machine code already used.", Toast.LENGTH_SHORT).show();
                    } else {
                        db.insertMachine(name, type, code, last_mt);
                        if (uriArrayList.size() > 0) {
                            db.insertMachineImage(db.getMachineIdRecent(), uriArrayList);
                        }
                        Toast.makeText(this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MachineDataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }else if(view.getId() == R.id.machine_image){
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent();
                            intent.setAction(ACTION_OPEN_DOCUMENT);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            activityResultLauncher.launch(intent);
                        }

                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(MachineDetailActivity.this, "Please enable permission for read storage on Settings", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    }).check();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String day, month;

        if(dayOfMonth < 10){
            day = "0" + dayOfMonth;
        }else{
            day = String.valueOf(dayOfMonth);
        }

        monthOfYear = monthOfYear + 1;
        if(monthOfYear < 10){
            month = "0" + monthOfYear;
        }else{
            month = String.valueOf(monthOfYear);
        }

        if (datePickerInput == R.id.last_mt) {
            lastMTET.setText(year+"-"+month+"-"+day);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if(previousMenu.equalsIgnoreCase("machine_data")) {
            intent = new Intent(this, MachineDataActivity.class);
        }else{
            intent = new Intent(this, CodeReaderActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
