package storage;

import exception.StorageException;
import model.ParkingTicket;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FileParkingStorage implements ParkingStorage {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String DELIM = "|";
    private final String filePath;

    public FileParkingStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Map<Integer, ParkingTicket> active, List<ParkingTicket> history) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ParkingTicket t : active.values()) {
                writer.write("A" + DELIM + t.getLicensePlate() + DELIM + t.getSpaceId() + DELIM +
                        t.getEntryTime().format(FMT) + DELIM + "null");
                writer.newLine();
            }
            for (ParkingTicket t : history) {
                writer.write("H" + DELIM + t.getLicensePlate() + DELIM + t.getSpaceId() + DELIM +
                        t.getEntryTime().format(FMT) + DELIM + t.getExitTime().format(FMT));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("Ошибка сохранения в файл: " + e.getMessage());
        }
    }

    @Override
    public void load(Map<Integer, ParkingTicket> active, List<ParkingTicket> history) {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue;

                String type = parts[0];
                String plate = parts[1];
                int space = Integer.parseInt(parts[2]);
                LocalDateTime entry = LocalDateTime.parse(parts[3], FMT);
                LocalDateTime exit = "null".equals(parts[4]) ? null : LocalDateTime.parse(parts[4], FMT);

                ParkingTicket ticket = new ParkingTicket(plate, space, entry);
                if ("A".equals(type)) {
                    active.put(space, ticket);
                } else if ("H".equals(type)) {
                    ticket.setExitTime(exit);
                    history.add(ticket);
                }
            }
        } catch (Exception e) {
            throw new StorageException("Ошибка загрузки из файла: " + e.getMessage());
        }
    }
}