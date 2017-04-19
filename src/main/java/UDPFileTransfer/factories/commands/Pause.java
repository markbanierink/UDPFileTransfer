package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.factories.flags.Flags.PAUSE;
import static UDPFileTransfer.helper.Modes.DEFAULT;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;

/**
 * Describes the Pause command
 * @author Mark Banierink
 */
public class Pause implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 2) {
            String fileName = splitLine[1].toLowerCase();
            if (main.isTransfer(fileName)) {
                transferHandler.sendCommand(PAUSE, fileName);
                transferHandler.handleUserOutput(DEFAULT, "Pausing transfer of file \"" + fileName + "\"");
            }
        }
        else {
            main.handleUserOutput(DEFAULT, UNKNOWN_COMMAND);
        }
    }
}

