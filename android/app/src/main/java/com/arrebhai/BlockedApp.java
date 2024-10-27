package com.arrebhai;

public class BlockedApp {
    private String bundleId = "";
    private String icon = "";
    private int id ;
    private String name = "";
    private String url = "";
    // Getters and Setters
    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String Url) {
        this.url = Url;
    }

    @Override
    public String toString() {
        return "BlockedApp{" +
                "bundleId='" + bundleId + '\'' +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

