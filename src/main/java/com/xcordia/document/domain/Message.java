package com.xcordia.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "message", length = 1000, nullable = false)
    private String message;

    @NotNull
    @Column(name = "lkp_message_type_id", nullable = false)
    private Long lkpMessageTypeId;

    @NotNull
    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @NotNull
    @Column(name = "sender_alias", nullable = false)
    private String senderAlias;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @OneToMany(mappedBy = "message")
    private Set<MessageRecipient> messageRecipients = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "messages", allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Message message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getLkpMessageTypeId() {
        return lkpMessageTypeId;
    }

    public Message lkpMessageTypeId(Long lkpMessageTypeId) {
        this.lkpMessageTypeId = lkpMessageTypeId;
        return this;
    }

    public void setLkpMessageTypeId(Long lkpMessageTypeId) {
        this.lkpMessageTypeId = lkpMessageTypeId;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public Message senderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
        return this;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderAlias() {
        return senderAlias;
    }

    public Message senderAlias(String senderAlias) {
        this.senderAlias = senderAlias;
        return this;
    }

    public void setSenderAlias(String senderAlias) {
        this.senderAlias = senderAlias;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Message createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Message dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<MessageRecipient> getMessageRecipients() {
        return messageRecipients;
    }

    public Message messageRecipients(Set<MessageRecipient> messageRecipients) {
        this.messageRecipients = messageRecipients;
        return this;
    }

    public Message addMessageRecipients(MessageRecipient messageRecipient) {
        this.messageRecipients.add(messageRecipient);
        messageRecipient.setMessage(this);
        return this;
    }

    public Message removeMessageRecipients(MessageRecipient messageRecipient) {
        this.messageRecipients.remove(messageRecipient);
        messageRecipient.setMessage(null);
        return this;
    }

    public void setMessageRecipients(Set<MessageRecipient> messageRecipients) {
        this.messageRecipients = messageRecipients;
    }

    public Order getOrder() {
        return order;
    }

    public Message order(Order order) {
        this.order = order;
        return this;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", lkpMessageTypeId=" + getLkpMessageTypeId() +
            ", senderUserId=" + getSenderUserId() +
            ", senderAlias='" + getSenderAlias() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
