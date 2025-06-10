package de.freewarepoint.cr.io;

import de.freewarepoint.cr.AnimSettings;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public final class AnimPropertiesLoader {

    private static final String MODE   = "anim.mode";
    private static final String FRAMES = "anim.explodeFrames";
    private static final String SHRINK = "anim.shrinkEnd";

    public static AnimSettings load(Path p) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(p)) { props.load(in); }

        AnimSettings a = new AnimSettings();
        if (props.containsKey(MODE))
            a.setMode(AnimSettings.Mode.valueOf(props.getProperty(MODE)));
        if (props.containsKey(FRAMES))
            a.setExplodeFrames(Integer.parseInt(props.getProperty(FRAMES)));
        if (props.containsKey(SHRINK))
            a.setShrinkEnd(Boolean.parseBoolean(props.getProperty(SHRINK)));
        return a;
    }
    private AnimPropertiesLoader() {}
}
