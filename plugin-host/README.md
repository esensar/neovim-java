# plugin-host

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/core-rpc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/plugin-host)
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/plugin-host)

This is the plugin host module, the main module when developing neovim remote plugins. It uses rest of the core modules to communicate with neovim and it provides annotations to easily define commands and autocommands. It can also be used to create hosted plugins, which can't be run on their own, but depend on [neovim-java-plugin-host](https://github.com/esensar/neovim-java-plugin-host).

It provides `NeovimJavaPluginHost` which holds everything needed to communicate with neovim. It provides a simple `start` method to connect to neovim via `stdio` (useful for plugins). It immediately loads API info, to be a able to provide `PluginApi` functionality, which enables easy command and autocommand creation.

This module also provides annotations for even easier command and autocommand creation (`@NeovimCommand` and `@NeovimAutocommand`).

Include it in your dependencies:
Maven:
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>plugin-host</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle:
```groovy
compile 'com.ensarsarajcic.neovim.java:plugin-host:${neovimjava.version}'
```

Example of usage:

```java
import com.ensarsarajcic.neovim.java.pluginhost.NeovimJavaPluginHost;

public final class Plugin {
    public static void main(String[] args) {
        var host = new NeovimJavaPluginHost();
        host.start().thenCompose(() -> {
            host.getApi().executeCommand("echo \"hello from java\"");
        });
    }
}
```

Example of hosted usage:

```java
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.pluginhost.NeovimJavaPluginHost;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimCommand;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimJavaHostedPlugin;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@NeovimJavaHostedPlugin
public final class HostedPlugin {
    private final NeovimJavaPluginHost host;

    public HostedPlugin(NeovimJavaPluginHost host) {
        this.host = host;
    }

    // Hook before initializing all the plugins
    // useful to add autocommand groups to be used with annotations
    public CompletableFuture<Void> prepare() {
        host.getApi().createAugroup("MyHostedPluginGroup", new HashMap<>());
    }

    // Hook after all plugins have been initialized
    // All hooks can either return CompletableFuture<Void> or void
    public void onReady() {
    }

    // All command handlers can either take `RequestMessage` or `CommandState`
    // NOTE: if sync = false, take `NotificationMessage` instead
    @NeovimCommand(value = "MyHostedPluginCommand", sync = true)
    public String incrementCalls(RequestMessage message)
            throws NeovimRequestException {
        // command handlers can throw NeovimRequestException to return error to neovim
        return "Hello from java";
    }

    // All autocommand handlers can either take `NotificationMessage` or `AutocommandState`
    // NOTE: if sync = true, take `RequestMessage` instead
    @NeovimAutocommand(value = {"BufRead"}, pattern = "*", group = "MyHostedPluginGroup")
    public void onBufRead(AutocommandState autocommandState) {
        System.err.println("Autocommand: " + autocommandState);
    }
}
```

**NOTE**: `@NeovimJavaHostedPlugin` annotation can be used in standalone examples too. Any class annotated with it will automatically be registered and any `@NeovimCommand` and `@NeovimAutocommand` annotations will be processed.

Check out `rplugin-example` and `rplugin-hosted-example` too.
