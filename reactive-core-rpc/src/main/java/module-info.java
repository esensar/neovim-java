module reactivecorerpc {
    opens com.ensarsarajcic.neovim.java.corerpc.reactive;
    exports com.ensarsarajcic.neovim.java.corerpc.reactive;

    requires corerpc;
    requires org.slf4j;
}