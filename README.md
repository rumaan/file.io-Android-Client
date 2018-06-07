# File.io Android App
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/845a73f559a747279016b83c41a78446)](https://www.codacy.com/app/rumaan/file.io-app?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rumaan/file.io-app&amp;utm_campaign=Badge_Grade)

This simple app allows you to upload any file and get a _sharable_ link with a set expiration time.
The file will be **Deleted** after its downloaded or after expiration time (regardless of whether its downloaded or not).
This app is made with the help of [file.io](https://file.io) which is an **_Anonymous_, _Secure_** file sharing platform by [Humbly](http://humbly.com/).

## Screenshots 📸
<p float="left">
<img src="/screenshots/sc1.png" alt="Home Screen" height="600"/>

## Libraries Used ❤️
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Permission Dispatcher](https://permissions-dispatcher.github.io/PermissionsDispatcher/)
- [Fuel](https://github.com/kittinunf/Fuel)
- [NumberProgressBar](https://github.com/daimajia/NumberProgressBar)
- [FirebaseCrashlytics](https://firebase.google.com/docs/crashlytics)
- [CustomActivityOnCrash](https://github.com/Ereza/CustomActivityOnCrash)

Vector Images from [FlatIcon](https://www.flaticon.com/).

## TODO List ✅:
- [ ] Set Expiration Time
- [X] Handle different use cases for storage on the device.
- [ ] Progress Bar for File Upload Progress
- [X] Custom Error Dialog
- [X] UI Testing
- [X] Kotlinize the project 🎳
- [ ] Support for *Multiple* File Upload (Create a List)
- [ ] App Icon
- [X] Offline Case (using WorkManager)
