package com.university.service;

import com.university.model.*;
import com.university.exceptions.*;

public interface RegistrationService {
    Student registerStudent(String name) throws StudentAlreadyRegisteredException, InvalidNameException;

}
