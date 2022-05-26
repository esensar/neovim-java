module com.ensarsarajcic.neovim.java.apiexplorer {
    exports com.ensarsarajcic.neovim.java.explorer;
    exports com.ensarsarajcic.neovim.java.explorer.api;
    exports com.ensarsarajcic.neovim.java.explorer.list;
    exports com.ensarsarajcic.neovim.java.explorer.test;

    requires com.ensarsarajcic.neovim.java.neovimapi;
    requires com.ensarsarajcic.neovim.java.corerpc;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires jackson.dataformat.msgpack;
    requires com.fasterxml.jackson.databind;
    requires com.ensarsarajcic.neovim.java.reactivecorerpc;
    requires com.ensarsarajcic.neovim.java.unixsocketconnection;
    requires org.slf4j;
    requires javafx.fxml;
    requires javafx.controls;
}