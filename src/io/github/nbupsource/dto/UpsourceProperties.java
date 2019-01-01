package io.github.nbupsource.dto;

import java.io.Serializable;

public class UpsourceProperties implements Serializable {

    private String projectName;

    private String upsourceUrl;

    private String userName;

    private String passwordHash;

    public UpsourceProperties(String projectName, String upsourceUrl, String username, String passwordHash) {
        this.projectName = projectName;
        this.upsourceUrl = upsourceUrl;
        this.userName = username;
        this.passwordHash = passwordHash;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getUpsourceUrl() {
        return upsourceUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

}
