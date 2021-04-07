# Vcpkg GUI manager

1. Clone this repository.
2. Setup local Vcpkg instance.
3. Run the application.

## Setup (Linux, Mac OS)
```
./setup.sh
```

## Setup (Windows)
```
setup.bat
```

## Run (Linux, Max OS)
```
./gradlew run
```

## Run (Windows)
```
gradlew.bat run
```

## Description
    - At first, provide the path to the directory,
      containing vcpkg executable (setup creates vcpkg
      instance in ./test/vcpkg), and press `Set root' button
    - `Update packages list' button refreshes the list
      of installed packages.
    - `Search' button starts the search of package by name
      provided in text field.
    - `Cancel' button cancels the current operation.
    - `Install' button installs all chosen packages.
    - `Remove' button removes all chosen packages.
    - To choose packages, click on any of them in lists
      on the right side.
    