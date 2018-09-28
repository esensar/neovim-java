# neovim-notifications

Neovim notifications module provides a way to receive notifications through Java 9 Flows. Besides that it provides models for all neovim notifications,
which can only be checked using *instanceof* operator currently, but provide an easier way to parse data.

```java
    NeovimNotificationHandler notificationHandler = new NeovimNotificationHandler(streamer);

    notificationHandler.uiEvents().subscribe(uiEventSubscriber);
    notificationHandler.bufferEvents().subscribe(bufferEventSubscriber);
```