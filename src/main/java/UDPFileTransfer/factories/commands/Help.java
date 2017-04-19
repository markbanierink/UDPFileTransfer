package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.helper.Modes.USE;
import static UDPFileTransfer.helper.Resources.COMMANDS;
import static UDPFileTransfer.helper.Resources.DOUBLE_BAR;
import static UDPFileTransfer.helper.Resources.SINGLE_BAR;

/**
 * Describes the Help command
 * @author Mark Banierink
 */
public class Help implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        transferHandler.handleUserOutput(USE, DOUBLE_BAR);
        transferHandler.handleUserOutput(USE, COMMANDS);
        transferHandler.handleUserOutput(USE, SINGLE_BAR);
    }
}
