package com.ensarsarajcic.neovim.java.handler.errors;

import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;

public class NeovimRequestException extends NeovimHandlerException {
    public NeovimRequestException(String message) {
        super(message);
    }

    @Override
    public RpcError toRpcError() {
        return RpcError.exception(getMessage());
    }
}
