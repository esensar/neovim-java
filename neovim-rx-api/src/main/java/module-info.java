module neovimrxapi {
    exports com.ensarsarajcic.neovim.java.rxapi;
    opens com.ensarsarajcic.neovim.java.rxapi;

    requires neovimapi;
    requires io.reactivex.rxjava2;
    requires corerpc;
}