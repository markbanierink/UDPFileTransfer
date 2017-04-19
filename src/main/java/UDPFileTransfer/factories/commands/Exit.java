package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

/**
 * Describes the Exit command
 * @author Mark Banierink
 */
public class Exit implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        main.exit();
    }
}
