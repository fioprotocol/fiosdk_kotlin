#!/bin/bash -x

IOS_SDK_VERSION=13.0
MIN_IOS_VERSION=11.0

# SELF=$(basename $0)
# BASE= .  # $(cd $(dirname $0); pwd -P)

# mkdir -p $BASE/target/native

CC=$(xcrun -f clang)
XCODE_PATH=$(xcode-select --print-path)
DEVICE_SDK=/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS13.1.sdk/ #$XCODE_PATH/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS$IOS_SDK_VERSION.sdk
SIM_SDK=$XCODE_PATH/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator$IOS_SDK_VERSION.sdk

#-I/Users/davidfaerman/dapix2/ioscpplib/coinomiios/robovm/robovm/compiler/vm/core/include

#$CC -arch arm64  -miphoneos-version-min=$MIN_IOS_VERSION -isysroot $DEVICE_SDK -o $BASE/target/native/init-arm64.o  -c $BASE/src/main/native/init.m -fembed-bitcode
#$CC -arch armv7  -miphoneos-version-min=$MIN_IOS_VERSION -isysroot $DEVICE_SDK -o $BASE/target/native/init-armv7.o  -c $BASE/src/main/native/init.m -fembed-bitcode
#$CC -arch i386   -miphoneos-version-min=$MIN_IOS_VERSION -isysroot $SIM_SDK    -o $BASE/target/native/init-x86.o    -c $BASE/src/main/native/init.m
#$CC -std=c++17 -arch arm64 -miphoneos-version-min=$MIN_IOS_VERSION -isysroot $DEVICE_SDK -I/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS13.1.sdk/usr/include -I/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers/   -o init--arm64.o -c abieos-lib.cpp -fembed-bitcode -Wwritable-strings
$CC -std=c++17 -arch armv7 -miphoneos-version-min=$MIN_IOS_VERSION -isysroot $DEVICE_SDK -I/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS13.1.sdk/usr/include -I/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers/   -o init-armv7.o  -c abieos-lib.cpp -fembed-bitcode -Wwritable-strings
$CC -std=c++17 -arch x86_64 -miphoneos-version-min=$MIN_IOS_VERSION -isysroot $DEVICE_SDK -I/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers/  -I/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include/  -o init-x86_64.o -c abieos-lib.cpp -Wwritable-strings

lipo init-*.o -create -output init.o

#/Users/davidfaerman/dapix2/ioscpplib/cpp
#-I/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include/