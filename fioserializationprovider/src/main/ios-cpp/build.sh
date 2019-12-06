#!/bin/bash

SELF=$(basename $0)
BASE=$(cd $(dirname $0); pwd -P)
CLEAN=0

# thumbv7, x86 will not build for min ios version 12 
# ARCHES="x86_64 x86 thumbv7 arm64"
ARCHES="arm64 x86_64"

libs=""
for ARCH in $ARCHES; do
  rm -rf "$BASE/target/build/ios-$ARCH"
  mkdir -p "$BASE/target/build/ios-$ARCH"
  rm -rf "$BASE/target/binaries/$ARCH"
  bash -c "cd '$BASE/target/build/ios-$ARCH'; cmake -DARCH=$ARCH '$BASE'; make install"
  R=$?
  if [[ $R != 0 ]]; then
    echo "$T-$B build failed"
    exit $R
  fi
  libs="$libs $BASE/target/binaries/$ARCH/libabieos-lib.a"
done

# making the fat lib 
lipo -create $libs -output "$BASE/target/binaries/libabieos-lib.a"
R=$?
if [[ $R != 0 ]]; then
  echo "$T-$B lipo failed"
  exit $R
fi