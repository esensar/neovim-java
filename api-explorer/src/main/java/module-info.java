module apiexplorer {
    exports com.ensarsarajcic.neovim.java.explorer;
    exports com.ensarsarajcic.neovim.java.explorer.api;
    exports com.ensarsarajcic.neovim.java.explorer.list;
    exports com.ensarsarajcic.neovim.java.explorer.test;

    requires neovimapi;
    requires corerpc;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires jackson.dataformat.msgpack;
    requires com.fasterxml.jackson.databind;
    requires reactivecorerpc;
    requires unixsocketconnection;
    requires slf4j.api;
    requires javafx.fxml;
    requires javafx.controls;
}