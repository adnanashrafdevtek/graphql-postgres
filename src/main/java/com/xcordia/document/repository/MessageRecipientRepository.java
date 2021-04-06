package com.xcordia.document.repository;

import com.xcordia.document.domain.MessageRecipient;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MessageRecipient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRecipientRepository extends JpaRepository<MessageRecipient, Long>, JpaSpecificationExecutor<MessageRecipient> {
}
