# handler-annotations

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/handler-annotations/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/handler-annotations)

Handler annotations module provides a way to register listeners for requests and notifications using annotations and different handlers. There needs to be an
object with methods annotated with `NeovimNotificationHandler` or `NeovimRequestHandler`. These methods need to take only one parameter, `NotificationMessage`
and `RequestMessage` respectively.

Include it in your dependencies:  
Maven:  
```
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>handler-annotations</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:  
```
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
