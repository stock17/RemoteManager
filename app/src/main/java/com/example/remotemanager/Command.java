package com.example.remotemanager;

public class Command {
    private Command.Type type;
    private double value;

    public Command(Type type, double value) {
        this.type = type;
        this.value = value;
    }

    enum Type {
        VOLUME_LEVEL
    }
}
