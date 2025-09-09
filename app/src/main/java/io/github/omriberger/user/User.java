package io.github.omriberger.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class User implements Serializable {
    private final String id;
    private final String userId;
    private final String schoolName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean gender; // false = male, true = female
    private final int institutionCode;
    private final String classCode;
    private final int classNumber;
    private final String token;
    private final String cellphone;

    private final StringBuilder builder = new StringBuilder();
    public String getClassCombo() {
        return builder.append(classCode).append("|").append(classNumber).toString(); // Example: 11|5
    }

    public String getFullName() {
        return builder.append(firstName).append(" ").append(lastName).toString();
    }

}
