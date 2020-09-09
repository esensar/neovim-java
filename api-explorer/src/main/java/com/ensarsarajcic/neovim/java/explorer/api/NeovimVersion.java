/*
 * MIT License
 *
 * Copyright (c) 2018 Ensar Sarajčić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ensarsarajcic.neovim.java.explorer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Neovim API Version info as provided by Neovim API Info
 * Used only by API Explorer
 */
public final class NeovimVersion {

    private final int apiCompatible;
    private final int apiLevel;
    private final boolean apiPreRelease;
    private final int major;
    private final int minor;
    private final int patch;

    public NeovimVersion(
            @JsonProperty("api_compatible")
                    int apiCompatible,
            @JsonProperty("api_level")
                    int apiLevel,
            @JsonProperty("api_prerelease")
                    boolean apiPreRelease,
            @JsonProperty("major")
                    int major,
            @JsonProperty("minor")
                    int minor,
            @JsonProperty("patch")
                    int patch) {
        this.apiCompatible = apiCompatible;
        this.apiLevel = apiLevel;
        this.apiPreRelease = apiPreRelease;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getApiCompatible() {
        return apiCompatible;
    }

    public int getApiLevel() {
        return apiLevel;
    }

    public boolean isApiPreRelease() {
        return apiPreRelease;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return "NeovimVersion{"
                + "apiCompatible=" + apiCompatible
                + ", apiLevel=" + apiLevel
                + ", apiPreRelease=" + apiPreRelease
                + ", major=" + major
                + ", minor=" + minor
                + ", patch=" + patch + '}';
    }
}
