cmake_minimum_required(VERSION 3.4.1)

set(64_BIT NO)
if (ARCH STREQUAL "x86_64")
elseif (ARCH STREQUAL "x86")
elseif (ARCH STREQUAL "thumbv7")
elseif (ARCH STREQUAL "arm64")
else()
  message(FATAL_ERROR "Unspecified/Unknown arch! ${ARCH}")
endif()

message(STATUS "ARCH: ${ARCH}")

exec_program(xcode-select ARGS --print-path OUTPUT_VARIABLE XCODE_PATH)
if(ARCH MATCHES "^(x86|x86_64)$")
  set(SYSROOT "${XCODE_PATH}/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk")
  if(NOT IS_DIRECTORY ${SYSROOT})
    message(FATAL_ERROR "Failed to locate iPhoneSimulator SDK")
  endif()
else()
  set(SYSROOT "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS13.1.sdk") 
  # "${XCODE_PATH}/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk")
  if(NOT IS_DIRECTORY ${SYSROOT})
    message(FATAL_ERROR "Failed to locate iPhoneOS SDK")
  endif()
endif()

# std gnu++17 requires ios12 min, as result it supports only 64 bit targets 
set(IOS_MIN_VERSION 12.0)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++17 -fPIC -arch ${ARCH} -miphoneos-version-min=${IOS_MIN_VERSION} -isysroot ${SYSROOT}")
set(CMAKE_OSX_SYSROOT "${SYSROOT}")

# Linker flags
set(LD_FLAGS "${LD_FLAGS} -fPIC")
set(LD_FLAGS "${LD_FLAGS} -arch ${ARCH}")
set(LD_FLAGS "${LD_FLAGS} -isysroot ${SYSROOT}")
set(LD_FLAGS "${LD_FLAGS} -miphoneos-version-min=${IOS_MIN_VERSION}")


add_library( # Sets the name of the library.
        abieos-lib

        # Sets the library as a shared library.
        STATIC

        # Provides a relative path to your source file(s).
        abieos-lib.cpp
        abieos.cpp)

target_include_directories(abieos-lib PRIVATE rapidjson/include date/include)

set(INSTALL_DIR ${CMAKE_SOURCE_DIR}/target/binaries/${ARCH})
install(TARGETS abieos-lib DESTINATION ${INSTALL_DIR})