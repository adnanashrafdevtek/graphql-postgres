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
 * Criteria class for the {@link com.xcordia.document.domain.Order} entity. This class is used
 * in {@link com.xcordia.document.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter uuid;

    private InstantFilter dateCreated;

    private StringFilter createdBy;

    private InstantFilter dateUpdated;

    private StringFilter updatedBy;

    private LongFilter buyerUserId;

    private LongFilter buyerOrganizationId;

    private LongFilter supplierOrganizationId;

    private LongFilter primarySupplierUserId;

    private LongFilter orderUsersId;

    private LongFilter messagesId;

    private LongFilter orderItemsId;

    public OrderCriteria() {
    }

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.buyerUserId = other.buyerUserId == null ? null : other.buyerUserId.copy();
        this.buyerOrganizationId = other.buyerOrganizationId == null ? null : other.buyerOrganizationId.copy();
        this.supplierOrganizationId = other.supplierOrganizationId == null ? null : other.supplierOrganizationId.copy();
        this.primarySupplierUserId = other.primarySupplierUserId == null ? null : other.primarySupplierUserId.copy();
        this.orderUsersId = other.orderUsersId == null ? null : other.orderUsersId.copy();
        this.messagesId = other.messagesId == null ? null : other.messagesId.copy();
        this.orderItemsId = other.orderItemsId == null ? null : other.orderItemsId.copy();
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUuid() {
        return uuid;
    }

    public void setUuid(StringFilter uuid) {
        this.uuid = uuid;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(InstantFilter dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LongFilter getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(LongFilter buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public LongFilter getBuyerOrganizationId() {
        return buyerOrganizationId;
    }

    public void setBuyerOrganizationId(LongFilter buyerOrganizationId) {
        this.buyerOrganizationId = buyerOrganizationId;
    }

    public LongFilter getSupplierOrganizationId() {
        return supplierOrganizationId;
    }

    public void setSupplierOrganizationId(LongFilter supplierOrganizationId) {
        this.supplierOrganizationId = supplierOrganizationId;
    }

    public LongFilter getPrimarySupplierUserId() {
        return primarySupplierUserId;
    }

    public void setPrimarySupplierUserId(LongFilter primarySupplierUserId) {
        this.primarySupplierUserId = primarySupplierUserId;
    }

    public LongFilter getOrderUsersId() {
        return orderUsersId;
    }

    public void setOrderUsersId(LongFilter orderUsersId) {
        this.orderUsersId = orderUsersId;
    }

    public LongFilter getMessagesId() {
        return messagesId;
    }

    public void setMessagesId(LongFilter messagesId) {
        this.messagesId = messagesId;
    }

    public LongFilter getOrderItemsId() {
        return orderItemsId;
    }

    public void setOrderItemsId(LongFilter orderItemsId) {
        this.orderItemsId = orderItemsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(buyerUserId, that.buyerUserId) &&
            Objects.equals(buyerOrganizationId, that.buyerOrganizationId) &&
            Objects.equals(supplierOrganizationId, that.supplierOrganizationId) &&
            Objects.equals(primarySupplierUserId, that.primarySupplierUserId) &&
            Objects.equals(orderUsersId, that.orderUsersId) &&
            Objects.equals(messagesId, that.messagesId) &&
            Objects.equals(orderItemsId, that.orderItemsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        uuid,
        dateCreated,
        createdBy,
        dateUpdated,
        updatedBy,
        buyerUserId,
        buyerOrganizationId,
        supplierOrganizationId,
        primarySupplierUserId,
        orderUsersId,
        messagesId,
        orderItemsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (uuid != null ? "uuid=" + uuid + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
                (buyerUserId != null ? "buyerUserId=" + buyerUserId + ", " : "") +
                (buyerOrganizationId != null ? "buyerOrganizationId=" + buyerOrganizationId + ", " : "") +
                (supplierOrganizationId != null ? "supplierOrganizationId=" + supplierOrganizationId + ", " : "") +
                (primarySupplierUserId != null ? "primarySupplierUserId=" + primarySupplierUserId + ", " : "") +
                (orderUsersId != null ? "orderUsersId=" + orderUsersId + ", " : "") +
                (messagesId != null ? "messagesId=" + messagesId + ", " : "") +
                (orderItemsId != null ? "orderItemsId=" + orderItemsId + ", " : "") +
            "}";
    }

}
