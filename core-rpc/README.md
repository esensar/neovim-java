# core-rpc

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/core-rpc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/core-rpc)

This is the Core RPC module, the core of this library. It provides the most basic interface for communicating with Neovim.

It provides *Request*, *Response* and *Notification* messages, and a way to send and receive them. It uses `RPCConnection` interface for
communication which just provides input and ouput streams. This module provides basic implementations of that interface allowing communication
either through **TCP socket** or throguh **process** (used for embedded neovim instance).

Include it in your dependencies:  
Maven:  
```
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>core-rpc</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```
compile 'com.ensarsarajcic.neovim.java:core-rpc:${neovimjava.version}'
```

Example of usage:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RPCConnection localConnection = new TcpSocketRPCConnection(socket);
    RPCStreamer rpcStreamer = RPCClient.getDefaultAsyncInstance(); // shared singleton
    
    rpcStreamer.attach(localConnection);
    Message request = new RequestMessage.Builder("nvim_get_current_line");
    rpcStreamer.send(request, (id, response) -> System.out.println(response)); // callback for request
    
    rpcStreamer.send(request); // Sending a request - fire and forget - no callback
```

You can also implement `RPCListener` and `RPCSender` and use these implementations instead for `RPCClient`:
```java
    RPCStreamer customSenderListenerClient = new RPCClient.Builder()
        .withRPCListener(customRPCListener)
        .withRPCSender(customRPCSender)
        .build();
```

Or you can implement `RPCStreamer` in which case, `RPCClient` is just a basic proxy:
```java
    RPCStreamer customClient = new RPCClient.Builder()
        .withRPCStreamer(customStreamer)
        .build();
```
