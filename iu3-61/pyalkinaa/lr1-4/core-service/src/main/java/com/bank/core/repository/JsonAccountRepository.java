package com.bank.core.repository;

import com.bank.core.model.Account;
import com.bank.core.model.AccountType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonAccountRepository implements AccountRepository {
    private final String filePath;
    private final List<Account> accounts = new ArrayList<>();

    public JsonAccountRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }

    @Override
    public void save(Account account) {
        accounts.removeIf(existing -> existing.getId() == account.getId());
        accounts.add(account);
        saveToFile();
    }

    @Override
    public Optional<Account> findById(long id) {
        return accounts.stream().filter(account -> account.getId() == id).findFirst();
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts);
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("[");
            for (int i = 0; i < accounts.size(); i++) {
                writer.println(accountToJson(accounts.get(i)));
                if (i < accounts.size() - 1) {
                    writer.println(",");
                }
            }
            writer.println("]");
        } catch (IOException e) {
            System.err.println("Ошибка записи bank_data.json: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try {
            String content = Files.readString(file.toPath()).trim();
            if (content.isBlank() || content.equals("[]")) {
                return;
            }
            String cleaned = content;
            if (cleaned.startsWith("[")) cleaned = cleaned.substring(1);
            if (cleaned.endsWith("]")) cleaned = cleaned.substring(0, cleaned.length() - 1);
            String[] jsonObjects = cleaned.split("\\}\\s*,\\s*\\{");
            for (String raw : jsonObjects) {
                String normalized = raw;
                if (!normalized.trim().startsWith("{")) normalized = "{" + normalized;
                if (!normalized.trim().endsWith("}")) normalized = normalized + "}";
                Account account = parseAccount(normalized);
                if (account != null) {
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения bank_data.json: " + e.getMessage());
        }
    }

    private String accountToJson(Account account) {
        return "  {\n" +
                "    \"id\": " + account.getId() + ",\n" +
                "    \"ownerName\": \"" + escapeJson(account.getOwnerName()) + "\",\n" +
                "    \"accountType\": \"" + account.getAccountType() + "\",\n" +
                "    \"balance\": " + account.getBalance() + "\n" +
                "  }";
    }

    private Account parseAccount(String json) {
        try {
            Long id = extractLong(json, "id");
            String ownerName = extractString(json, "ownerName");
            String accountType = extractString(json, "accountType");
            Double balance = extractDouble(json, "balance");
            if (id == null || ownerName == null || accountType == null || balance == null) {
                return null;
            }
            Account account = new Account(id, ownerName, AccountType.fromString(accountType));
            account.setBalance(balance);
            return account;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractString(String json, String key) {
        String search = "\"" + key + "\": \"";
        int start = json.indexOf(search);
        if (start == -1) {
            return null;
        }
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private Long extractLong(String json, String key) {
        String search = "\"" + key + "\": ";
        int start = json.indexOf(search);
        if (start == -1) {
            return null;
        }
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("\n", start);
        return Long.parseLong(json.substring(start, end).trim());
    }

    private Double extractDouble(String json, String key) {
        String search = "\"" + key + "\": ";
        int start = json.indexOf(search);
        if (start == -1) {
            return null;
        }
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("\n", start);
        if (end == -1) end = json.indexOf("}", start);
        return Double.parseDouble(json.substring(start, end).trim());
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
