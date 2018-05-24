package com.ensarsarajcic.neovim.java.explorer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class NeovimVersion {

    @JsonProperty("api_compatible")
    private int apiCompatible;
    @JsonProperty("api_level")
    private int apiLevel;
    @JsonProperty("api_prerelease")
    private boolean apiPreRelease;
    private int major;
    private int minor;
    private int patch;

    public int getApiCompatible() {
        return apiCompatible;
    }

    public void setApiCompatible(int apiCompatible) {
        this.apiCompatible = apiCompatible;
    }

    public int getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(int apiLevel) {
        this.apiLevel = apiLevel;
    }

    public boolean isApiPreRelease() {
        return apiPreRelease;
    }

    public void setApiPreRelease(boolean apiPreRelease) {
        this.apiPreRelease = apiPreRelease;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getPatch() {
        return patch;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    @Override
    public String toString() {
        return "NeovimVersion{" +
                "apiCompatible=" + apiCompatible +
                ", apiLevel=" + apiLevel +
                ", apiPreRelease=" + apiPreRelease +
                ", major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                '}';
    }
}
