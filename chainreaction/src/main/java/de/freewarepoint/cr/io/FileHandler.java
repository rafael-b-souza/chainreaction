// src/main/java/de/freewarepoint/cr/io/FileHandler.java
package de.freewarepoint.cr.io;

import java.io.*;
import java.nio.file.Path;

public final class FileHandler {
    public static void save(Path p, GameState s) throws IOException {
        try (ObjectOutputStream o = new ObjectOutputStream(
                new BufferedOutputStream(java.nio.file.Files.newOutputStream(p)))) {
            o.writeObject(s);
        }
    }
    public static GameState load(Path p) throws IOException, ClassNotFoundException {
        try (ObjectInputStream i = new ObjectInputStream(
                new BufferedInputStream(java.nio.file.Files.newInputStream(p)))) {
            return (GameState) i.readObject();
        }
    }
    private FileHandler() {}
}
