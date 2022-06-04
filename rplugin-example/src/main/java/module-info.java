module com.ensarsarajcic.neovim.java.rpluginex {
    requires com.ensarsarajcic.neovim.java.pluginhost;
    requires com.ensarsarajcic.neovim.java.handlerannotations;
    requires com.ensarsarajcic.neovim.java.neovimapi;
    requires com.ensarsarajcic.neovim.java.corerpc;
    exports com.ensarsarajcic.neovim.java.rpluginex to com.ensarsarajcic.neovim.java.pluginhost;
}