# neovim-notifications

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/neovim-notifications/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/neovim-notifications)

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
