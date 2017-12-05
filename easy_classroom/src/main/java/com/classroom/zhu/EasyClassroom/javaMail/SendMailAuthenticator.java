package com.classroom.zhu.EasyClassroom.javaMail;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SendMailAuthenticator extends Authenticator {
    private String username = "";
    private String password = "";

    public void check(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
} 