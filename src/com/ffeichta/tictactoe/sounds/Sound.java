package com.ffeichta.tictactoe.sounds;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * This class contains a method called "playSound()" which is used tho play a
 * winning sound after a player wins
 * 
 * @author Fabian
 * 
 */
public class Sound {

	public void playSound() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(this.getClass()
					.getResource("/com/ffeichta/tictactoe/sounds/music.wav")));
			clip.start();
			// Wait until the sound is finished, without this you hear nothing
			Thread.sleep(clip.getMicrosecondLength() / 1000);
		} catch (Exception e) {
			;
		}
	}
}
