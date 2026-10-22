# Implementation Plan - Firebase Cloud Messaging Push Notifications Setup

Implement Firebase Cloud Messaging (FCM) push notification infrastructure so the app can receive and display manual notifications sent from the Firebase Console.

## User Review Required

> [!IMPORTANT]
> This will add the `firebase-messaging` dependency, configure a notification channel in `CSstudentApplication`, add a background messaging service, register it in the manifest, and subscribe the app to the `"announcements"` topic on startup.

## Proposed Changes

### App Module

#### [MODIFY] [build.gradle.kts](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/build.gradle.kts)
- Add `implementation("com.google.firebase:firebase-messaging")`.

#### [MODIFY] [CSstudentApplication.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/CSstudentApplication.kt)
- Create Android notification channel (`updates_channel_id`) in `onCreate()`.
- Subscribe to FCM topic `"announcements"`.

#### [NEW] [MyFirebaseMessagingService.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/MyFirebaseMessagingService.kt)
- Extend `FirebaseMessagingService` to handle `onMessageReceived()` and display local push notifications using `NotificationCompat.Builder`.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/AndroidManifest.xml)
- Register `MyFirebaseMessagingService` under `<application>`.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to verify compilation and resource resolution.
