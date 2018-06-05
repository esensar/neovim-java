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

package com.ensarsarajcic.neovim.java.api.types.apiinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class VersionInfo {
    private int major;
    private int minor;
    private int patch;
    private int level;
    private int compatible;
    private boolean preRelease;

    public VersionInfo(
            @JsonProperty("major")
            int major,
            @JsonProperty("minor")
            int minor,
            @JsonProperty("patch")
            int patch,
            @JsonProperty("api_level")
            int level,
            @JsonProperty("api_compatible")
            int compatible,
            @JsonProperty("api_prerelease")
            boolean preRelease) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.level = level;
        this.compatible = compatible;
        this.preRelease = preRelease;
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

    public int getLevel() {
        return level;
    }

    public int getCompatible() {
        return compatible;
    }

    public boolean isPreRelease() {
        return preRelease;
    }

    public String getVersionString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                ", level=" + level +
                ", compatible=" + compatible +
                ", preRelease=" + preRelease +
                '}';
    }
}
