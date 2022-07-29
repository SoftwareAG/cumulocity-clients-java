package com.cumulocity.model.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;


@Data
@Slf4j
@With
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Closeable {

    /**
     Contains singular replyTo address for an email.

     As long as the sending email implementation will be using SimpleMailMessage, which enforces this field to be
     a String, this cannot be an array. Small info regarding this:
     https://github.com/spring-projects/spring-framework/issues/4885#issuecomment-453285522
     */
    private String replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private String subject;

    private String text;

    private Attachment[] attachments;

    public Email(Email email) {
        this.replyTo = email.getReplyTo();
        this.to = email.getTo().clone();
        this.cc = email.getCc().clone();
        this.bcc = email.getBcc().clone();
        this.subject = email.getSubject();
        this.text = email.getText();
        this.attachments = email.getAttachments().clone();
    }

    @Override
    public String toString() {
        return "Email [replyTo=" + replyTo + ", to=" + Arrays.toString(to) + ", cc=" + Arrays.toString(cc) + ", bcc=" + Arrays.toString(bcc)
                + ", subject=" + subject + ", text=" + text + ", attachments=" + Arrays.toString(attachments) + "]";
    }

    @Override
    public void close() {
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                try {
                    attachment.close();
                } catch (IOException e) {
                    log.warn("Error while closing email attachment", e);
                }
            }
        }
    }
}
