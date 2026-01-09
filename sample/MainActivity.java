package com.er.erdatasync;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.er.datasynclib.BleLibraryManager;
import com.er.datasynclib.MainBleService;
import com.er.datasynclib.cnf.DataSyncLibConfig;
import com.er.datasynclib.service.IServiceCallback;
import com.er.datasynclib.util.NetworkStatus;
import com.er.datasynclib.util.UserPermissionManager;
import com.er.erdatasync.adapter.UpdateStatusAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements IServiceCallback {

    //int count = 1;
    //private static final int REQUEST_ALL_PERMISSIONS = 1001;
    private static final String TAG = "MainActivity";
    private static boolean permissionDenied = false;

    private static boolean runBackGroud = false;
    RecyclerView recyclerView;
    UpdateStatusAdapter adapter;

    //private MainBleService callback;
    ArrayList<String> list;
    private MainBleService bleService;
    private boolean allPermissionsGranted = false;
    private boolean isBoundERBeaconServ;
    private ServiceConnection connectionERService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((MainBleService.LocalBinder) service).getService();
            isBoundERBeaconServ = true;
            bleService.setCallback(MainActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBoundERBeaconServ = false;
            Log.d(TAG, "Service disconnected.");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String apiKey = "DF322350352308HDKV958ddTRD456JDJDHEH9994dfGGGDRTDSW";
        String userName = "Mobile User Name";
        // Get references to the buttons defined in activity_main.xml
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> starDataSyncService(apiKey, userName));

//        Button stopButton = findViewById(R.id.stopButton);
//        stopButton.setOnClickListener(v -> stopBleService());


        UserPermissionManager.requestAllPermissions(this);

        setAdapterView();
    }

    public void setAdapterView() {
        // this.recyclerView = recyclerView;
        list = new ArrayList<>();
        list.add("Wait for instruction");
        // list.add("Item 2");
        //list.add("Item 3");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Use a LinearLayoutManager (vertical list)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the adapter
        adapter = new UpdateStatusAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == UserPermissionManager.REQUEST_ALL_PERMISSIONS) {
            //onRequestPermissionsResult(requestCode, permissions, grantResults);
            allPermissionsGranted = true;
            for (int result : grantResults) {

                if (result != PackageManager.PERMISSION_GRANTED) {
                    // At least one permission denied
                    onAllPermissionsDenied();
                    break;
                }
            }
            if (allPermissionsGranted) {
                UserPermissionManager.requestBackgroundRunPermission(this);

            }


        } else if (requestCode == UserPermissionManager.BACKGROUND_PERMISSION_REQUEST_CODE) {
            runBackGroud = true;
            for (int result : grantResults) {

                if (result != PackageManager.PERMISSION_GRANTED) {
                    // At least one permission denied
                    //onAllPermissionsDenied();
                    runBackGroud = false;
                    break;
                }
            }
            if (runBackGroud) NetworkStatus.runPowerOptimization(this);
        }


    }


    private void onAllPermissionsDenied() {
        permissionDenied = true;
        allPermissionsGranted = false;
        Toast.makeText(this, "You must allow permissions to use application!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Executes the library code to start the Foreground Service using the public API.
     */
    @SuppressLint("MissingPermission")
    private void starDataSyncService(String apiKey, String userName) {
        // Pass the fully qualified class name of this activity.
        Log.d(TAG, "starDataSyncService . permissionDenied=" + permissionDenied + ", runBackGroud=" + runBackGroud);

        if (permissionDenied == false && (runBackGroud || UserPermissionManager.isBackgroundEnable(this))) {
            if (NetworkStatus.requestBluetoothEnable(this) && NetworkStatus.isInternetConnected(this) && NetworkStatus.locationEnable(this)) {
                Log.d(TAG, "network Permitted .");
                String mainActivityClass = MainActivity.class.getName();

                DataSyncLibConfig.initialize(apiKey, userName);
                // Call the library function to start the service
                BleLibraryManager.startMonitoring(MainActivity.this, mainActivityClass, BleLibraryManager.SERVICE_CLASS_BLE, connectionERService);

                Toast.makeText(this, "Service call!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Mobile Internet or Bluetooth are not enable", Toast.LENGTH_SHORT).show();
                NetworkStatus.openWirelessSettings(this);
            }
        } else {
            if (runBackGroud) {
                Toast.makeText(this, "Background Location Permission Required", Toast.LENGTH_SHORT).show();
                UserPermissionManager.requestBackgroundRunPermission(this);
            } else {
                permissionDenied = false;
                NetworkStatus.packageEnable(this);
                runBackGroud = true;
            }
            //UserPermissionManager.requestAllPermissions(this);

        }

    }

    @Override
    public void onStatusUpdate(String workerName, String status) {
        Log.d(TAG, "serviceName: " + workerName + " , status=" + status);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        //list.add(0,"Time :" + time + " , Status :"+ status );
        String data = "Time :" + time + ", " + workerName + " , Status :" + status;
        // list.add(0,"Time :" + new Date() + " , Status :"+ status + " , ServiceName: " + workerName);
        // 2. Notify the adapter that the data has changed
        //adapter.notifyDataSetChanged();
        // 3. Repeat this task every 2000ms (2 seconds)
        updateUi(data);
    }

    @Override
    public void onTaskCompleted(String workerName, String result) {
        Log.d(TAG, "serviceName: " + workerName + " , status=" + result);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        String data = "Time :" + time + ", " + workerName + " , Status :" + result;
        // 2. Notify the adapter that the data has changed
        // adapter.notifyDataSetChanged();
        updateUi(data);
    }


    public void updateUi(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //list.add(0, "New Item");
                updateUiSync(message);
            }
        });
    }

    public synchronized void updateUiSync(String message) {
        if (list.size() > 20) {

            //list=new ArrayList<String>();
            list.add(0, message);
            adapter.notifyDataSetChanged();
        } else {
            list.add(0, message);
            adapter.notifyItemInserted(0); // This triggers the "slide-in" animation
            recyclerView.scrollToPosition(0);
        }
    }
//    /**
//     * Executes the library code to stop the Foreground Service.
//     */
//    private void stopBleService() {
//        // Call the library function to stop the service
//        BleLibraryManager.stopMonitoring(getApplicationContext(), BleLibraryManager.SERVICE_CLASS_BLE);
//        Toast.makeText(this, "BLE SERVICE_CLASS_ERB Service Stopped.", Toast.LENGTH_SHORT).show();
//
//    }


    private void stopBleService() {
        if (isBoundERBeaconServ) {
            unbindService(connectionERService);
            isBoundERBeaconServ = false;

            Intent serviceIntent = new Intent(this, MainBleService.class);
            stopService(serviceIntent);
            Toast.makeText(this, "Service stopped.", Toast.LENGTH_SHORT).show();
            updateUi("Service stopped");
            Log.v("RR..", "stopBleService");
        }
    }

}