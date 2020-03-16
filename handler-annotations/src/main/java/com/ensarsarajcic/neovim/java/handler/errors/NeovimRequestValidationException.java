package com.ensarsarajcic.neovim.java.handler.errors;

import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;

public class NeovimRequestValidationException extends NeovimHandlerException {
    public NeovimRequestValidationException(String message) {
        super(message);
    }

    @Override
    public RpcError toRpcError() {
        return RpcError.validation(getMessage());
    }
}
