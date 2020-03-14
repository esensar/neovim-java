# neovim-api

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/neovim-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/neovim-api)

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
    
    RpcConnection localConnection = new TcpSocketRpcConnection(socket);
    NeovimApi api = NeovimApis.getApiForConnection(localConnection);
    
    api.getCurrentLine().thenAccept(System.out::println);
    
    NeovimApi another = new NeovimStreamApi(ReactiveRpcClient.getDefaultInstance());
```
