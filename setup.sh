#!/bin/bash
set -e

mkdir test
cd test
git clone https://github.com/microsoft/vcpkg
cd vcpkg
./bootstrap-vcpkg.sh