package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.factories.flags.Flags.FOLDER_REQUEST;
import static UDPFileTransfer.factories.flags.Flags.REMOVE;
import static UDPFileTransfer.helper.Modes.USE;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;

/**
 * Describes the Remove command
 * @author Mark Banierink
 */
public class Remove implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 2) {
            String fileName = splitLine[1].toLowerCase();
            transferHandler.sendCommand(REMOVE, fileName);
        }
        else {
            main.handleUserOutput(USE, UNKNOWN_COMMAND);
        }
    }
}