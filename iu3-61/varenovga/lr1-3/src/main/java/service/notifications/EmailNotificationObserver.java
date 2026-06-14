package main.java.service.notifications;
import main.java.service.dto.OverdueInfo;
import java.time.LocalDate;
import java.util.List;

public class EmailNotificationObserver implements NotificationObserver {
    private final String email;
    private final String userName;
    private final List<OverdueInfo> overdueList;
    private final LocalDate currentDate;
    private static int notificationCounter = 0;

    public EmailNotificationObserver(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.overdueList = null;
        this.currentDate = null;
    }

    public EmailNotificationObserver(String userName, String email,
                                     List<OverdueInfo> overdueList, LocalDate currentDate) {
        this.userName = userName;
        this.email = email;
        this.overdueList = overdueList;
        this.currentDate = currentDate;
    }

    @Override
    public void update(String message, NotificationType type) {
        notificationCounter++;
        System.out.println("📧 EMAIL на " + email + ": ");
        System.out.println("   Кому: " + userName);
        System.out.println("   Тип: " + type.getDisplayName());
        System.out.println("   Сообщение: " + message);
        System.out.println("   -------------------");
    }

    public void sendOverdueNotification() {
        if (overdueList == null || overdueList.isEmpty()) {
            System.out.println("⚠️  Нет просроченных книг для уведомления");
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("У вас есть просроченные книги:\n\n");

        for (OverdueInfo item : overdueList) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(item.dueDate(), currentDate);
            message.append(String.format("  📚 '%s'\n", item.publicationTitle()));
            message.append(String.format("      Дата возврата: %s\n", item.dueDate()));
            message.append(String.format("      Просрочка: %d дн.\n\n", days));
        }

        message.append("Пожалуйста, верните книги в ближайшее время!");
        update(message.toString(), NotificationType.OVERDUE);
    }

    @Override
    public String getObserverName() {
        return userName + " (" + email + ")";
    }

    public static int getNotificationCounter() {
        return notificationCounter;
    }
}