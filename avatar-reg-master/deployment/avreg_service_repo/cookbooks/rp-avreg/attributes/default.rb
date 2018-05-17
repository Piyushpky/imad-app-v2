include_attribute "gen2_base"
include_attribute "nginxclojure"
default['rp-avreg']['service-name']="rp-avreg"
default['rp-avreg']['location'] ="/avatar/v1/entity_validation/"
default['rp-avreg']['location_withoutauth']="/avatar/v1/"
default['rp-avreg']['proxypass'] ="http://52.11.215.244:8080"
default['rp-avreg']['credentials']= nil

default['rp-avreg']['workerprocess'] ="1"
default['rp-avreg']['master_process']="off"
default['rp-avreg']['listen_port'] ="8080"
default['rp-avreg']['user']="ec2-user"
default['rp-avreg']['worker_connections']= "1024"
default['rp-avreg']['keepalive_timeout ']="65"
default['rp-avreg']['logpath'] ="access.log"
default['rp-avreg']['jvm_path'] ="/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.85-2.6.1.2.el7_1.x86_64/jre/lib/amd64/server/libjvm.so"
default['rp-avreg']['jvm_classpath'] ="jars/*"
default['rp-avreg']['min_heap'] ="256m"
default['rp-avreg']['max_heap'] ="512m"
default['rp-avreg']['jmxremote_port']="11300"
default['rp-avreg']['path']="#{node['nginxclojure']['directory']}/nginx-clojure-0.4.3"
default['rp-avreg']['proxyheader']="www-authenticate 'Basic realm=\"svc_avatar\"'"
default['rp-avreg']['auth_basic']="svc_avatar";
default['rp-avreg']['data_bag_key']="/opt/data_bag_key";

#Specifies the node templates.
default['zabbix']['agent']['templates']         = ['WPP Template OS Linux']

#Specify the hostgroup for your service nodes.The syntax is [WPP: <HOST_GROUP_NAME>]
default['zabbix']['agent']['hostgroups']        = ["Avatar-Reg-RP"]

#Enable jmx if required.Default value is true
default['zabbix']['agent']['jmx'] = nil

#enable splunk attributes
default['splunk']['monitors'] = [ 
  {
    'path' => "#{node['nginxclojure']['directory']}/nginx-clojure-0.4.3/logs/",
    'sourcetype' => 'avatar_reg_rp'
  }
]

override['zabbix']['service_name']= "rp-avreg"