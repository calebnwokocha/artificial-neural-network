/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

import java.util.Arrays;
import java.util.List;

public class Debugger {
    private static boolean debugger = false;
    private static boolean speak = false;
    public boolean printedLine = false;
    public boolean printedText = false;

    public boolean isDebugging() {
        return debugger;
    }

    public void startDebugger(boolean state) {
        debugger = state;
    }

    public boolean canSpeak() {
        return speak;
    }

    public void speak(boolean speak) {
        Debugger.speak = speak;
    }

    private void speak(String text) {
        if (speak) {
            // Create TextToSpeech
            TTS tts = new TTS();

            // Print all the available voices
            // Voice: dfki-poppy-hsmm
            // Voice: cmu-slt-hsmm
            // Voice: cmu-rms-hsmm
            // tts.getAvailableVoices().stream().forEach(voice -> System.out.println("Voice: " + voice));

            // Setting the Current Voice
            tts.setVoice("cmu-rms-hsmm");

            List<String> arrayList = Arrays.asList(text);

            arrayList.forEach(word -> tts.speak(word, 2.0f, false, true));
        }
    }

    public void printText(String text) {
        if (debugger) {
            System.out.print(text);
            speak(text);
        }
    }

    public void printLine(String text) {
        if (debugger) {
            System.out.println(text);
            speak(text);
        }
    }
}
