module apiexplorer {
    exports com.ensarsarajcic.neovim.java.explorer;
    exports com.ensarsarajcic.neovim.java.explorer.api;
    exports com.ensarsarajcic.neovim.java.explorer.list;
    exports com.ensarsarajcic.neovim.java.explorer.test;
    requires neovimapi;
    requires corerpc;
    requires com.fasterxml.jackson.core;
    requires jackson.dataformat.msgpack;
    requires com.fasterxml.jackson.databind;
    requires reactivecorerpc;
    requires jackson.annotations;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires unixsocketconnection;
    requires slf4j.api;
}