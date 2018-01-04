package com.gamecraft.web.rest.util;

import java.io.BufferedWriter;

public class IrcBotListener {
    public static void sendString(BufferedWriter bw, String str) {
        try {
            bw.write(str + "\r\n");
            bw.flush();
        }
        catch (Exception e) {
            System.out.println("Exception: "+e);
        }
    }
}
