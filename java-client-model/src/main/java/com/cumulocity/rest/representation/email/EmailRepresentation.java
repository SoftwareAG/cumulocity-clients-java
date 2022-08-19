package com.cumulocity.rest.representation.email;

import com.cumulocity.model.email.Attachment;
import com.cumulocity.model.email.Email;
import com.cumulocity.model.email.EmailBuilder;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRepresentation extends AbstractExtensibleRepresentation {

    private String replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private String subject;

    private String text;

    private Attachment[] attachments;

    public Email toEmail() {
        return EmailBuilder.aEmail()
                .withReplyTo(replyTo)
                .withTo(to)
                .withCc(cc)
                .withBcc(bcc)
                .withSubject(subject)
                .withText(text)
                .withAttachments(attachments)
                .build();
    }
}
