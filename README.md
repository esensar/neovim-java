# neovim-java - Java Client for Neovim API

[![Build Status](https://travis-ci.org/esensar/neovim-java.svg?branch=master)](https://travis-ci.org/esensar/neovim-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent)

Neovim Java is a Java Client for Neovim RPC API. Currently it is best suited for creating a GUI using Java and either an embedded Neovim instance or connecting to an exisitng instance either using Unix domain sockets or TCP sockets. Plugin development may be possible as well, but it was not tested.

This library consists of multiple modules and you may choose which one you want to use (depending on preferences and level of abstraction needed):
* [**Core RPC**](#core-rpc) - core of the library providing most basic interface for communicating with Neovim - it can be used without other modules, but other modules are recommended for most uses
 * [**Reactive Core RPC**](#reactive-core-rpc) - simple wrapper around Core RPC module for providing a reactive interface
 * [**Unix Socket Connection**](#unix-socket-connection) - simple addition of another RPCConnection for using unix domain sockets
 * [**Neovim API**](#neovim-api) - main interface for communicating with neovim. This is the recommended module to use, it provides a reactive interface and greatly simplified interface, providing complete implementations for all functions provided by Neovim.
 * [**Neovim RX API**](#neovim-rx-api) - small wrapper around Neovim API providing RxJava2 interface instead of Java reactive interface
 * [**Handler annotations**](#handler-annotations) - addition allowing creation of annotation based handlers for requests and notifications
 * [**Neovim notifications**](#neovim-notifications) - addition allowing usage of notifications through Java 9 Flows interface and also provides data models for all neovim notifications
 * [**API Explorer**](#api-explorer) - simple JavaFX application used for testing library and exploring neovim API

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
client.send(new RequestMessage.Builder("nvim_feedkeys").addArgument("jjjj").addArgument("").addArgument(false));

// Send get current line call and print out the response
client.send(new RequestMessage.Builder("nvim_get_current_line"), (id, response) -> System.out.println(response));
```

### Customizations

By default, `RPCClient` will use a sequential message id generator, meaning every new message will have its id increased by one. This makes it possible to match up response for given request. If you need a different behaviour, you may implement `MessageIdGenerator` interface and provide a custom `RPCStreamer` to `RPCClient` which will use that generator.

```java
MessageIdGenerator customGenerator = createCustomGenerator();

RPCStreamer customStreamer = new PackStream(sender, listener, customGenerator);
RPCStreamer client = new RPCClient.Builder()
    .withRPCStreamer(customStreamer)
    .build();
```

Similar to this, different parts of `RPCClient` can be changed using the builder:
1. `ExecutorService` used to listen for requests
1. `ObjectMapper` used for parsing requests and responses
1. `RPCSender` used to send messages
1. `RPCListener` used to listen for requests

### Info

If you wish to include this module (and not some other module), you may add it as your dependency:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>core-rpc</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:core-rpc:${neovimjava.version}'
```

## Reactive Core RPC
## Unix Socket Connection
## Neovim API
## Neovim RX API
## Handler annotations
## Neovim notifications
## API Explorer
