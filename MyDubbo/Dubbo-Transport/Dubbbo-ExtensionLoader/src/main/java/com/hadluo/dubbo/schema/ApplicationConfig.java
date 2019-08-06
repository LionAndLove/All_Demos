package com.hadluo.dubbo.schema;


public class ApplicationConfig extends AbstractConfig {
    @Tag("name")
    private String name;
    @Tag("owner")
    private String owner;
    @Tag("organization")
    private String organization;

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getOrganization() {
        return organization;
    }

}
