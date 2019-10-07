module neovim.notifications {
    exports com.ensarsarajcic.neovim.java.notifications.buffer;
    exports com.ensarsarajcic.neovim.java.notifications.ui;
    exports com.ensarsarajcic.neovim.java.notifications.ui.cmdline;
    exports com.ensarsarajcic.neovim.java.notifications.ui.global;
    exports com.ensarsarajcic.neovim.java.notifications.ui.grid;
    exports com.ensarsarajcic.neovim.java.notifications.ui.popupmenu;
    exports com.ensarsarajcic.neovim.java.notifications.ui.tabline;
    exports com.ensarsarajcic.neovim.java.notifications.ui.wildmenu;
    exports com.ensarsarajcic.neovim.java.notifications;

    requires neovimapi;
    requires corerpc;
    requires reactivecorerpc;
    requires slf4j.api;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
}