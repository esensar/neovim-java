# neovim-java

[![CI](https://ci.codeberg.org/api/badges/neovim-java/neovim-java/status.svg)](https://ci.codeberg.org/neovim-java/neovim-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ensarsarajcic.neovim.java/parent)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.ensarsarajcic.neovim.java/parent?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/com/ensarsarajcic/neovim/java/)
[![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java)

Neovim Java client

Consists of following modules:
 * **Core RPC** [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/core-rpc) - core of the library providing most basic interface for communicating with Neovim - it can be used without other modules, but other modules are recommended for most uses
 * **Reactive Core RPC**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/reactive-core-rpc/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/reactive-core-rpc) - simple wrapper around Core RPC module for providing a reactive interface
 * **Unix Socket Connection**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/unix-socket-connection/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/unix-socket-connection) - simple addition of another RPCConnection for using unix domain sockets
 * **Neovim API**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-api) - main interface for communicating with neovim. This is the recommended module to use, it provides a reactive interface and greatly simplified interface, providing complete implementations for all functions provided by Neovim.
 * **Neovim Rx API**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-rx-api/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-rx-api) - small wrapper around Neovim API providing RxJava2 interface instead of Java reactive interface
 * **Handler annotations**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/handler-annotations/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/handler-annotations) - addition allowing creation of annotation based handlers for requests and notifications
 * **Neovim notifications**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/neovim-notifications/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/neovim-notifications) - addition allowing usage of notifications through Java 9 Flows interface and also provides data models for all neovim notifications
 * **API Explorer**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/api-explorer/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/api-explorer) - simple JavaFX application used for testing library and exploring neovim API
 * **Plugin host**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/plugin-host/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/plugin-host) - library for building remote plugins - this should be the main dependency when developing Neovim plugins (either standalone or hosted by `plugins-common-host`) - enables easy usage with [neovim-java-plugin-host](https://codeberg.org/neovim-java/neovim-java-plugin-host)
 * **Plugins common host**  [![javadoc](https://javadoc.io/badge2/com.ensarsarajcic.neovim.java/plugins-common-host/javadoc.svg)](https://javadoc.io/doc/com.ensarsarajcic.neovim.java/plugins-common-host) - executable which can be used as a remote plugin in Neovim, which should load all other plugins from classpath (provided by [neovim-java-plugin-host](https://codeberg.org/neovim-java/neovim-java-plugin-host))

Many features are not completely tested and this library was mostly built to experiment with Neovim RPC API, but there should be no issues with using this library.

To include this library right away and start working with Neovim, **Neovim API** is enough.

Include it in your dependencies:
Maven(*Neovim API*):
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-api</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle(*Neovim API*):
```groovy
compile 'com.ensarsarajcic.neovim.java:neovim-api:${neovimjava.version}'
```

Or if you prefer RxJava2, use **Neovim Rx API**:
Maven(*Neovim Rx API*):
```xml
<dependency>
  <groupId>com.ensarsarajcic.neovim.java</groupId>
  <artifactId>neovim-rx-api</artifactId>
  <version>${neovimjava.version}</version>
</dependency>
```
Gradle(*Neovim Rx API*):
```groovy
compile 'com.ensarsarajcic.neovim.java:neovim-rx-api:${neovimjava.version}'
```

If you wish to use latest SNAPSHOT version, it is available in Sonatype snapshots repository.

For more information, check out README of modules you are interested in.

## Remote plugin support
This repository contains a small remote plugin example in `rplugin-example`. To run this example run `mvn package` and run Neovim with provided vimrc by calling `nvim -u rplugin-example/vimrc` from project root.

It should be possible to then call the `:NeovimJavaIncrementCount` command which communicated with java plugin.

It is an example of a standalone rplugin which should provide its own configuration.

`rplugin-hosted-example` is an example of a hosted plugin which can be easily loaded with [neovim-java-plugin-host](https://codeberg.org/neovim-java/neovim-java-plugin-host)

## Contributing

Check out [contributing guidelines](CONTRIBUTING.md).

## License

[MIT](LICENSE)
