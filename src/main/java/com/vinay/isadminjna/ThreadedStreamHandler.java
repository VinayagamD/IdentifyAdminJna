package com.vinay.isadminjna;

import java.io.*;

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
class ThreadedStreamHandler extends Thread {
    InputStream inputStream;
    StringBuilder outputBuffer = new StringBuilder();
    StringBuilder commandInput;

    /**
     * This constructor will just run the command you provide
     *
     * @param inputStream an InputStream for working with STDIN / STDOUT
     */
    ThreadedStreamHandler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setCommandInput(StringBuilder commandForInput) {
        this.commandInput = commandForInput;
    }

    public void run() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                outputBuffer.append(line).append("\n");
            }
        } catch (IOException ioe) {
            // TODO handle this better
            ioe.printStackTrace();
        } catch (Throwable t) {
            // TODO handle this better
            t.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                // ignore this one
            }
        }
    }

    public StringBuilder getOutputBuffer() {
        return outputBuffer;
    }
}