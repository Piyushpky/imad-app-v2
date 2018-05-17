#
# Cookbook Name:: rp-avregAvreg
# Recipe:: applyEnvironment
#
# Copyright (c) 2015 The Authors, All Rights Reserved.

#include_recipe 'nginxclojure::stopNginxclojure'

if "#{node['environment_name']}" != "dev"
	include_recipe 'zabbixagent::agent_autoreg'
	include_recipe 'users::default'
end

include_recipe 'splunkforwarder::register'
if "#{node['environment_name']}" == "dev" || "#{node['environment_name']}" == "pie"
	nginxconfig_details = data_bag_item("#{node['rp-avreg']['service-name']}", "#{node['environment_name']}Properties")
else
	nginxconfig_details = data_bag_item("#{node['rp-avreg']['service-name']}", "#{node['environment_name']}Properties",IO.read("#{node['rp-avreg']['data_bag_key']}"))
end

node.override['rp-avreg']['location'] ="#{nginxconfig_details['location']}"
node.override['rp-avreg']['location_withoutauth'] ="#{nginxconfig_details['location_withoutauth']}"
node.override['rp-avreg']['proxypass'] ="#{nginxconfig_details['proxypass']}"
node.override['rp-avreg']['credentials']=nginxconfig_details['credentials']

node.override['rp-avreg']['workerprocess'] ="#{nginxconfig_details['workerprocess']}"
node.override['rp-avreg']['master_process']="#{nginxconfig_details['master_process']}"
node.override['rp-avreg']['listen_port'] ="#{nginxconfig_details['listen_port']}"
node.override['rp-avreg']['user']="#{nginxconfig_details['user']}"
node.override['rp-avreg']['worker_connections']= "#{nginxconfig_details['worker_connections']}"
node.override['rp-avreg']['keepalive_timeout ']="#{nginxconfig_details['keepalive_timeout ']}"
node.override['rp-avreg']['logpath'] ="#{nginxconfig_details['logpath']}"
node.override['rp-avreg']['jvm_path'] ="#{nginxconfig_details['jvm_path'] }"
node.override['rp-avreg']['jvm_classpath'] ="#{nginxconfig_details['jvm_classpath']}"
node.override['rp-avreg']['min_heap'] ="#{nginxconfig_details['min_heap']}"
node.override['rp-avreg']['max_heap'] ="#{nginxconfig_details['max_heap']}"
node.override['rp-avreg']['jmxremote_port'] ="#{nginxconfig_details['jmxremote_port']}"
node.override['rp-avreg']['proxyheader']="#{nginxconfig_details['proxyheader']}"
node.override['rp-avreg']['auth_basic']="#{nginxconfig_details['auth_basic']}"
#include_recipe 'nginxclojure::installNginxclojure'
include_recipe 'rp-avreg::createAuthentication'


template "nginx.conf" do
  path "#{node['nginxclojure']['directory']}/nginx-clojure-0.4.3/conf/nginx.conf"
  source "nginx.conf.erb"
  owner "root"
  group "root"
  mode "0644"
end


include_recipe 'nginxclojure::startNginxclojure'
