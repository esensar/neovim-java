# neovim-rx-api

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/neovim-rx-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/neovim-rx-api)

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
    
    RpcConnection localConnection = new TcpSocketRpcConnection(socket);
    NeovimApi api = NeovimApis.getApiForConnection(localConnection); // Create regular API
    NeovimRxApi rxApi = new NeovimRxWrapper(api); // And then wrap it in RxJava2 interface
    
    api.getCurrentLine().thenAccept(System.out::println);
    rxApi.getCurrentLine().subscribe(System.out::println);
```
