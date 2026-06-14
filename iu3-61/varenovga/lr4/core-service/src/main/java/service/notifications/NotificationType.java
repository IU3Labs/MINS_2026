package service.notifications;

public enum NotificationType {
    OVERDUE("Просрочка"),
    AVAILABLE("Доступно"),
    REMINDER("Напоминание"),
    RETURNED("Возврат");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}