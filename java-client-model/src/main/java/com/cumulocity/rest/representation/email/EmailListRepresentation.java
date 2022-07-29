package com.cumulocity.rest.representation.email;

import com.cumulocity.model.email.Email;
import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import org.svenson.JSONTypeHint;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailListRepresentation extends BaseCollectionRepresentation<Email> {

    @Delegate
    public Iterable<Email> emails;

    @JSONTypeHint(Email.class)
    public void setEmails(Iterable<Email> emails) {
        this.emails = emails;
    }

}
