# fiosdk_kotlin
Kotlin SDK

#Build

Make sure your project is synced.  This should happen automatically, but if it didn't, go to
Files->Sync Project with Gradle Files

To build the Kotlin SDK project, select "Build" from the Android Studio menu and select
"Rebuild Project"

#Documentation

To create the documentation, open a terminal and run ./gradlew dokka

The documentation files will be located in the "documentation" folder in the root of the project.

#Running Unit Tests

The unit tests are located in the "app" project under the "androidTest" folder.  These are
instrumented tests and will need to run in an emulator or on a device.

To run a test, proceed as follows:

    Be sure your project is synchronized with Gradle by clicking Sync Project in the toolbar.
    Run your test in one of the following ways:
        In the Project window, right-click a test and click Run .

        In the Code Editor, right-click a class or method in the test file and click Run to
        test all methods in the class.

        To run all tests, right-click on the test directory and click Run tests .

By default, your test runs using Android Studio's default run configuration.  If you'd like to
change some run settings such as the instrumentation runner and deployment options, you can
edit the run configuration in the Run/Debug Configurations dialog (click Run > Edit Configurations).
