# altla-nearby

## Description
This sample application is that performs proximity communication using BLE (Bluetooth Low Energy).
<br>
In recent years, proximity communication has been attention.

For example, there are the following cases.

- Set the beacon in the store and deliver the coupon when the user passed the store
- Set the beacon at the bus stop and display the bus timetable when the user approaches the bus stop

This sample application activates the Android device as a beacon (Eddystone-UID) and each Android devices advertise and subscribe using BLE.
<br>
When you can detect nearby users and you can get user information.

## Getting started

### Set up Firebase project
This sample application uses Firebase that known as mobile backend as a service (MBaaS).
<br>
<br>
You need to create a Firebase project, and download a ```google-services.json``` file.
<br>
After downloading the ```google-services.json file```, copy this into your project's module folder, typically app/.
<br>
And, you must add SHA-1 fingerprint of your application to Firebase.

Please refer to the following link.
<br>
[How to add Firebase to your Android Project](https://firebase.google.com/docs/android/setup#add_firebase_to_your_app)

### Enable Google sign in
Please refer to the following link.
<br>
[Authenticate Using Google Sign-In on Android - Before you begin](https://firebase.google.com/docs/auth/android/google-signin)

## Function

### Sgin in
Use Google sign in.

### Detection of the nearby users
You can detect nearby users.
<br>
Detection of nearby users is performed in the foreground and in the background.

### Nearby history
You can see the history of detecting the nearby users in chronological order.

### User profile
You can see detected user profile.
<br>
User profile contains personal information (such as name, email) associated with a Google Account and environment data (such as weather, location) when proximity communication has occurred.

### Favorite
It is possible to manage nearby users as favorites and access favorite user profile.

### Device tracking
You can track the location of the device.
<br>
When other user detect your devices, your device location saved and you can see that location.

## Use Libraries

### [Firebase](https://firebase.google.com/?hl=ja)
This sample application uses Firebase as a backend service.
<br>
This sample application uses Firebase functions that Realtime Database, Analytics, Crash Reporting.

### [AltBeacon](http://altbeacon.org/)
AltBeacon is beacon library.
<br>
This sample application advertise and subscribe as a beacon by using AltBeacon library.

### [Google Awereness API](https://developers.google.com/awareness/)
When detect nearby users, get the user's current environment (such as weather, location, user activity) by using Google Awareness API.
<br>
Show getting user's environment on user profile screen.

### [Google Maps API](https://developers.google.com/maps/?hl=ja)
Show detected user on map by using Google Maps API.

### [Dagger](https://google.github.io/dagger/)
This sample application approaches [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) using dependency injection.
<br>
Dagger is library of dependency injection.