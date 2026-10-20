# Implementation Plan - Extract Hardcoded Strings to strings.xml

Extract all hardcoded user-facing strings across all Compose screens into `res/values/strings.xml` and replace them with `stringResource(R.string...)` references to facilitate future multi-language localization.

## User Review Required

> [!IMPORTANT]
> This refactoring will centralize all UI text strings into `res/values/strings.xml` without changing any behavior or UI styling.

## Open Questions

- None.

## Proposed Changes

### Resources
#### [MODIFY] [strings.xml](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/res/values/strings.xml)
- Add string keys for Add screen, Login screen, Navigation bar, Settings screen, and Module detail categories.

### Screens & UI Components
#### [MODIFY] [Add.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/screens/Add.kt)
- Replace hardcoded button, dialog, and form labels with `stringResource(...)`.

#### [MODIFY] [Login.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/screens/Login.kt)
- Replace login titles, descriptions, and button texts with `stringResource(...)`.

#### [MODIFY] [NavBottom.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/screens/NavBottom.kt)
- Replace bottom navigation item labels with `stringResource(...)`.

#### [MODIFY] [Settings.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/screens/Settings.kt)
- Replace account title, sign out button, and confirmation dialog texts with `stringResource(...)`.

#### [MODIFY] [ModuleDetailScreen.kt](file:///C:/Users/rocdz/AndroidStudioProjects/CSstudent/app/src/main/java/com/zineeddine/cs_student/ui/screens/ModuleDetailScreen.kt)
- Replace category titles (`Documents`, `Videos`, `Useful Links`) with `stringResource(...)`.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` and `:app:assembleRelease` to verify successful compilation and resource resolution.
