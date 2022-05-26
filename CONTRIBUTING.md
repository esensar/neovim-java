# Contributing to kotlinx.serialization.msgpack

First of all, thanks for taking the time to contribute to the project!

Following is a basic set of guidelines for contributing to this repository and instructions to make it as easy as possible.

> Parts of this guidelines are taken from https://github.com/atom/atom/blob/master/CONTRIBUTING.md

#### Table of contents

- [Code of Conduct](#code-of-conduct)
- [Asking questions](#asking-questions)
- [Styleguides](#styleguides)
  - [Commit message](#commit-messages)
  - [Lint](#lint)
- [Additional info](#additional-info)

## Code of Conduct

Code of conduct is [available in the repository](CODE_OF_CONDUCT.md)

## Asking questions

For asking questions, please make sure to use [**Discussions**](https://github.com/esensar/neovim-java/discussions) instead of **Issues**.

## Styleguides

### Commit messages
 - Use the present tense ("Add feature" not "Added feature")
 - Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
 - Limit the first line to 72 characters or less
 - Reference issues and pull requests liberally after the first line

### Lint

This project uses [checkstyle](https://checkstyle.org/) for linting. It is run on every pull request, together with full test suite. It can be run locally like:

```
./mvnw checkstyle:check -B
```

## Additional info

Since this project implements [Neovim API](https://neovim.io/doc/user/api.html), make sure it properly supports the spec. `api-explorer` can be useful to check out and test functionality of the API before implementing changes.
