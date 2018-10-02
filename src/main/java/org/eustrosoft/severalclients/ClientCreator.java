package org.eustrosoft.severalclients;

import java.io.IOException;
import java.net.InetAddress;

public class ClientCreator {
    static final int MAX_THREADS = 40;

    public static void main(String[] args) throws IOException,
            InterruptedException {
        InetAddress addr = InetAddress.getByName(null);
        while (true) {
            if (SingleClient.threadCount().intValue() < MAX_THREADS)
                new SingleClient(addr);
            else break;
            //Thread.sleep(100);
        }
    }
}
