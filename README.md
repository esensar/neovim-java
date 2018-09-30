# neovim-java - Java Client for Neovim API

[![Build Status](https://travis-ci.org/esensar/neovim-java.svg?branch=master)](https://travis-ci.org/esensar/neovim-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent)

Neovim Java is a Java Client for Neovim RPC API. Currently it is best suited for creating a GUI using Java and either an embedded Neovim instance or connecting to an exisitng instance either using Unix domain sockets or TCP sockets. Plugin development may be possible as well, but it was not tested.

This library consists of multiple modules and you may choose which one you want to use (depending on preferences and level of abstraction needed):
* **Core RPC** - core of the library providing most basic interface for communicating with Neovim - it can be used without other modules, but other modules are recommended for most uses
 * **Reactive Core RPC** - simple wrapper around Core RPC module for providing a reactive interface
 * **Unix Socket Connection** - simple addition of another RPCConnection for using unix domain sockets
 * **Neovim API** - main interface for communicating with neovim. This is the recommended module to use, it provides a reactive interface and greatly simplified interface, providing complete implementations for all functions provided by Neovim.
 * **Neovim RX API** - small wrapper around Neovim API providing RxJava2 interface instead of Java reactive interface
 * **Handler annotations** - addition allowing creation of annotation based handlers for requests and notifications
 * **Neovim notifications** - addition allowing usage of notifications through Java 9 Flows interface and also provides data models for all neovim notifications
 * **API Explorer** - simple JavaFX application used for testing library and exploring neovim API

## Core RPC
Core of the library with a basic interface for RPC communication through a `RPCConnection`. This implements basic Neovim API definition, without any specific types or functions. It can be used if functions to be used may not be known beforehand. All of the classes in this module may be replaced with a different implementation if special behaviour is required.

Basic usage:
```java
// Get an instance of RPCStreamer
RPCStreamer client = RPCClient.getDefaultAsyncInstance();

// Prepare a connection to neovim
ProcessBuilder pb = new ProcessBuilder("nvim", "--embed");
Process neovim = pb.start(); // Start embedded neovim instance

RPCConnection neovimConnection = new ProcessRPCConnection(neovim, true); // true to kill neovim once connection is closed

// Connect to neovim
client.attach(neovimConnection);

// Send feed keys call
client.send(new RequestMessage.Builder("nvim_feedkeys").addArgument("jjjj").addArgument('').addArgument(false));
```

## Reactive Core RPC
## Unix Socket Connection
## Neovim API
## Neovim RX API
## Handler annotations
## Neovim notifications
## API Explorer
