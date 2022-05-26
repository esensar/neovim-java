module neovim.notifications {
    exports com.ensarsarajcic.neovim.java.notifications.global;
    exports com.ensarsarajcic.neovim.java.notifications.buffer;
    exports com.ensarsarajcic.neovim.java.notifications.ui;
    exports com.ensarsarajcic.neovim.java.notifications.ui.cmdline;
    exports com.ensarsarajcic.neovim.java.notifications.ui.global;
    exports com.ensarsarajcic.neovim.java.notifications.ui.grid;
    exports com.ensarsarajcic.neovim.java.notifications.ui.popupmenu;
    exports com.ensarsarajcic.neovim.java.notifications.ui.tabline;
    exports com.ensarsarajcic.neovim.java.notifications.ui.wildmenu;
    exports com.ensarsarajcic.neovim.java.notifications;

    requires com.ensarsarajcic.neovim.java.neovimapi;
    requires com.ensarsarajcic.neovim.java.corerpc;
    requires com.ensarsarajcic.neovim.java.reactivecorerpc;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires org.reflections;
}