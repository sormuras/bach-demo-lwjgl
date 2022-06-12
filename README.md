# ⌨ Bach + Lightweight Java Game Library

A demo of building a modular application with [Bach] based on [GET STARTED/HelloWorld](https://www.lwjgl.org/guide) by LW**JGL** 3.

## Prepare

- Download and install [JDK] 17 or later
- Clone this repository

## Build

Change into the root directory of your cloned project and call:

- Linux/Mac
```shell script
.bach/bin/bach build
```

- Windows
```shell script
.bach\bin\bach build
```
## Run via Java Launcher

- Linux/Mac
```shell script
java --module-path .bach/out/main/modules:.bach/external-modules --module com.github.sormuras.bach.lwjgl
```

- Windows
```shell script
java --module-path .bach\out\main\modules;.bach\external-modules --module com.github.sormuras.bach.lwjgl
```

## Run via custom runtime image

- Linux/Mac
```shell script
.bach/out/main/image/bin/bach-lwjgl
```

- Windows
```shell script
.bach\out\main\image\bin\bach-lwjgl
```


[Bach]: https://github.com/sormuras/bach
[JDK]: https://jdk.java.net
