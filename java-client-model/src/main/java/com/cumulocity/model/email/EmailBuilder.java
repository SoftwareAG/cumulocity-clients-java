package com.cumulocity.model.email;

public class EmailBuilder {

    private String[] to;

    private String replyTo;

    private String[] cc;

    private String[] bcc;

    private String subject;

    private String text;

    private Attachment[] attachments;

    public static EmailBuilder aEmail() {
        return new EmailBuilder();
    }

    public EmailBuilder withTo(String... to) {
        this.to = to;
        return this;
    }

    public EmailBuilder withCc(String... cc) {
        this.cc = cc;
        return this;
    }

    public EmailBuilder withBcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    public EmailBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public EmailBuilder withReplyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public EmailBuilder withAttachments(Attachment... attachments) {
        this.attachments = attachments;
        return this;
    }

    public Email build() {
        Email result = new Email();
        result.setTo(to);
        result.setReplyTo(replyTo);
        result.setCc(cc);
        result.setBcc(bcc);
        result.setSubject(subject);
        result.setText(text);
        result.setAttachments(attachments);
        return result;
    }

}
