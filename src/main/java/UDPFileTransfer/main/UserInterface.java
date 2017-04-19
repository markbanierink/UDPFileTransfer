package UDPFileTransfer.main;

import UDPFileTransfer.helper.Modes;
import UDPFileTransfer.helper.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static UDPFileTransfer.helper.Modes.*;
import static java.lang.Thread.interrupted;


/**
 * Describes the Main Interface and its input and output
 * @author Mark Banierink
 */
public class UserInterface implements Runnable {

    private Main main;
    private Modes mode = DEBUG;
    private boolean running = true;
    private boolean windows = false;

    public UserInterface(Main main, boolean windows) {
        this.main = main;
        this.windows = windows;
    }

    @Override
    public void run() {
        if (windows) {
            mode = USE;
            String line;
            while ((line = readConsoleInput()) != null && !interrupted() && running) {
                handleUserInput(line);
            }
        }
    }

    private String readConsoleInput() {
        String line = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            line = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null) {
            return "";
        }
        else {
            return line;
        }
    }

    private void handleUserInput(String line) {
        main.handleUserInput(line);
    }

    public void handleUserOutput(String string) {
        System.out.println(string);
    }

    public void handleUserOutput(Modes selectedMode, Resources resource) {
        handleUserOutput(selectedMode, resource.toString());
    }

    public void handleUserOutput(Modes selectedMode, String string) {
        if (mode.equals(DEBUG)) {
            System.out.println(string);
        }
        else if (mode.equals(selectedMode)) {
            System.out.println(string);
        }
    }

    public void shutdown() {
        running = false;
    }

}
