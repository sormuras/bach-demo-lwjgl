# ⌨ Bach + Lightweight Java Game Library

A demo of building a modular application with [Bach] based on [GET STARTED/HelloWorld](https://www.lwjgl.org/guide) by LW**JGL** 3.

## Prepare

- Download and install [JDK] 22 or later
- Clone this repository with submodules

## Build

Change into the root directory of your cloned project and call:

```shell script
java @build
```

## Run via Java Launcher

- Linux/Mac
```shell script
java --module-path .bach/out/main/modules:lib --module demo
```

- Windows
```shell script
java --module-path .bach\out\main\modules;lib --module demo
```

## Run via custom runtime image

- Linux/Mac
```shell script
.bach/out/main/image/bin/demo
```

- Windows
```shell script
.bach\out\main\image\bin\demo
```


[Bach]: https://github.com/sormuras/bach
[JDK]: https://jdk.java.net
