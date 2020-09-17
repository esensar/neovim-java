# reactive-core-rpc

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/reactive-core-rpc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/reactive-core-rpc)
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-rx-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-rx-api)

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

It handles the same `RpcConnection` interface as core Rpc module and creation is very similar:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RpcConnection localConnection = new TcpSocketRpcConnection(socket);
    ReactiveRpcStreamer rpcStreamer = ReactiveRpcClient.getDefaultInstance(); // shared singleton
    
    rpcStreamer.attach(localConnection);
    Message request = new RequestMessage.Builder("nvim_get_current_line");
    rpcStreamer.response(request).thenAccept(System.out::println); // requesting
    
    rpcStreamer.notificationsFlow().subscribe(notificationsSubscriber); // notifications subscription
```

RxJava wrapper is not provided, since it is very easy to wrap this Java 9 Flow implementation into an RxJava implementation.
