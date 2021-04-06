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
 * Criteria class for the {@link com.xcordia.document.domain.MessageRecipient} entity. This class is used
 * in {@link com.xcordia.document.web.rest.MessageRecipientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /message-recipients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageRecipientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private BooleanFilter read;

    private InstantFilter dateMessageRead;

    private LongFilter messageId;

    public MessageRecipientCriteria() {
    }

    public MessageRecipientCriteria(MessageRecipientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.read = other.read == null ? null : other.read.copy();
        this.dateMessageRead = other.dateMessageRead == null ? null : other.dateMessageRead.copy();
        this.messageId = other.messageId == null ? null : other.messageId.copy();
    }

    @Override
    public MessageRecipientCriteria copy() {
        return new MessageRecipientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getRead() {
        return read;
    }

    public void setRead(BooleanFilter read) {
        this.read = read;
    }

    public InstantFilter getDateMessageRead() {
        return dateMessageRead;
    }

    public void setDateMessageRead(InstantFilter dateMessageRead) {
        this.dateMessageRead = dateMessageRead;
    }

    public LongFilter getMessageId() {
        return messageId;
    }

    public void setMessageId(LongFilter messageId) {
        this.messageId = messageId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageRecipientCriteria that = (MessageRecipientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(read, that.read) &&
            Objects.equals(dateMessageRead, that.dateMessageRead) &&
            Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userId,
        read,
        dateMessageRead,
        messageId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageRecipientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (read != null ? "read=" + read + ", " : "") +
                (dateMessageRead != null ? "dateMessageRead=" + dateMessageRead + ", " : "") +
                (messageId != null ? "messageId=" + messageId + ", " : "") +
            "}";
    }

}
