/**
 * 
 */
package com.hp.wpp.postcard.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author mahammad
 *
 */
@Entity
@Table(name = "postcard_certificate_info", uniqueConstraints={@UniqueConstraint(columnNames = {"id", "certificate_serial_number"})})
public class PostcardCertificateInfoEntity {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="certificate_serial_number")
	private String certificateSerialNum;
	
	@Column(name="certificate_data")
	private byte[] certificateData;
	
	@Column(name="privatekey_data")
	private byte[] privateKeyData;
	
	@Column(name="certificate_owner")
	private String certificateOwner;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCertificateSerialNum() {
		return certificateSerialNum;
	}

	public void setCertificateSerialNum(String certificateSerialNum) {
		this.certificateSerialNum = certificateSerialNum;
	}

	public byte[] getCertificateData() {
		return certificateData;
	}

	public void setCertificateData(byte[] certificateData) {
		this.certificateData = certificateData;
	}

	public String getCertificateOwner() {
		return certificateOwner;
	}

	public void setCertificateOwner(String certificateOwner) {
		this.certificateOwner = certificateOwner;
	}

	public byte[] getPrivateKeyData() {
		return privateKeyData;
	}

	public void setPrivateKeyData(byte[] privateKeyData) {
		this.privateKeyData = privateKeyData;
	}
}
