package com.pandora.api.repository;

import com.pandora.api.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByAdminId(Long adminId);

    Optional<Account> findFirstByUserId(String id);
    void deleteByuserId(String id);

    @Query("select a from Account a where a.userId like ?1 and a.wipe = false")
    Page<Account> searchActive(String userId, Date date, Pageable pageable);

    @Query("select a from Account a where a.userId like ?1 and a.wipe = true")
    Page<Account> searchWiped(String userId, Pageable pageable);

    @Query("select a from Account a where a.userId like ?1")
    Page<Account> search(String userId, Pageable pageable);


    @Query("select a from Account a where a.adminId = ?2 and a.userId like ?1 and a.wipe = false")
    Page<Account> searchActiveByAdminId(String userId,Long id, Pageable pageable);

    @Query("select a from Account a where a.adminId = ?2 and a.userId like ?1 and a.wipe = true")
    Page<Account> searchWipedByAdminId(String userId,Long id, Pageable pageable);

    @Query("select a from Account a where a.adminId = ?2 and a.userId like ?1")
    Page<Account> searchByAdminId(String userId,Long id, Pageable pageable);
}
