# unix-socket-connection

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/unix-socket-connection/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/unix-socket-connection)

Unix socket connection module provides a very simple additional implementation of `RPCConnection` based on Unix domain sockets.

Include it in your dependencies:  
Maven:  
```
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>unix-socket-connection</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```
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
