package com.hp.wpp.ssnc.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.config.DynamicPropertyFactory;

@Component
public class PrinterResourceServiceConfig {

	private static final String VERSION = "META_AVATAR-LOOKUP-SERVICE_VERSION";
	private static final String HYSTRIX_THREAD_POOL_CORE_SIZE_AVDLS = "hystrix.threadpool.core.size.avdls";
	private static final String HYSTRIX_THREAD_POOL_MAXQUEUE_SIZE_AVDLS = "hystrix.threadpool.maxqueue.size.avdls";
	private static final String HYSTRIX_THREAD_POOL_QUEUE_REJECTION_SIZE_AVDLS = "hystrix.threadpool.queue.rejection.threshold.avdls";
	private static final String HYSTRIX_EXECUTION_TIMEOUT_AVDLS = "hystrix.execution.timeout.avdls";
	private static final String HYSTRIX_THREAD_INTERRUPT_ON_TIMEOUT_AVDLS = "hystrix.thread.interruptOnTimeout.avdls";
	private static final String HYSTRIX_FALLBACK_ENABLED_AVDLS = "hystrix.fallback.enabled.avdls";
	private static final String HYSTRIX_KEY_NAME_AVDLS = "hystix.key.name.avdls";
	@Autowired
	private DynamicPropertyFactory dynamicPropertyFactory;

	public String getVersion() {
		return String.valueOf(DynamicPropertyFactory.getInstance()
				.getStringProperty(VERSION, "1.0.0").getValue());
	}
	public boolean isTestDomainKeyDecodeIgnored() {
		return Boolean.valueOf(DynamicPropertyFactory.getInstance()
				.getBooleanProperty("ssnc.test_domain_key.decode.ignore", false).getValue());
	}
	public String getDomainKeyEncyptedKeyValue() {
		return String.valueOf(DynamicPropertyFactory.getInstance()
				.getStringProperty("ssnc.domain_key.encyrpted.value", "UGLLQXZHDGFYUGXH").getValue());
	}

	public String getAvDisAuthHeader() {
		return String.valueOf(DynamicPropertyFactory.getInstance()
				.getStringProperty("ssnc.avdis.auth.header", "Basic YXZjX3Rlc3RfNDkxNV9kZXY6OGUzNDJhNzEtMWU1OC00M2Y1LTljZWEtODIxMDliZTRjODQz").getValue());
	}

	public int getHystixThreadPoolCoreSizeAVDLS() {
		return dynamicPropertyFactory.getIntProperty(HYSTRIX_THREAD_POOL_CORE_SIZE_AVDLS, 15).getValue();
	}

	public int getHystixThreadPoolMaxQueueSizeAVDLS() {
		return dynamicPropertyFactory.getIntProperty(HYSTRIX_THREAD_POOL_MAXQUEUE_SIZE_AVDLS, 100).getValue();
	}

	public int getHystixThreadRejectionSizeAVDLS() {
		return dynamicPropertyFactory.getIntProperty(HYSTRIX_THREAD_POOL_QUEUE_REJECTION_SIZE_AVDLS, 100).getValue();
	}

	public int getHystixExecutionTimeOutAVDLS() {
		return dynamicPropertyFactory.getIntProperty(HYSTRIX_EXECUTION_TIMEOUT_AVDLS, 60000).getValue();
	}

	public Boolean getHystixThreadInteruptTimeOutAVDLS() {
		return dynamicPropertyFactory.getBooleanProperty(HYSTRIX_THREAD_INTERRUPT_ON_TIMEOUT_AVDLS, false).getValue();
	}

	public Boolean getHystixFallbackEnabledAVDLS() {
		return dynamicPropertyFactory.getBooleanProperty(HYSTRIX_FALLBACK_ENABLED_AVDLS, false).getValue();
	}

	public String getHystixKeyNameAVDLS() {
		return dynamicPropertyFactory.getStringProperty(HYSTRIX_KEY_NAME_AVDLS, "AvatarLookupService").getValue();
	}
	
}
