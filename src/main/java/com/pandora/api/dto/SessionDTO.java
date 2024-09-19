package com.pandora.api.dto;

import com.stripe.model.checkout.Session;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SessionDTO {
    private String id;
    private Session session;
}
