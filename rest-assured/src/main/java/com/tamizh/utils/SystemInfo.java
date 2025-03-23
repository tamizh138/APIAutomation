package com.tamizh.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemInfo {
    public static String getHostName() throws UnknownHostException {
        String hostname = "unknown";
        try {

            hostname = InetAddress.getLocalHost().getHostName();
            System.out.println("Hostname: " + hostname);
        } catch (UnknownHostException e) {
            System.out.println("Unable to determine hostname");
        }
        return  hostname;
    }

    public static String getOs() {
        return System.getProperty("os.name");
    }
}
