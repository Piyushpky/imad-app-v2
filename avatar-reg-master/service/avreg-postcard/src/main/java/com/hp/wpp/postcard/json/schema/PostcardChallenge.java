package com.hp.wpp.postcard.json.schema;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;


public class PostcardChallenge {

    private String scheme;
    
    @JsonProperty("chip_id")
    private byte[] chipId;
    
    private byte[] bkid;
    
    private BigInteger sdiv;
    
    private byte[] hdiv;
    
    private byte[] nonce;
    
    private byte[] signature;

    /**
     * Gets the value of the scheme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the value of the scheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheme(String value) {
        this.scheme = value;
    }

    /**
     * Gets the value of the chipId property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getChipId() {
        return chipId;
    }

    /**
     * Sets the value of the chipId property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setChipId(byte[] value) {
        this.chipId = value;
    }

    /**
     * Gets the value of the bkid property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBkid() {
        return bkid;
    }

    /**
     * Sets the value of the bkid property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBkid(byte[] value) {
        this.bkid = value;
    }

    /**
     * Gets the value of the sdiv property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSdiv() {
        return sdiv;
    }

    /**
     * Sets the value of the sdiv property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSdiv(BigInteger value) {
        this.sdiv = value;
    }

    /**
     * Gets the value of the hdiv property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getHdiv() {
        return hdiv;
    }

    /**
     * Sets the value of the hdiv property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setHdiv(byte[] value) {
        this.hdiv = value;
    }

    /**
     * Gets the value of the nonce property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getNonce() {
        return nonce;
    }

    /**
     * Sets the value of the nonce property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setNonce(byte[] value) {
        this.nonce = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSignature(byte[] value) {
        this.signature = value;
    }

}
