# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

## [0.4.1] - 2022-06-04

- `plugin-host` module for both standalone and hosted plugins
- `plugins-common-host` for hosted plugins

## [0.4.0] - 2022-05-27

### Added
- Neovim API v7 support
- Neovim API v8 support
- Neovim API v9 support

**NOTES:**
- Rx-Api is no longer being maintained. It should be easy enough to wrap the regular API

## [0.3.0] - 2022-05-26

### Added
- JDK 17 support
- `neovim-notifications`: global error event
- `api-explorer`: support for custom neovim executable path

**BREAKING CHANGES**:
- Updated module names to proper canonical name (e.g. `com.ensarsarajcic.neovim.java.corerpc` instead of `corerpc`)

## [0.2.3] - 2021-02-07

### Fixed
- Issues with `MultiGrid` events in `neovim-notifications` ([#119][i119] - thanks @smolck)

**BREAKING CHANGES**:
- Removed duplicate method from `NeovimJacksonModule` (for retrieving default mapper, since it is available in `ObjectMappers`).
- Callers should move to using `ObjectMappers`.
- Consider using `NeovimApis` instead of manually building `ReactiveRpcClient`.

## [0.2.2] - 2021-02-07

### Added
- Missing UiOptions ([#119][i119])

## [0.2.1] - 2021-02-06

### Fixed
- Issues with `UIEvent` deserialization - this makes `neovim-notifications` usable

### Added
- Missing UI events

## [0.2.0] - 2020-09-09

### Added
- `rplugin-example` - basic example of vim rplugin

### Changed
- Style fixes
- Naming standardization

## [0.1.16] - 2020-03-14

### Added
- Neovim API v6 support

## [0.1.15] - 2020-03-04

### Changed
- JDK 11 (LTS) compatibility

## [0.1.14] - 2020-03-03

### Added
- JDK 13 support
- Neovim API v5 support

### Fixed
- Fix `nvim_call_function` arguments type ([#96][i96])

## [0.1.13] - 2018-12-07

- JDK 11 support
- OpenJFX in `api-explorer`
- Proper release of [#77][i77]

## [0.1.12] - 2018-12-07

### Added
- Ability to release resources from both sender and listener ([#77][i77])

## [0.1.11] - 2018-10-25

### Fixed
- Fixed mapping for msgpack types when using default NeovimApi

## [0.1.10] - 2018-09-29

- Proper maven central release, no code changes since `0.1.0`

## 0.1.0 - 2018-09-29
### Added
- `core-rpc` module for basic neovim communication
- `reactive-core-rpc` for reactive flows support
- `neovim-api` basic high level API
- `neovim-rx-api` RxJava support for `neovim-api`
- `neovim-notifications` support for neovim notification
- `handler-annotations` for easier way of implementing requests and notifications
- `api-explorer` JavaFX application for exploring Neovim API functions

[Unreleased]: https://github.com/esensar/neovim-java/compare/0.4.1...main
[0.1.10]: https://github.com/esensar/neovim-java/compare/0.1...0.1.10
[0.1.11]: https://github.com/esensar/neovim-java/compare/0.1.10...0.1.11
[0.1.12]: https://github.com/esensar/neovim-java/compare/0.1.11...0.1.12
[0.1.13]: https://github.com/esensar/neovim-java/compare/0.1.12...0.1.13
[0.1.14]: https://github.com/esensar/neovim-java/compare/0.1.13...0.1.14
[0.1.15]: https://github.com/esensar/neovim-java/compare/0.1.14...0.1.15
[0.1.16]: https://github.com/esensar/neovim-java/compare/0.1.15...0.1.16
[0.2.0]: https://github.com/esensar/neovim-java/compare/0.1.16...0.2.0
[0.2.1]: https://github.com/esensar/neovim-java/compare/0.2.0...0.2.1
[0.2.2]: https://github.com/esensar/neovim-java/compare/0.2.1...0.2.2
[0.2.3]: https://github.com/esensar/neovim-java/compare/0.2.2...0.2.3
[0.3.0]: https://github.com/esensar/neovim-java/compare/0.2.3...0.3.0
[0.4.0]: https://github.com/esensar/neovim-java/compare/0.3.0...0.4.0
[0.4.1]: https://github.com/esensar/neovim-java/compare/0.4.0...0.4.1
[i77]: https://github.com/esensar/neovim-java/issues/77
[i96]: https://github.com/esensar/neovim-java/issues/96
[i119]: https://github.com/esensar/neovim-java/issues/119
