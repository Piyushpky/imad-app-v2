package com.hp.wpp.avatar.restapp.then;

public abstract class Response {
	
	private com.jayway.restassured.response.Response response;

    public void setResponse(com.jayway.restassured.response.Response response){
        this.response = response;
        afterSetResponse();
    }

    protected int getStatusCode(){
        return response.getStatusCode();
    }

    public com.jayway.restassured.response.Response getResponse() {
        return response;
    }

    void afterSetResponse() {
    }
}
