# neovim-java

[![Build Status](https://travis-ci.org/esensar/neovim-java.svg?branch=master)](https://travis-ci.org/esensar/neovim-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent)

Neovim Java client

Consists of following modules:
 * **Core RPC** - core of the library providing most basic interface for communicating with Neovim - it can be used without other modules, but other modules are recommended for most uses
 * **Reactive Core RPC** - simple wrapper around Core RPC module for providing a reactive interface
 * **Unix Socket Connection** - simple addition of another RPCConnection for using unix domain sockets
 * **Neovim API** - main interface for communicating with neovim. This is the recommended module to use, it provides a reactive interface and greatly simplified interface, providing complete implementations for all functions provided by Neovim.
 * **Neovim Rx API** - small wrapper around Neovim API providing RxJava2 interface instead of Java reactive interface
 * **Handler annotations** - addition allowing creation of annotation based handlers for requests and notifications
 * **Neovim notifications** - addition allowing usage of notifications through Java 9 Flows interface and also provides data models for all neovim notifications
 * **API Explorer** - simple JavaFX application used for testing library and exploring neovim API

Many features are not completely tested and this library was mostly built to experiment with Neovim RPC API, but there should be no issues with using this library.

To include this library right away and start working with Neovim, **Neovim API** is enough.  

Include it in your dependencies:  
Maven(*Neovim API*):  
```
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-api</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle(*Neovim API*):  
```
compile 'com.ensarsarajcic.neovim.java:neovim-api:${neovimjava.version}'
```

Or if you prefer RxJava2, use **Neovim Rx API**:  
Maven(*Neovim Rx API*):  
```
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-rx-api</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle(*Neovim Rx API*):  
```
compile 'com.ensarsarajcic.neovim.java:neovim-rx-api:${neovimjava.version}'
```

For more information, check out README of modules you are interested in.
