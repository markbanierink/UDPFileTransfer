package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.factories.flags.Flags.ABORT;
import static UDPFileTransfer.helper.Modes.USE;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;

/**
 * Describes the Abort command
 * @author Mark Banierink
 */
public class Abort implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 2) {
            String fileName = splitLine[1].toLowerCase();
            if (main.isTransfer(fileName)) {
                transferHandler.sendCommand(ABORT, fileName);
            }
        }
        else {
            main.handleUserOutput(USE, UNKNOWN_COMMAND);
        }
    }
}
