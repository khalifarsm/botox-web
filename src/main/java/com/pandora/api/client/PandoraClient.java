package com.pandora.api.client;

import com.pandora.api.client.protocol.WipeDTO;
import com.pandora.api.exceptions.rest.BadRequestRestException;
import com.pandora.api.util.ObjectMapperImp;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class PandoraClient {

    @Value("${pandora.api.server.url}")
    private String url;

    @PostConstruct
    public void postConstruct() {
        Unirest.config().setObjectMapper(new ObjectMapperImp());
    }


    public void wipeAccount(String pandoraId, String code) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.post(url + "/v1/accounts/wipe/wipe-code")
                    .header("Content-Type","application/json")
                    .body(new WipeDTO()
                            .setPandoraId(pandoraId)
                            .setWipeCode(code))
                    .asString();
            log.info("pandora api response {} {}",response.getStatus(),response.getBody());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (response == null || !isSuccess(response.getStatus())) {
            throw new BadRequestRestException("Failed to wipe account");
        }
    }



    public boolean isSuccess(int code) {
        return code >= 200 && code < 300;
    }
}
