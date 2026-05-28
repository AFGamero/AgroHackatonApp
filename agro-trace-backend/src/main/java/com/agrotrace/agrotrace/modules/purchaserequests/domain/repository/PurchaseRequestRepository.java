package com.agrotrace.agrotrace.modules.purchaserequests.domain.repository;

import com.agrotrace.agrotrace.modules.purchaserequests.domain.model.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, UUID> {
    List<PurchaseRequest> findByLotId(UUID lotId);
    List<PurchaseRequest> findByEmail(String email);
}
