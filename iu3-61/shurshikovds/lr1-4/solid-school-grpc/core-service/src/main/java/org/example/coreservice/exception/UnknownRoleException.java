package org.example.coreservice.exception;

import org.example.coreservice.entity.enums.UserRole;

public class UnknownRoleException extends SystemException {
    public UnknownRoleException(UserRole role) {
        super("Неизвестная роль: " + role + ". Это баг, сообщите разработчику");
    }
}
