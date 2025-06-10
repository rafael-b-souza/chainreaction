package de.freewarepoint.cr;

public class AnimSettings {
    public enum Mode { CLASSIC, SMOOTH }

    private Mode    mode          = Mode.SMOOTH;
    private int     explodeFrames = 12;
    private boolean shrinkEnd     = true;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public int getExplodeFrames() {
        return explodeFrames;
    }

    public void setExplodeFrames(int explodeFrames) {
        this.explodeFrames = explodeFrames;
    }

    public boolean isShrinkEnd() {
        return shrinkEnd;
    }

    public void setShrinkEnd(boolean shrinkEnd) {
        this.shrinkEnd = shrinkEnd;
    }
}
