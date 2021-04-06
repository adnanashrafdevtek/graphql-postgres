package com.xcordia.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A MessageRecipient.
 */
@Entity
@Table(name = "message_recipient")
public class MessageRecipient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "read", nullable = false)
    private Boolean read;

    @Column(name = "date_message_read")
    private Instant dateMessageRead;

    @ManyToOne
    @JsonIgnoreProperties(value = "messageRecipients", allowSetters = true)
    private Message message;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public MessageRecipient userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean isRead() {
        return read;
    }

    public MessageRecipient read(Boolean read) {
        this.read = read;
        return this;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Instant getDateMessageRead() {
        return dateMessageRead;
    }

    public MessageRecipient dateMessageRead(Instant dateMessageRead) {
        this.dateMessageRead = dateMessageRead;
        return this;
    }

    public void setDateMessageRead(Instant dateMessageRead) {
        this.dateMessageRead = dateMessageRead;
    }

    public Message getMessage() {
        return message;
    }

    public MessageRecipient message(Message message) {
        this.message = message;
        return this;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageRecipient)) {
            return false;
        }
        return id != null && id.equals(((MessageRecipient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageRecipient{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", read='" + isRead() + "'" +
            ", dateMessageRead='" + getDateMessageRead() + "'" +
            "}";
    }
}
