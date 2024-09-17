package com.pandora.api.client.protocol;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VerifyRequestBody {
    private int registrationId;
    private String signalingKey;
    private boolean voice;
}
