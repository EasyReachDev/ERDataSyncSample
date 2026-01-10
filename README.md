# ERDataSyncSample
This is a sample Android Application developed using datasynclib by EasyReach.

ERBeacon - Refrigirator Temeprature monitoring integration document
# 1. Introduction
This document is for EasyReach customers who wish to deploy EasyReach Temperature Monitoring Device in their own refrigirator. The Library will have to be integrated into the customers mobile application provided to the Retailer or Merchandizer who visits the Retail shop on a regular basis. The Developer need to follow few simple steps while integrating the erdatasync.arr library into their own applicaiton.

Below is the behaviour of this library

1. The library works as a Android Service
2. The library scans for different variety of ERBeacon
3. If the device is found to be ERBeacon the library connects to the Device
4. Post successfull connection, library reads all the stored data (including temperature, GPS locaiton, timestamp) from the device
5. The data is sent to EasyReach Platform where data can be visualized in the form of Table, Graphs and Reports

# 2. How to integrate
Simply clone the present repository and fallow the Below Process.

1. AAR file Implimentaton
Once you have the datasynclib.aar file, follow these steps to import it into a new project:

Step 1: Add the file
Copy the datasynclib.aar file.

In your new project, switch the project view to "Project" and paste the file into the app/libs folder (create the folder if it doesn't exist).

Step 2: Configure build.gradle
Open your app-level build.gradle file and add the dependency:

Gradle

dependencies {
    // This tells Gradle to look into the libs folder for the specific file
    implementation files('libs/datasynclib.aar')
}
Step 3: Sync Project
Click Sync Now in the top right bar. You can now call the classes and methods from your library just like any other dependency.

# 3. User Permission 

	Add below Permission in Android Manifest file 

    <uses-permission android:name="android.permission.LOCAL_MAC_ADDRESS"
        tools:ignore="ProtectedPermissions" />
    <!-- Required for all foreground services from Android 9 (API 28) and up -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <!-- Required for Android 13 (API 33) and up for posting notifications -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <!-- Required for Android 14 (API 34) and up, specific to the task type -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission
        android:name="android.permission.BLUETOOTH"
        android:maxSdkVersion="35"
        tools:replace="android:maxSdkVersion" />
    <uses-permission
        android:name="android.permission.BLUETOOTH_ADMIN"
        android:maxSdkVersion="35"
        tools:replace="android:maxSdkVersion" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />

    <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
    <uses-permission
        android:name="android.permission.READ_EXTERNAL_STORAGE"
        android:maxSdkVersion="32" />

    <uses-feature
        android:name="android.hardware.bluetooth"
        android:required="true" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-feature
        android:name="android.hardware.bluetooth_le"
        android:required="true" />

    <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
    <!-- Required for Android 12+ -->
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE" />

# 4. Service Declaration 
 
	<service
            android:name="com.er.datasynclib.MainBleService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="location" />
			
# 5. Activity implementation & sevice call

	
	1. Ask user Permission put below line in onCreate methods
	
	 UserPermissionManager.requestAllPermissions(this);
	 
	2. API Key: 

		String apiKey = "DF322350352308HDKV958ddTRD456JDJDHEH99944444444"; 
		Note : This is dummy Key will not work. Contact sales@easyreach.co.in to get key  
        String userName = "Mobile User Name";	
	
	3. Call Service: 	
	
		Copy starDataSyncService method from sample Code and call in from onCreate or from button Click
		
	
	4. Permission Result:
		
		Copy onRequestPermissionsResult method from sample Code in main Activity

	5. Bind Service: 

		Copy ServiceConnection as connectionERService to bind service sample Code in main Activity
	
	6. Library Add:

		implementation(files("libs/er-datasync-lib.aar"))
		implementation("com.squareup.retrofit2:retrofit:2.9.0")
		implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
		implementation("com.squareup.retrofit2:converter-gson:2.9.0")
		implementation("com.google.code.gson:gson:2.10.1")
# 3. Code Explanation
## Manifest Permissions

## `onCreate` implementation

## One time permission in code

## Background services

## Notifications

## Application User Interface


