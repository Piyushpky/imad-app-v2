include_attribute "gen2_base"
include_attribute "tomcat"
include_attribute "splunkforwarder"

default['avdls_service']['war_prefix'] = "virtualprinter"
default['avdls_service']['artifact']['name'] = "virtualprinter"

default['avdls_service']['keystore']['cert_directory'] = "#{node['gen2_base']['localdir']['base']}/certs"
default['avdls_service']['keystore']['location'] = "#{node['gen2_base']['localdir']['base']}/certs/keystore.jks"

#Specifies the node templates.
default['zabbix']['agent']['templates']         = ['WPP-meta-avdls']
#Specify the hostgroup for your service nodes.The syntax is [<HOST_GROUP_NAME>]eg., ["Avatar-Reg"]
default['zabbix']['agent']['hostgroups']            =   ["Avatar-Lookup-Service"]
#Enable jmx if required.Default value is true
default['zabbix']['agent']['jmx'] = true

default['zabbix']['service_name']= "avdls"

default['splunk']['monitors'] = [
  {
    'path' => "#{node['tomcat']['catalina_base']}/logs/",
    'sourcetype' => 'avdls'
  }
]

#attributes used in server.xml
default['avdls_service']['access_log']['prefix'] = "localhost_access_log."
default['avdls_service']['access_log']['suffix'] = ".txt"
default['avdls_service']['access_log']['pattern'] = "%h %l %u %t %r %s %b %T %I %{Internal-Error-Code}o"
