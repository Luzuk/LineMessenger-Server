package com.buchach.lyceum.messenger.util;

import com.buchach.lyceum.messenger.server.SocketProcessor;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private static volatile Util instance;
    private static Object mutex = new Object();

    private List<SocketProcessor> socketProcessors;

    private Util() {
        socketProcessors = new ArrayList<>();
    }

    public List<SocketProcessor> getAllConnections() {
        return socketProcessors;
    }

    public void addConnection(SocketProcessor socketProcessor) {
        socketProcessors.add(socketProcessor);
    }

    public static Util getInstance() {
        Util result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new Util();
                }
            }
        }
        return result;
    }
}
