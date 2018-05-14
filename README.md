# Fileio Android App

This simple app allows you to upload any fileEntity and get a _sharable_ link with a set expiration time.
The fileEntity will be **Deleted** after its downloaded or after expiration time (regardless of whether its downloaded or not).
This app is made with the help of [fileEntity.io](https://fileEntity.io) which is an **_Anonymous_, _Secure_** fileEntity sharing platform by [Humbly](http://humbly.com/).

## Screenshots 📸
<p float="left">
<img src="/screenshots/sc1.png" alt="Home Screen"  height="600"/>

## Libraries Used ❤️
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
- [Permission Dispatcher](https://permissions-dispatcher.github.io/PermissionsDispatcher/)
- [ButterKnife](http://jakewharton.github.io/butterknife/)
- [Fuel](https://github.com/kittinunf/Fuel)
- [NumberProgressBar](https://github.com/daimajia/NumberProgressBar)
- [FirebaseCrashlytics](https://firebase.google.com/docs/crashlytics)
- [Room](https://developer.android.com/topic/libraries/architecture/room.html)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
- [CustomActivityOnCrash](https://github.com/Ereza/CustomActivityOnCrash)

Vector Images from [FlatIcon](https://www.flaticon.com/).

## TODO List ✅:
- [X] Set Expiration Time
- [X] Handle different use cases for storage on the device.
- [X] Handle Different File Types and Different File Managers (**BUG!!**)
- [X] Progress Bar for File Upload Progress
- [X] Custom Error Dialog
- [X] restructure App with **MVVM** (kinda maybe!)😉
- [X] UI Testing
- [ ] Support for *Multiple* File Upload (Create a List)
- [ ] App Icon
- [ ] Kotlinize the project 🎳
