# OurCraft
Minecraft clone developed for software engineering course. Written in Java using LWJGL library with OpenGL bindings and JOML library for matrix transformations.

## Features:
 * auto generated terrain with rivers and oceans
 * option to pass random seed to world generator
 * static and animated block textures
 * saving world with all modifications to disk
 * achievements system
 * simple day/night cycle

## Running project
This project requires that you have [Java JRE](https://www.oracle.com/technetwork/java/javase/downloads/index.html) version 8 or higher installed on your system.
To build and run the project you also have to install [Gradle](https://gradle.org/install/) build tool.

First clone the repository:
```
git clone git@github.com:PixelCatalyst/OurCraft.git
cd OurCraft
```

Then run the game using Gradle:
```
gradle run
```

## Runing tests
You can run unit tests with Gradle task:
```
gradle test
```
During the tests a lot of small windows will be created in order to provide OpenGL context.

## Documentation
Most important things [here](docs/DocLang.md).
