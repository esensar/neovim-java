package com.ensarsarajcic.neovim.java.explorer.api;

public final class NeovimType {

    private int id;
    private String prefix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "NeovimType{" +
                "id=" + id +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
