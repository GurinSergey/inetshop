package com.webstore.base;

import com.fasterxml.jackson.annotation.JsonProperty;

class MethodProperties {
    @JsonProperty("Language")
    private String language;
    @JsonProperty("SettlementRef")
    private String settlementRef;

    public MethodProperties() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSettlementRef() {
        return settlementRef;
    }

    public void setSettlementRef(String settlementRef) {
        this.settlementRef = settlementRef;
    }
}

public class NovaRequest {
   private String modelName;
    private  String calledMethod;
    private  String apiKey;
    private MethodProperties methodProperties;


    public NovaRequest() {
    }


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(String calledMethod) {
        this.calledMethod = calledMethod;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public MethodProperties getMethodProperties() {
        return methodProperties;
    }

    public void setMethodProperties(MethodProperties methodProperties) {
        this.methodProperties = methodProperties;
    }

    public  NovaRequest createMethodProperties(){
         this.methodProperties = new MethodProperties();
         return this;
    }


    public NovaRequest setLanguage(String language) {
        this.methodProperties.setLanguage(language);
        return this;
    }

    public NovaRequest setSettlementRef(String settlementRef) {
        this.methodProperties.setSettlementRef(settlementRef);
        return this;
    }


}
