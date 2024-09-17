package com.pandora.api.service;

import com.pandora.api.entity.DiscountCode;
import com.pandora.api.entity.User;
import com.pandora.api.exceptions.rest.BadRequestRestException;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.exceptions.rest.UnauthorizedRestException;
import com.pandora.api.repository.DiscountCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.pandora.api.entity.User.ROLE_ADMIN;
import static com.pandora.api.entity.User.ROLE_OWNER;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountCodeRepository discountCodeRepository;
    private final AdminService adminService;

    public DiscountCode createOrUpdate(DiscountCode discountCode) {
        validateDiscount(discountCode);
        if (discountCode.getId() == null) {
            return create(discountCode);
        } else {
            return update(discountCode);
        }
    }

    public void validateDiscount(DiscountCode discountCode) {
        if (discountCode.getDiscount() > 100) {
            throw new BadRequestRestException("discount cannot be greater than 100");
        }
        if (discountCode.getMinAccounts() < 0) {
            throw new BadRequestRestException("min accounts required cannot be less than 0");
        }
    }

    @SneakyThrows
    public DiscountCode create(DiscountCode discountCode) {
        User auth = adminService.getAuthenticatedUser();
        discountCode.setCreated(new Date());
        discountCode.setUsed(0L);
        discountCode.setAdminId(auth.getId());
        return discountCodeRepository.save(discountCode);
    }

    @SneakyThrows
    public DiscountCode update(DiscountCode discountCode) {
        DiscountCode old = discountCodeRepository.findById(discountCode.getId())
                .orElseThrow(() -> new NotFoundRestException("version not found"));
        User auth = adminService.getAuthenticatedUser();
        if (!old.getAdminId().equals(auth.getId()) && auth.getRole().equals(ROLE_ADMIN)) {
            throw new UnauthorizedRestException("only creator of the discount can update it");
        }
        discountCode.setUsed(old.getUsed());
        discountCode.setCreated(old.getCreated());
        return discountCodeRepository.save(discountCode);
    }

    @SneakyThrows
    public void delete(Long id) {
        DiscountCode old = discountCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundRestException("version not found"));
        User auth = adminService.getAuthenticatedUser();
        if (!old.getAdminId().equals(auth.getId()) && auth.getRole().equals(ROLE_ADMIN)) {
            throw new UnauthorizedRestException("only creator of the discount can delete it");
        }
        discountCodeRepository.delete(old);
    }

    @SneakyThrows
    public Page<DiscountCode> list(int page) {
        User auth = adminService.getAuthenticatedUser();
        if (auth.getRole().equals(ROLE_OWNER)) {
            Page<DiscountCode> list = discountCodeRepository.findAll(PageRequest.of(page - 1, 100, Sort.Direction.DESC, "id"));
            return list;
        } else {
            Page<DiscountCode> list = discountCodeRepository.findByAdminId(auth.getId(), PageRequest.of(page - 1, 100, Sort.Direction.DESC, "id"));
            return list;
        }
    }

    @SneakyThrows
    public DiscountCode get(Long id) {
        User auth = adminService.getAuthenticatedUser();
        DiscountCode code = discountCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundRestException("application not found"));
        if (auth.getRole().equals(ROLE_ADMIN) && !code.getAdminId().equals(auth.getId())) {
            throw new UnauthorizedRestException("Unauthorized access");
        }
        return code;
    }
}
