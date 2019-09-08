package com.example.remotemanager;

public class Command {
    private Command.Type type;
    private int value;

    public Command(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    enum Type {
        VOLUME_LEVEL
    }
}
