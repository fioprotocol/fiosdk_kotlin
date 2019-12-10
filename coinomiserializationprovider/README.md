# Coinomi FIO Serialization Provider

Before building the app for the first time, ensure the following is installed:

CMake 3.4.1
XCode 11.0 or higher

# Building the c++ for ios

    cd src/main/ios-cpp
    sh build.sh

# Resulting files

The build.sh script builds the "libabieos-lib.a" file located at src/main/ios-cpp/target/binaries.
There is a "libabieos-lib.a" for each supported platform.  The "libabieos-lib.a" in the root of
the "binaries" folder supports all platforms.

#Building the Serialization Provider

From the build menu in Visual Studio, select "Make Module".

The serialization provider is also built when the entire project is built.