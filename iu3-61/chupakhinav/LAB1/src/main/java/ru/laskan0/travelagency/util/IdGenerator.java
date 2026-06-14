package ru.laskan0.travelagency.util;

import java.util.Collection;

public final class IdGenerator {
    private IdGenerator() {
    }

    public static String nextId(Collection<String> existingIds, String prefix) {
        int maxValue = existingIds.stream()
                .filter(id -> id != null && id.startsWith(prefix))
                .map(id -> id.substring(prefix.length()))
                //.filter(suffix -> suffix.chars().allMatch(Character::isDigit))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return prefix + String.format("%03d", maxValue + 1);
    }
}
