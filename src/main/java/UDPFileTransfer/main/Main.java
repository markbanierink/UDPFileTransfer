package UDPFileTransfer.main;

import UDPFileTransfer.factories.commands.CommandFactory;
import UDPFileTransfer.factories.commands.Command;
import UDPFileTransfer.helper.Modes;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.helper.Resources;
import java.util.ArrayList;
import java.util.List;

import static UDPFileTransfer.factories.flags.Flags.CONNECT;
import static UDPFileTransfer.helper.Modes.*;
import static UDPFileTransfer.helper.Resources.*;

/**
 * Main class
 * @author Mark Banierink
 */
public class Main {

    private UserInterface userInterface;
    private Thread userInterfaceThread;
    private TransferHandler mainTransferHandler;
    private List<TransferHandler> transferHandlers = new ArrayList<>();
    private int port;
    private String path;
    private boolean windows = false;

    private static final String BROADCAST_ADDRESS = "192.168.40.2";     // TODO - temporary, should be 255, but doesn't work with cable connected
    public static final int DEFAULT_PORT = 8900;

    private Main() {
        checkOS();
        port = DEFAULT_PORT;
        initUserInterface();
        initTransferHandler();
        initConnection();
    }

    private void checkOS() {
        if (System.getProperty("os.name").equals("Windows 10")) {
            setPath("C:\\Workspace\\UDPFileTransfer\\files\\");
            windows = true;
        }
        else {
            setPath("/home/pi/files/");
        }
    }

    private void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static void main(String[] args) {
        new Main();
    }

    private void initUserInterface() {
        userInterface = new UserInterface(this, windows);
        userInterfaceThread = new Thread(userInterface, "UserInterface");
        userInterfaceThread.start();
        startupMessage();
    }

    private void initConnection() {
        if (windows) {
            mainTransferHandler.sendCommand(CONNECT, "");
        }
    }

    public String getStatusViews() {
        String string = "STATUS\n";
        string += DOUBLE_BAR;
        for (TransferHandler transferHandler : transferHandlers) {
            string += transferHandler.getSenderStatistics();
            string += SINGLE_BAR;
        }
        return string;
    }

    private void initTransferHandler() {
        mainTransferHandler = new TransferHandler(this, DEFAULT_PORT, BROADCAST_ADDRESS);
    }

    public TransferHandler createTransferHandler(String fileName, int port) {
        TransferHandler newTransferHandler = new TransferHandler(this, port, mainTransferHandler.getInetAddress());
        newTransferHandler.setTransferFile(fileName);
        transferHandlers.add(newTransferHandler);
        return newTransferHandler;
    }

    private void startupMessage() {
        handleUserOutput(USE, PROGRAM_TITLE);
        handleUserOutput(USE, DOUBLE_BAR);
        handleUserOutput(USE, COMMANDS);
        handleUserOutput(USE, SINGLE_BAR);
    }

    public void handleUserInput(String line) {
        Command userCommand = CommandFactory.handleCommand(line);
        if (userCommand == null) {
            handleUserOutput(USE, UNKNOWN_COMMAND);
        }
        else {
            userCommand.execute(this, getMainTransferHandler(), line);
        }
    }

    public int increaseAndGetPort() {
        port = port + 2;
        return port;
    }

    public long pendingSpace() {
        long pendingSpace = 0;
        for (TransferHandler transferHandler : transferHandlers) {
            pendingSpace += transferHandler.getTransferFile().getSize();
        }
        return pendingSpace;
    }

    public boolean isTransfer(String fileName) {
        return getTransferHandler(fileName) != null;
    }

    private TransferHandler getTransferHandler(String fileName) {
        for (TransferHandler transferHandler : transferHandlers) {
            if (transferHandler.getFileName().equals(fileName)) {
                return transferHandler;
            }
        }
        return null;
    }

    public void abortTransfer(String fileName) {
        TransferHandler transferHandler = getTransferHandler(fileName);
        if (transferHandler != null) {
            transferHandler.shutdown();
            transferHandlers.remove(transferHandler);
        }
    }

    public void handleUserOutput(String[] strings) {
        StringBuilder lines = new StringBuilder();
        for (String string : strings) {
            lines.append("- ").append(string).append("\n");
        }
        handleUserOutput(lines.toString());
    }

    public TransferHandler getMainTransferHandler() {
        return mainTransferHandler;
    }

    public void handleUserOutput(Modes mode, Resources resource) {
        userInterface.handleUserOutput(mode, resource);
    }

    public void handleUserOutput(Modes mode, String string) {
        userInterface.handleUserOutput(mode, string);
    }

    public void handleUserOutput(String string) {
        userInterface.handleUserOutput(string);
    }

    public void closeTransferHandler(TransferHandler transferHandler) {
        transferHandlers.remove(transferHandler);
        transferHandler.shutdown();
    }

    public void exit() {
        handleUserOutput(USE, CLOSE_APPLICATION);
        shutdown();
    }

    public void shutdown() {
        for (TransferHandler transferHandler : transferHandlers) {
            transferHandler.shutdown();
        }
        userInterfaceShutdown();
    }

    public void userInterfaceShutdown() {
        userInterfaceThread.interrupt();
        userInterface.shutdown();
    }
}







