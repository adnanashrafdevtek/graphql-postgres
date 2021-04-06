package com.xcordia.document.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.xcordia.document.domain.Message} entity. This class is used
 * in {@link com.xcordia.document.web.rest.MessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private LongFilter lkpMessageTypeId;

    private LongFilter senderUserId;

    private StringFilter senderAlias;

    private StringFilter createdBy;

    private InstantFilter dateCreated;

    private LongFilter messageRecipientsId;

    private LongFilter orderId;

    public MessageCriteria() {
    }

    public MessageCriteria(MessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.lkpMessageTypeId = other.lkpMessageTypeId == null ? null : other.lkpMessageTypeId.copy();
        this.senderUserId = other.senderUserId == null ? null : other.senderUserId.copy();
        this.senderAlias = other.senderAlias == null ? null : other.senderAlias.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.messageRecipientsId = other.messageRecipientsId == null ? null : other.messageRecipientsId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
    }

    @Override
    public MessageCriteria copy() {
        return new MessageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public LongFilter getLkpMessageTypeId() {
        return lkpMessageTypeId;
    }

    public void setLkpMessageTypeId(LongFilter lkpMessageTypeId) {
        this.lkpMessageTypeId = lkpMessageTypeId;
    }

    public LongFilter getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(LongFilter senderUserId) {
        this.senderUserId = senderUserId;
    }

    public StringFilter getSenderAlias() {
        return senderAlias;
    }

    public void setSenderAlias(StringFilter senderAlias) {
        this.senderAlias = senderAlias;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LongFilter getMessageRecipientsId() {
        return messageRecipientsId;
    }

    public void setMessageRecipientsId(LongFilter messageRecipientsId) {
        this.messageRecipientsId = messageRecipientsId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageCriteria that = (MessageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(lkpMessageTypeId, that.lkpMessageTypeId) &&
            Objects.equals(senderUserId, that.senderUserId) &&
            Objects.equals(senderAlias, that.senderAlias) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(messageRecipientsId, that.messageRecipientsId) &&
            Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        message,
        lkpMessageTypeId,
        senderUserId,
        senderAlias,
        createdBy,
        dateCreated,
        messageRecipientsId,
        orderId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (lkpMessageTypeId != null ? "lkpMessageTypeId=" + lkpMessageTypeId + ", " : "") +
                (senderUserId != null ? "senderUserId=" + senderUserId + ", " : "") +
                (senderAlias != null ? "senderAlias=" + senderAlias + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (messageRecipientsId != null ? "messageRecipientsId=" + messageRecipientsId + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
            "}";
    }

}
