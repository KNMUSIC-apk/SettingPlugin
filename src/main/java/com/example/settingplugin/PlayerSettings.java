package com.example.settingplugin;

public class PlayerSettings {
    private boolean pvpEnabled = true;
    private boolean voidProtection = false;
    private boolean loginEnabled = false;
    private boolean mobSpawnProtection = false;
    private boolean autoAcceptTpa = false;
    private boolean autoRespawn = false;

    private String passwordHash = null;
    private boolean loggedIn = true;
    private int loginFailCount = 0; // Số lần đăng nhập sai

    // Getter và Setter cho từng thuộc tính

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public boolean isVoidProtection() {
        return voidProtection;
    }

    public void setVoidProtection(boolean voidProtection) {
        this.voidProtection = voidProtection;
    }

    public boolean isLoginEnabled() {
        return loginEnabled;
    }

    public void setLoginEnabled(boolean loginEnabled) {
        this.loginEnabled = loginEnabled;
    }

    public boolean isMobSpawnProtection() {
        return mobSpawnProtection;
    }

    public void setMobSpawnProtection(boolean mobSpawnProtection) {
        this.mobSpawnProtection = mobSpawnProtection;
    }

    public boolean isAutoAcceptTpa() {
        return autoAcceptTpa;
    }

    public void setAutoAcceptTpa(boolean autoAcceptTpa) {
        this.autoAcceptTpa = autoAcceptTpa;
    }

    public boolean isAutoRespawn() {
        return autoRespawn;
    }

    public void setAutoRespawn(boolean autoRespawn) {
        this.autoRespawn = autoRespawn;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(int loginFailCount) {
        this.loginFailCount = loginFailCount;
    }
}
