package com.xcordia.document.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @Column(name = "updated_by")
    private String updatedBy;

    @NotNull
    @Column(name = "buyer_user_id", nullable = false)
    private Long buyerUserId;

    @NotNull
    @Column(name = "buyer_organization_id", nullable = false)
    private Long buyerOrganizationId;

    @NotNull
    @Column(name = "supplier_organization_id", nullable = false)
    private Long supplierOrganizationId;

    @NotNull
    @Column(name = "primary_supplier_user_id", nullable = false)
    private Long primarySupplierUserId;

    @OneToMany(mappedBy = "order")
    private Set<OrderUser> orderUsers = new HashSet<>();

    @OneToMany(mappedBy = "order")
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Order name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public Order uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Order dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Order createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Order dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Order updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getBuyerUserId() {
        return buyerUserId;
    }

    public Order buyerUserId(Long buyerUserId) {
        this.buyerUserId = buyerUserId;
        return this;
    }

    public void setBuyerUserId(Long buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public Long getBuyerOrganizationId() {
        return buyerOrganizationId;
    }

    public Order buyerOrganizationId(Long buyerOrganizationId) {
        this.buyerOrganizationId = buyerOrganizationId;
        return this;
    }

    public void setBuyerOrganizationId(Long buyerOrganizationId) {
        this.buyerOrganizationId = buyerOrganizationId;
    }

    public Long getSupplierOrganizationId() {
        return supplierOrganizationId;
    }

    public Order supplierOrganizationId(Long supplierOrganizationId) {
        this.supplierOrganizationId = supplierOrganizationId;
        return this;
    }

    public void setSupplierOrganizationId(Long supplierOrganizationId) {
        this.supplierOrganizationId = supplierOrganizationId;
    }

    public Long getPrimarySupplierUserId() {
        return primarySupplierUserId;
    }

    public Order primarySupplierUserId(Long primarySupplierUserId) {
        this.primarySupplierUserId = primarySupplierUserId;
        return this;
    }

    public void setPrimarySupplierUserId(Long primarySupplierUserId) {
        this.primarySupplierUserId = primarySupplierUserId;
    }

    public Set<OrderUser> getOrderUsers() {
        return orderUsers;
    }

    public Order orderUsers(Set<OrderUser> orderUsers) {
        this.orderUsers = orderUsers;
        return this;
    }

    public Order addOrderUsers(OrderUser orderUser) {
        this.orderUsers.add(orderUser);
        orderUser.setOrder(this);
        return this;
    }

    public Order removeOrderUsers(OrderUser orderUser) {
        this.orderUsers.remove(orderUser);
        orderUser.setOrder(null);
        return this;
    }

    public void setOrderUsers(Set<OrderUser> orderUsers) {
        this.orderUsers = orderUsers;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Order messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Order addMessages(Message message) {
        this.messages.add(message);
        message.setOrder(this);
        return this;
    }

    public Order removeMessages(Message message) {
        this.messages.remove(message);
        message.setOrder(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order orderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Order addOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public Order removeOrderItems(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", buyerUserId=" + getBuyerUserId() +
            ", buyerOrganizationId=" + getBuyerOrganizationId() +
            ", supplierOrganizationId=" + getSupplierOrganizationId() +
            ", primarySupplierUserId=" + getPrimarySupplierUserId() +
            "}";
    }
}
