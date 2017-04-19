package UDPFileTransfer.factories.commands;

/**
 * Factory for handling main commands
 * @author Mark Banierink
 */
public class CommandFactory {

    public static Command handleCommand(String userInput) {
        String[] splitString = userInput.split(" ");
        for (Commands command : Commands.values()) {
            if (splitString[0].toLowerCase().equals(command.toString().toLowerCase())) {
                return command.getInstance();
            }
        }
        return null;
    }

}
