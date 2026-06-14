package service;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 Генератор ID с возможностью переиспользования освобождённых ID.
 */
public class IdGenerator {
    private final Deque<Integer> availableIds;
    private int nextId;

    public IdGenerator(int startFrom) {
        this.availableIds = new ArrayDeque<>();
        this.nextId = startFrom;
    }

    public int generate() {
        if (!availableIds.isEmpty()) {
            return availableIds.pop();
        }
        return nextId++;
    }

    public void release(int id) {
        if (id < nextId) {
            availableIds.push(id);
        }
    }

    public int getAvailableCount() {
        return availableIds.size();
    }
}