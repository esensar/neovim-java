module com.ensarsarajcic.neovim.java.handlerannotations {
    exports com.ensarsarajcic.neovim.java.handler;
    exports com.ensarsarajcic.neovim.java.handler.annotations;

    // Export reflection utils to plugin host
    exports com.ensarsarajcic.neovim.java.handler.util to com.ensarsarajcic.neovim.java.pluginhost;
    exports com.ensarsarajcic.neovim.java.handler.errors;

    requires com.ensarsarajcic.neovim.java.corerpc;
    requires org.slf4j;
}