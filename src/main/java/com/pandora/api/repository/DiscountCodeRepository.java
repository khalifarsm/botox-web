package com.pandora.api.repository;

import com.pandora.api.entity.DiscountCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Long> {
    DiscountCode findFirstByCode(String code);
    Page<DiscountCode> findByAdminId(Long id,Pageable pageable);
}
