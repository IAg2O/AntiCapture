package dev.ag2o.anticapture.os;

public enum OS {
    Windows("Windows"),
    MacOS("Mac OS"),
    IDK("Null"),
    ;

    private final String name;

    OS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
