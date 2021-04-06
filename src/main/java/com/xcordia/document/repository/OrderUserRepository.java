package com.xcordia.document.repository;

import com.xcordia.document.domain.OrderUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OrderUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderUserRepository extends JpaRepository<OrderUser, Long>, JpaSpecificationExecutor<OrderUser> {
}
