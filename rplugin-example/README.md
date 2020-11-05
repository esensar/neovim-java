# Remote plugin example

This module represents simple example of remote plugin. To run this example run `mvn package` onparent project and run Neovim with provided vimrc by calling `nvim -u rplugin-example/vimrc` from project root.

It should be possible to then call the `:NeovimJavaIncrementCount` command which communicated with java plugin.
