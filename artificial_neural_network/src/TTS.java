import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author GOXR3PLUS
 *
 */
public class TTS {
	
	private Audio audio;
	private MaryInterface maryInterface;
	
	/**
	 * Constructor
	 */
	public TTS() {
		try {
			maryInterface = new LocalMaryInterface();
			
		} catch (MaryConfigurationException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//----------------------GENERAL METHODS---------------------------------------------------//
	
	/**
	 * Transform text to speech
	 * 
	 * @param text
	 *            The text that will be transformed to speech
	 * @param daemon
	 *            <br>
	 *            <b>True</b> The thread that will start the text to speech Player will be a daemon Thread <br>
	 *            <b>False</b> The thread that will start the text to speech Player will be a normal non daemon Thread
	 * @param join
	 *            <br>
	 *            <b>True</b> The current Thread calling this method will wait(blocked) until the Thread which is playing the Speech finish <br>
	 *            <b>False</b> The current Thread calling this method will continue freely after calling this method
	 */
	public void speak(String text , float gainValue , boolean daemon , boolean join) {
		
		// Stop the previous player
		stopSpeaking();
		
		try (AudioInputStream audio = maryInterface.generateAudio(text)) {
			
			// Player is a thread(threads can only run one time) so it can be
			// used has to be initiated every time
			this.audio = new Audio();
			this.audio.setAudio(audio);
			this.audio.setGain(gainValue);
			this.audio.setDaemon(daemon);
			this.audio.start();
			if (join)
				this.audio.join();
			
		} catch (SynthesisException ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "IO Exception", ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Interrupted ", ex);
			audio.interrupt();
		}
	}
	
	/**
	 * Stop the MaryTTS from Speaking
	 */
	public void stopSpeaking() {
		// Stop the previous player
		if (audio != null)
			audio.cancel();
	}
	
	//----------------------GETTERS---------------------------------------------------//
	
	/**
	 * Available voices in String representation
	 * 
	 * @return The available voices for MaryTTS
	 */
	public Collection<Voice> getAvailableVoices() {
		return Voice.getAvailableVoices();
	}
	
	/**
	 * @return the marytts
	 */
	public MaryInterface getMaryInterface() {
		return maryInterface;
	}
	
	/**
	 * Return a list of available audio effects for MaryTTS
	 * 
	 * @return
	 */
	public List<AudioEffect> getAudioEffects() {
		return StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList());
	}
	
	//----------------------SETTERS---------------------------------------------------//
	
	/**
	 * Change the default voice of the MaryTTS
	 * 
	 * @param voice
	 */
	public void setVoice(String voice) {
		maryInterface.setVoice(voice);
	}
	
}
