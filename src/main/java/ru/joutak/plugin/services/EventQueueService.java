package ru.joutak.plugin.services;

import ru.joutak.plugin.model.KillEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueueService {
    private final Queue<KillEvent> eventQueue = new ConcurrentLinkedQueue<>();

    public void add(KillEvent event) {
        eventQueue.add(event);
    }

    public Queue<KillEvent> drain() {
        Queue<KillEvent> drained = new ConcurrentLinkedQueue<>(eventQueue);
        eventQueue.clear();
        return drained;
    }

    public int size() {
        return eventQueue.size();
    }
}
