package org.eustrosoft.severalclients;


import org.eustrosoft.multiserver.MultiServer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;


public class SingleClient extends Thread {

    private static volatile AtomicInteger counter = new AtomicInteger(0);
    private static volatile AtomicInteger threadcount = new AtomicInteger(0);
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int id = counter.getAndIncrement();

    public SingleClient(InetAddress addr) {
        System.out.println("Making client " + id);
        threadcount.getAndIncrement();
        System.out.println("########### "+threadcount+" ############");
        try {
            socket = new Socket(addr, MultiServer.PORT);
        } catch (IOException e) {
            System.err.println("Socket failed");
            // Если создание сокета провалилось,
            // ничего ненужно чистить.
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            // Включаем автоматическое выталкивание:
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
            start();
        } catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            try {
                socket.close();
            } catch (IOException e2) {
                System.err.println("Socket not closed");
            }
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }

    public static AtomicInteger threadCount() {
        return threadcount;
    }

    public void run() {
        try {
            for (int i = 0; i < 25; i++) {
                out.println("Client " + id + ": " + i);
                String str = in.readLine();
                System.out.println(str);
            }
            out.println("END");
        } catch (IOException e) {
            System.err.println("IO Exception");
        } finally {
            // Всегда закрывает:
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
            threadcount.getAndDecrement(); // Завершаем эту нить
        }
    }
}
