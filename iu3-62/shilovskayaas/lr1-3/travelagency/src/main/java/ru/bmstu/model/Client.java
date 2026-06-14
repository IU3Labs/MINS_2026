package ru.bmstu.model;

import java.math.BigDecimal;

public class Client {
    private int id;
    private String name;
    private String email;
    private String password;
    private BigDecimal personalDiscount;

    public Client(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.personalDiscount = BigDecimal.ZERO;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; } // геттер для пароля
    public BigDecimal getPersonalDiscount() { return personalDiscount; }

    public void setId(int id) { this.id = id; }
    public void setPersonalDiscount(BigDecimal personalDiscount) {
        this.personalDiscount = personalDiscount;
    }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | Персональная скидка: %.2f%%",
                id, name, email, personalDiscount);
    }
}