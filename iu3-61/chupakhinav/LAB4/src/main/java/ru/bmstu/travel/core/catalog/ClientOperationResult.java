package ru.bmstu.travel.core.catalog;

public record ClientOperationResult(boolean success, String message, CatalogClient client) {
}
