# neovim-java - Java Client for Neovim API

[![Build Status](https://travis-ci.org/esensar/neovim-java.svg?branch=master)](https://travis-ci.org/esensar/neovim-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent)
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java)

Neovim Java is a Java Client for Neovim RPC API. Currently it is best suited for creating a GUI using Java and either an embedded Neovim instance or connecting to an exisitng instance either using Unix domain sockets or TCP sockets. Plugin development may be possible as well, but it was not tested.

This library consists of multiple modules and you may choose which one you want to use (depending on preferences and level of abstraction needed):
* [**Core RPC**](#core-rpc) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/core-rpc) - core of the library providing most basic interface for communicating with Neovim - it can be used without other modules, but other modules are recommended for most uses
 * [**Reactive Core RPC**](#reactive-core-rpc) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/reactive-core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/reactive-core-rpc) - simple wrapper around Core RPC module for providing a reactive interface
 * [**Unix Socket Connection**](#unix-socket-connection) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/unix-socket-connection/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/unix-socket-connection) - simple addition of another RPCConnection for using unix domain sockets
 * [**Neovim API**](#neovim-api) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-api) - main interface for communicating with neovim. This is the recommended module to use, it provides a reactive interface and greatly simplified interface, providing complete implementations for all functions provided by Neovim.
 * [**Neovim RX API**](#neovim-rx-api) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-rx-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-rx-api) - small wrapper around Neovim API providing RxJava2 interface instead of Java reactive interface
 * [**Handler annotations**](#handler-annotations) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/handler-annotations/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/handler-annotations) - addition allowing creation of annotation based handlers for requests and notifications
 * [**Neovim notifications**](#neovim-notifications) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-notifications/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-notifications) - addition allowing usage of notifications through Java 9 Flows interface and also provides data models for all neovim notifications
 * [**API Explorer**](#api-explorer) [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/api-explorer/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/api-explorer) - simple JavaFX application used for testing library and exploring neovim API

## Core RPC 
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/core-rpc)
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
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/reactive-core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/reactive-core-rpc)
Reactive core RPC module is a simple wrapper around core RPC module. It provides a reactive interface using Java 9 Flows. Requests and notifications
are provided through a `Flow`, while sending messages returns a `CompletableFuture` which can return a response.

Include it in your dependencies:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>reactive-core-rpc</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:reactive-core-rpc:${neovimjava.version}'
```

It handles the same `RPCConnection` interface as core RPC module and creation is very similar:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RPCConnection localConnection = new TcpSocketRPCConnection(socket);
    ReactiveRPCStreamer rpcStreamer = ReactiveRPCClient.getDefaultInstance(); // shared singleton
    
    rpcStreamer.attach(localConnection);
    Message request = new RequestMessage.Builder("nvim_get_current_line");
    rpcStreamer.response(request).thenAccept(System.out::println); // requesting
    
    rpcStreamer.notificationsFlow().subscribe(notificationsSubscriber); // notifications subscription
```

RxJava wrapper is not provided, since it is very easy to wrap this Java 9 Flow implementation into an RxJava implementation.

## Unix Socket Connection
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/unix-socket-connection/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/unix-socket-connection)
Unix socket connection module provides a very simple additional implementation of `RPCConnection` based on Unix domain sockets.

Include it in your dependencies:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>unix-socket-connection</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:unix-socket-connection:${neovimjava.version}'
```

Example usage:
```java
    File socket = new File("/var/nvim/random");

    RPCConnection fileConnection = new UnixDomainSocketRPCConnection(socket);

    // It can now be used for communication
    rpcStreamer.attach(fileConnection);
    rpcStreamer.sent(message); // send a message to unix domain socket located on /var/nvim/random
```

## Neovim API
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-api)
This is the main high level interface for this library. It provides all of the Neovim RPC API functions in an easy to use way. It holds all types as models
and provides 4 separate **APIs**: *Neovim*, *Buffer*, *Tabpage* and *Window* (the way they were meant to be used in OOP languages).

> This does not proivide atomic calls yet.

All calls are made using *reactive-core-rpc* module and all calls return `CompletableFuture`. If you prefer RxJava over Java 9 Flows, you can use
*neovim-rx-api* module.

Include it in your dependencies:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-api</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:neovim-api:${neovimjava.version}'
```

Example usage:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RPCConnection localConnection = new TcpSocketRPCConnection(socket);
    NeovimApi api = NeovimApis.getApiForConnection(localConnection);
    
    api.getCurrentLine().thenAccept(System.out::println);
    
    NeovimApi another = new NeovimStreamApi(ReactiveRPCClient.getDefaultInstance());
```

## Neovim RX API
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-rx-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-rx-api)
Neovim RX api module provides a same API as neovim API module, but using RxJava2.

Include it in your dependencies:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-rx-api</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:neovim-rx-api:${neovimjava.version}'
```

Example:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RPCConnection localConnection = new TcpSocketRPCConnection(socket);
    NeovimApi api = NeovimApis.getApiForConnection(localConnection); // Create regular API
    NeovimRxApi rxApi = new NeovimRxWrapper(api); // And then wrap it in RxJava2 interface
    
    api.getCurrentLine().thenAccept(System.out::println);
    rxApi.getCurrentLine().subscribe(System.out::println);
```

## Handler annotations
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/handler-annotations/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/handler-annotations)
Handler annotations module provides a way to register listeners for requests and notifications using annotations and different handlers. There needs to be an
object with methods annotated with `NeovimNotificationHandler` or `NeovimRequestHandler`. These methods need to take only one parameter, `NotificationMessage`
and `RequestMessage` respectively.

Include it in your dependencies:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>handler-annotations</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:handler-annotations:${neovimjava.version}'
```

Example usage:
```java
    class AHandlerClass {
    
        @NeovimNotificationHandler("redraw")
        public void handleRedraw(NotificationMessage message) {
            System.out.println("Received a message: " + message);
        }
    }
    
    NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager();

    neovimHandlerManager.registerNeovimHandler(new AHandlerClass());
    neovimHandlerManager.attachToStream(neovimStream);
```

By default, all notifications and requests will be blocking. If you need a different behaviour, you can use a different constructor for `NeovimHandlerManager`.
```java
    NeovimHandlerManager customHandlerManager = new NeovimHandlerManager(new NeovimHandlerProxy(customExecutorService));
```

## Neovim notifications
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-notifications/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-notifications)
Neovim notifications module provides a way to receive notifications through Java 9 Flows. Besides that it provides models for all neovim notifications,
which can only be checked using *instanceof* operator currently, but provide an easier way to parse data.

Include it in your dependencies:  
Maven:  
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-notifications</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```groovy
compile 'com.ensarsarajcic.neovim.java:neovim-notifications:${neovimjava.version}'
```

Example usage:
```java
    NeovimNotificationHandler notificationHandler = new NeovimNotificationHandler(streamer);

    notificationHandler.uiEvents().subscribe(uiEventSubscriber);
    notificationHandler.bufferEvents().subscribe(bufferEventSubscriber);
```

## API Explorer
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/api-explorer/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/api-explorer)
Simple JavaFX application loading Neovim API information using `nvim --api-info`. Displays all loaded information in tables for simple overview.

It can also be used as an example of usage of library.
