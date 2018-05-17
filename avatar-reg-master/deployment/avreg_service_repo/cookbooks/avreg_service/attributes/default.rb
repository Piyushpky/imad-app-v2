include_attribute "gen2_base"
include_attribute "tomcat"
include_attribute "splunkforwarder"

default['avreg_service']['war_prefix'] = "avatar"
default['avreg_service']['artifact']['name'] = "avatar"

default['avreg_service']['keystore']['cert_directory'] = "#{node['gen2_base']['localdir']['base']}/certs"
default['avreg_service']['keystore']['location'] = "#{node['gen2_base']['localdir']['base']}/certs/keystore.jks"

#Specifies the node templates.
default['zabbix']['agent']['templates']         = ['WPP-meta-avreg']
#Specify the hostgroup for your service nodes.The syntax is [<HOST_GROUP_NAME>]eg., ["Avatar-Reg"]
default['zabbix']['agent']['hostgroups']            =   ["Avatar-Reg"]
#Enable jmx if required.Default value is true
default['zabbix']['agent']['jmx'] = true

default['zabbix']['service_name']= "avreg"

default['splunk']['monitors'] = [
  {
    'path' => "#{node['tomcat']['catalina_base']}/logs/",
    'sourcetype' => 'avreg'
  }
]

#attributes used in server.xml
default['avreg_service']['access_log']['prefix'] = "localhost_access_log."
default['avreg_service']['access_log']['suffix'] = ".txt"
default['avreg_service']['access_log']['pattern'] = "%h %l %u %t %r %s %b %T %I %{x-hp-cloud-id}i %{Internal-Error-Code}o %{User-Agent}i"