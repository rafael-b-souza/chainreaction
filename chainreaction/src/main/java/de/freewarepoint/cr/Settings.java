package de.freewarepoint.cr;

public class Settings {

	private final int reactionDelay;
	private AnimSettings anim = new AnimSettings();

	public AnimSettings getAnim() {
		return anim;
	}

	public void setAnim(AnimSettings anim) {
		this.anim = anim;
	}

	public Settings() {
		this.reactionDelay = 100;
	}

	public Settings(int reactionDelay) {
		this.reactionDelay = reactionDelay;
	}

	public int getReactionDelay() {
		return reactionDelay;
	}
}
