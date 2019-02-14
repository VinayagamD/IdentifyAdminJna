package com.vinay.isadminjna;

import java.io.*;
import java.util.List;

/**
 * This code is Copyright 2010 Alvin J. Alexander, http://devdaily.com.
 * You are free to adapt and share this work under the terms of the
 * Creative Commons Attribution-ShareAlike 3.0 Unported License;
 * see http://creativecommons.org/licenses/by-sa/3.0/ for more
 * details.
 *
 * Thanks to Alvin for this great post: http://alvinalexander.com/java/java-exec-processbuilder-process-1
 *
 * Changes made by Michael Hernandez:
 *  - change package to my own
 *  - remove sudo-related STDIN code and comments
 *  - change formatting
 *  - see commit history of repo for details
 */
public class SystemCommandExecutor {
    public static final int EXIT_CODE_DEFAULT = -99;

    private List<String> commandInformation;
    private ThreadedStreamHandler inputStreamHandler;
    private ThreadedStreamHandler errorStreamHandler;

    /**
     * Give the consumer access to these objects as well.
     */
    private ProcessBuilder processBuilder;
    private Process process;

    /**
     * Pass in the system command you want to run as a List of Strings, as shown here:
     *
     * List<String> commands = new ArrayList<String>();
     * commands.add("/sbin/ping");
     * commands.add("-c");
     * commands.add("5");
     * commands.add("www.google.com");
     * SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
     * commandExecutor.executeCommand();
     *
     * @param commandInformation The command you want to run.
     */
    public SystemCommandExecutor(final List<String> commandInformation) {
        if (commandInformation.isEmpty()) {
            throw new IllegalArgumentException("The commandInformation is required.");
        }
        this.commandInformation = commandInformation;
    }

    public int executeCommand() throws IOException, InterruptedException {
        processBuilder = new ProcessBuilder(commandInformation);

        process = processBuilder.start();

        // These need to run as java threads to get the standard output and error from the command.
        // The inputStreamHandler gets a reference to our stdOutput in case we need to write something to it.
        inputStreamHandler = new ThreadedStreamHandler(process.getInputStream());
        errorStreamHandler = new ThreadedStreamHandler(process.getErrorStream());

        return handle();}

    private int handle() throws InterruptedException {
        inputStreamHandler.start();
        errorStreamHandler.start();

        int exitValue = process.waitFor();

        inputStreamHandler.interrupt();
        errorStreamHandler.interrupt();

        inputStreamHandler.join();
        errorStreamHandler.join();
        return exitValue;
    }

    public int executeCommand(StringBuilder inputStringBuilder) throws IOException, InterruptedException {
        processBuilder = new ProcessBuilder(commandInformation);

        // Create a temp file to hold the entry. It's much easier than dealing with pipes
        File temp = File.createTempFile("dayonePre", "dayoneSuf");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(inputStringBuilder.toString());
        bw.close();
        processBuilder.redirectInput(temp);

        process = processBuilder.start();

        // These need to run as java threads to get the standard output and error from the command.
        inputStreamHandler = new ThreadedStreamHandler(process.getInputStream());
        errorStreamHandler = new ThreadedStreamHandler(process.getErrorStream());

        inputStreamHandler.setCommandInput(inputStringBuilder);

        return handle();
    }

    /**
     * Get the standard output (stdout) from the command you just exec'd.
     */
    public StringBuilder getStandardOutputFromCommand() {
        return inputStreamHandler.getOutputBuffer();
    }

    /**
     * Get the standard error (STDERR) from the command you just exec'd.
     */
    public StringBuilder getStandardErrorFromCommand() {
        return errorStreamHandler.getOutputBuffer();
    }
}
