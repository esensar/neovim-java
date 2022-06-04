module com.ensarsarajcic.neovim.java.rpluginehostedx {
    requires com.ensarsarajcic.neovim.java.pluginhost;
    requires com.ensarsarajcic.neovim.java.handlerannotations;
    requires com.ensarsarajcic.neovim.java.neovimapi;
    requires com.ensarsarajcic.neovim.java.corerpc;
    exports com.ensarsarajcic.neovim.java.rpluginehostedx to com.ensarsarajcic.neovim.java.pluginhost;
}