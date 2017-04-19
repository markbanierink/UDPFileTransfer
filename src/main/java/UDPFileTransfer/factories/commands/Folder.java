package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.factories.flags.Flags.FOLDER_REQUEST;
import static UDPFileTransfer.helper.Modes.DEFAULT;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;

/**
 * Describes the Folder command
 * @author Mark Banierink
 */
public class Folder implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 2) {
            String location = splitLine[1].toLowerCase();
            if (location.equals("pi")) {
                transferHandler.sendCommand(FOLDER_REQUEST, "");
            }
            else if (location.equals("local")) {
                transferHandler.handleUserOutput("Files available:");
                transferHandler.handleUserOutput(transferHandler.getFileNamesString(main.getPath()));
            }
            else {
                transferHandler.handleUserOutput(DEFAULT, UNKNOWN_COMMAND);
            }
        }
        else {
           transferHandler.handleUserOutput(DEFAULT, UNKNOWN_COMMAND);
        }
    }
}
