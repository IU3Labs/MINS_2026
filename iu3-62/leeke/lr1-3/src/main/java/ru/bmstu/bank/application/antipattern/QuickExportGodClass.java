package ru.bmstu.bank.application.antipattern;

import ru.bmstu.bank.application.service.AccountService;
import ru.bmstu.bank.application.service.TransactionService;
import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class QuickExportGodClass {
    private static final String EXPORT_PATH = "C:/Users/79146/OneDrive/Рабочий стол/учеба/минс/";
    private static final BigDecimal THRESHOLD_LOW = new BigDecimal("5000");
    private static final BigDecimal THRESHOLD_MID = new BigDecimal("15000");
    private static final BigDecimal FEE_LOW = new BigDecimal("0.05");
    private static final BigDecimal FEE_MID = new BigDecimal("0.10");
    private static final BigDecimal FEE_HIGH = new BigDecimal("0.15");

    private final AccountService accountService;
    private final TransactionService transactionService;

    public QuickExportGodClass(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public String exportAndCalculateFee(Long accountId) {
        String result = "UNKNOWN";
        try {
            Account acc = accountService.getAccount(accountId);
            List<Transaction> txs = transactionService.getStatement(accountId);
            BigDecimal fee = BigDecimal.ZERO;
            if (acc.getBalance().compareTo(THRESHOLD_LOW) > 0) {
                fee = acc.getBalance().multiply(FEE_LOW);
            } else if (acc.getBalance().compareTo(THRESHOLD_MID) > 0) {
                fee = acc.getBalance().multiply(FEE_MID);
            } else {
                fee = acc.getBalance().multiply(FEE_HIGH);
            }
            StringBuilder json = new StringBuilder();
            json.append("{\"id\":").append(acc.getId())
                    .append(",\"owner\":\"").append(acc.getOwnerName()).append("\"")
                    .append(",\"balance\":").append(acc.getBalance())
                    .append(",\"fee\":").append(fee)
                    .append(",\"exportedAt\":\"").append(LocalDateTime.now()).append("\"")
                    .append(",\"transactions\":[");

            for (int i = 0; i < txs.size(); i++) {
                Transaction t = txs.get(i);
                json.append("{\"id\":").append(t.getId())
                        .append(",\"type\":\"").append(t.getType()).append("\"")
                        .append(",\"amount\":").append(t.getAmount())
                        .append(",\"status\":\"").append(t.getStatus()).append("\"");
                if (i < txs.size() - 1) json.append(",");
            }
            json.append("]}");
            try (FileWriter fw = new FileWriter(EXPORT_PATH + accountId + ".json")) {
                fw.write(json.toString());
            } catch (IOException e) {
            }
            result = "SUCCESS | Fee: " + fee + " | Path: " + EXPORT_PATH + accountId + ".json";
        } catch (Exception e) {
            result = "SILENT_FAILURE";
        }
        return result;
    }
}