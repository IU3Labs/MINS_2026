package badmodule;

import java.math.BigDecimal;

public class BadParkingCalculator {
    private int calculations;
    private BigDecimal collectedTotal = BigDecimal.ZERO;
    private BigDecimal collectedPenalties = BigDecimal.ZERO;
    private String lastVehicleType = "";
    private String lastCategory = "";
    private boolean lastWeekend;
    private boolean lastHoliday;
    private int lastEntryHour;
    private int lastHours;
    private StringBuilder auditLog = new StringBuilder();

    public BadParkingSummary calculate(String vehicleType,
                                       int hours,
                                       int entryHour,
                                       boolean weekend,
                                       boolean holiday,
                                       boolean vipClient,
                                       boolean lostTicket,
                                       boolean blockedExit,
                                       boolean crossedLines,
                                       boolean usedCharger,
                                       boolean usedPremiumZone) {
        // если тип машины не передали, принудительно считаем, что это обычный автомобиль.
        if (vehicleType == null || vehicleType.trim().isEmpty()) {
            vehicleType = "CAR";
        }
        // если часы указаны некорректно, подменяем их минимальным допустимым значением.
        if (hours <= 0) {
            hours = 1;
        }
        // если час въезда ушел ниже границы суток, прижимаем его к 0.
        if (entryHour < 0) {
            entryHour = 0;
        }
        // если час въезда больше 23, прижимаем его к последнему часу суток.
        if (entryHour > 23) {
            entryHour = 23;
        }

        calculations++;
        lastVehicleType = vehicleType.trim().toUpperCase();
        lastWeekend = weekend;
        lastHoliday = holiday;
        lastEntryHour = entryHour;
        lastHours = hours;

        BigDecimal base = BigDecimal.ZERO;
        BigDecimal special = BigDecimal.ZERO;
        BigDecimal penalty = BigDecimal.ZERO;
        String category = "STANDARD";
        String notes = "";

        // если это грузовик, применяем повышенный базовый тариф и категорию heavy.
        if ("TRUCK".equals(lastVehicleType)) {
            base = base.add(BigDecimal.valueOf(hours * 180));
            notes += "truck-rate;";
            category = "HEAVY";
        // если это мотоцикл, применяем пониженный базовый тариф и категорию light.
        } else if ("MOTORCYCLE".equals(lastVehicleType)) {
            base = base.add(BigDecimal.valueOf(hours * 60));
            notes += "motorcycle-rate;";
            category = "LIGHT";
        // если это автобус, ставим самый высокий базовый тариф и heavy-категорию.
        } else if ("BUS".equals(lastVehicleType)) {
            base = base.add(BigDecimal.valueOf(hours * 250));
            notes += "bus-rate;";
            category = "HEAVY";
        // если это электромобиль, используем отдельный базовый тариф и green-категорию.
        } else if ("ELECTRIC".equals(lastVehicleType)) {
            base = base.add(BigDecimal.valueOf(hours * 110));
            notes += "electric-rate;";
            category = "GREEN";
        // если это льготная категория, даем социальный тариф и social-категорию.
        } else if ("DISABLED".equals(lastVehicleType)) {
            base = base.add(BigDecimal.valueOf(hours * 40));
            notes += "accessible-rate;";
            category = "SOCIAL";
        // если тип не распознан, используем тариф обычной машины по умолчанию.
        } else {
            base = base.add(BigDecimal.valueOf(hours * 100));
            notes += "car-rate;";
        }

        // если парковка средней длительности, добавляем фиксированную надбавку.
        if (hours > 3 && hours <= 6) {
            special = special.add(BigDecimal.valueOf(120));
            notes += "mid-stay-extra;";
        }
        // если парковка длинная, повышаем спецтариф сильнее.
        if (hours > 6 && hours <= 12) {
            special = special.add(BigDecimal.valueOf(280));
            notes += "long-stay-extra;";
        }
        // если парковка очень длинная, начисляем максимальную надбавку за длительность.
        if (hours > 12) {
            special = special.add(BigDecimal.valueOf(700));
            notes += "very-long-stay-extra;";
        }

        // если въезд попал в утренний пик, добавляем пиковую доплату.
        if (entryHour >= 7 && entryHour < 10) {
            special = special.add(BigDecimal.valueOf(90));
            notes += "morning-peak;";
        }
        // если въезд попал в вечерний пик, добавляем вечернюю доплату.
        if (entryHour >= 18 && entryHour < 22) {
            special = special.add(BigDecimal.valueOf(150));
            notes += "evening-peak;";
        }
        // если въезд ночью, даем небольшую скидку вместо доплаты.
        if (entryHour >= 22 || entryHour < 6) {
            special = special.subtract(BigDecimal.valueOf(45));
            notes += "night-discount;";
        }

        // если это выходной, начисляем календарную надбавку.
        if (weekend) {
            special = special.add(BigDecimal.valueOf(130));
            notes += "weekend-surcharge;";
        }
        // если это праздник, начисляем еще более высокую надбавку.
        if (holiday) {
            special = special.add(BigDecimal.valueOf(260));
            notes += "holiday-surcharge;";
        }
        // если одновременно выходной и праздник, добавляем комбинированную надбавку.
        if (weekend && holiday) {
            special = special.add(BigDecimal.valueOf(80));
            notes += "double-calendar-surcharge;";
        }

        // если клиент VIP, применяем базовую скидку.
        if (vipClient) {
            special = special.subtract(BigDecimal.valueOf(200));
            notes += "vip-discount;";
        }
        // если VIP-клиент стоит долго, даем дополнительную скидку.
        if (vipClient && hours > 10) {
            special = special.subtract(BigDecimal.valueOf(75));
            notes += "vip-long-stay-discount;";
        }

        // если использовали зарядку, дальше смотрим, имел ли водитель на это право.
        if (usedCharger) {
            // если это электромобиль, зарядка считается платной услугой, а не нарушением.
            if ("ELECTRIC".equals(lastVehicleType)) {
                special = special.add(BigDecimal.valueOf(hours * 30));
                notes += "electric-charge;";
            // если это не электромобиль, использование зарядки считаем нарушением и штрафуем.
            } else {
                penalty = penalty.add(BigDecimal.valueOf(900));
                notes += "illegal-charge-usage;";
            }
        }

        // если использовали премиум-зону, добавляем доплату за каждый час.
        if (usedPremiumZone) {
            special = special.add(BigDecimal.valueOf(hours * 70));
            notes += "premium-zone;";
        }

        // если потерян талон, начисляем штраф за нарушение процедуры выезда.
        if (lostTicket) {
            penalty = penalty.add(BigDecimal.valueOf(1500));
            notes += "lost-ticket;";
        }
        // если машина задела или пересекла разметку, начисляем штраф за нарушение правил стоянки.
        if (crossedLines) {
            penalty = penalty.add(BigDecimal.valueOf(600));
            notes += "crossed-lines;";
        }
        // если водитель перекрыл выезд, начисляем крупный штраф за блокировку движения.
        if (blockedExit) {
            penalty = penalty.add(BigDecimal.valueOf(2500));
            notes += "blocked-exit;";
        }
        // если одновременно потерян талон и перекрыт выезд, добавляем комбинированный штраф.
        if (lostTicket && blockedExit) {
            penalty = penalty.add(BigDecimal.valueOf(400));
            notes += "lost-ticket-blocked-exit-combo;";
        }
        // если разметка нарушена именно в премиум-зоне, считаем это отдельным усилением нарушения.
        if (crossedLines && usedPremiumZone) {
            penalty = penalty.add(BigDecimal.valueOf(350));
            notes += "premium-abuse;";
        }
        // если стоянка вышла за рамки суток, начисляем штраф за каждый лишний час.
        if (hours > 24) {
            penalty = penalty.add(BigDecimal.valueOf((hours - 24) * 125));
            notes += "overtime-penalty;";
        }
        // если стоянка экстремально долгая, добавляем дополнительный крупный штраф и меняем категорию риска.
        if (hours > 48) {
            penalty = penalty.add(BigDecimal.valueOf(3000));
            notes += "extreme-overtime;";
            category = "RISK";
        }

        // если льготная машина получила слишком большой штраф, помечаем это как отдельный проблемный случай.
        if ("SOCIAL".equals(category) && penalty.compareTo(BigDecimal.valueOf(1000)) > 0) {
            category = "SOCIAL_VIOLATION";
        }
        // если green-машина использовала зарядку без других грубых нарушений, переводим ее в сервисную категорию.
        if ("GREEN".equals(category) && usedCharger && !lostTicket && !blockedExit) {
            category = "GREEN_SERVICE";
        }
        // если сумма штрафов стала критически большой, категория повышается до critical.
        if (penalty.compareTo(BigDecimal.valueOf(5000)) > 0) {
            category = "CRITICAL";
        }

        // если скидки ушли слишком глубоко в минус, ограничиваем максимальную льготу вручную.
        if (special.compareTo(BigDecimal.ZERO) < 0 && vipClient && !holiday && !weekend) {
            special = special.max(BigDecimal.valueOf(-350));
            notes += "manual-floor;";
        }

        BigDecimal total = base.add(special).add(penalty);
        // если итоговая сумма получилась слишком маленькой, принудительно выставляем минимальный счет.
        if (total.compareTo(BigDecimal.valueOf(100)) < 0) {
            total = BigDecimal.valueOf(100);
            notes += "min-bill;";
        }

        collectedTotal = collectedTotal.add(total);
        collectedPenalties = collectedPenalties.add(penalty);
        lastCategory = category;
        auditLog.append("#").append(calculations)
                .append(":").append(lastVehicleType)
                .append("|").append(hours)
                .append("|").append(entryHour)
                .append("|").append(total)
                .append("|").append(category)
                .append("; ");

        return new BadParkingSummary(base, penalty, special, total, category, notes);
    }

    public String buildDiagnosticReport() {
        return "BadParkingCalculator{"
                + "calculations=" + calculations
                + ", collectedTotal=" + collectedTotal
                + ", collectedPenalties=" + collectedPenalties
                + ", lastVehicleType='" + lastVehicleType + '\''
                + ", lastCategory='" + lastCategory + '\''
                + ", lastWeekend=" + lastWeekend
                + ", lastHoliday=" + lastHoliday
                + ", lastEntryHour=" + lastEntryHour
                + ", lastHours=" + lastHours
                + ", auditLog='" + auditLog + '\''
                + '}';
    }
}
