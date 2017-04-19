package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.factories.flags.Flags.*;
import static UDPFileTransfer.helper.Modes.DEFAULT;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;

/**
 * Describes the Connect command
 * @author Mark Banierink
 */
public class Connect implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        if (line.split(" ").length == 1) {
            transferHandler.sendCommand(CONNECT, "");
            transferHandler.handleUserOutput(DEFAULT, "Connecting to server");
        }
        else {
            main.handleUserOutput(DEFAULT, UNKNOWN_COMMAND);
        }

    }

}
