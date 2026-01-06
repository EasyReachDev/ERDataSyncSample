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
Simply clone the present repository and open it using Android Studio. Connect the device and run the application.

# 3. Code Explanation
## Manifest Permissions

## `onCreate` implementation

## One time permission in code

## Background services

## Notifications

## Application User Interface


