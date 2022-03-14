package com.webstore.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webstore.base.NovaRequest;
import com.webstore.base.NovaResponse;
import com.webstore.domain.SchedulerProtocol;
import com.webstore.service.AddressGeneralService;
import com.webstore.service.SchedulerProtocolService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;


public class NovaImportBranchTask {

    public void importBranchNova(String novaUrl) {

        AddressGeneralService addressGeneralService = BeanUtil.getBean(AddressGeneralService.class);
        SchedulerProtocolService schedulerProtocolService = BeanUtil.getBean(SchedulerProtocolService.class);
        SchedulerProtocol schedulerProtocol = new SchedulerProtocol("NovaImportBranchTask");
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        MediaType mediaType = MediaType.parse("application/json");
        schedulerProtocol.addLineProtocol("NovaImportBranchTask start");
        NovaRequest novaRequest = new NovaRequest();
        novaRequest.createMethodProperties()
                .setLanguage("ru");
        // .setSettlementRef("e71629ab-4b33-11e4-ab6d-005056801329"); //если по конкретному городу?

        novaRequest.setModelName("AddressGeneral");
        novaRequest.setCalledMethod("getWarehouses");
        //novaRequest.setApiKey("100e5e1e9d91efc3e76bd0122dec672f");
        novaRequest.setApiKey(addressGeneralService.getNovaKey());
        String json = null;
        try {
            json = mapper.writeValueAsString(novaRequest);
            schedulerProtocol.addLineProtocol(json);
        } catch (JsonProcessingException e) {
            schedulerProtocol.addLineProtocol(e.getMessage());
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                // .url("https://api.novaposhta.ua/v2.0/json/")
                //   .url(environment.getProperty("nova.url-import-branch"))
                .url(novaUrl)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            NovaResponse novaResponse = mapper.readValue(jsonData, NovaResponse.class);
            schedulerProtocol.addLineProtocol(novaResponse.getSuccess().toString());
            addressGeneralService.saveAll(novaResponse.getData());


        } catch (IOException e) {
            schedulerProtocol.addLineProtocol(e.getMessage());
            e.printStackTrace();
        } finally {
            schedulerProtocolService.save(schedulerProtocol);
        }

    }

}
