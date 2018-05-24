package com.ensarsarajcic.neovim.java.explorer.api;

public final class NeovimError {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NeovimError{" +
                "id=" + id +
                '}';
    }
}
