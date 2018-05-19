include_recipe 'tomcat::stopTomcat'

#include_recipe 'zabbixagent::agent_autoreg'
#zabbix agent registration for dev nodes is disabled
if "#{node['environment_name']}" != "dev"
    include_recipe 'zabbixagent::agent_autoreg'
    include_recipe 'users::default'
end

#create setenv.sh
config_details = data_bag_item("avdls_service", "#{node['environment_name']}Properties")


node.override['avdls_service']['java']['opts']="#{config_details['java_opts']}"
node.override['avdls_service']['catalina']['opts']="#{config_details['catalina_opts']}"
if "#{node['environment_name']}" == "dev"
	
	node.override['avdls_service']['environment']=" "
else
	node.override['avdls_service']['environment']="#{config_details['ENVIRONMENT']}"
end

template "#{node['tomcat']['catalina_base']}/bin/setenv.sh" do
  source 'setenv.sh.erb'
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0754'
end
if "#{node['environment_name']}" == "dev" || "#{node['environment_name']}" == "pie"|| "#{node['environment_name']}" == "piepod1"
	config_details = data_bag_item("avdls_service", "db_#{node['environment_name']}_properties")
else
	config_details = data_bag_item("avdls_service", "db_#{node['environment_name']}_properties",IO.read("#{node['secret-file']}"))
end

node.override['appconfig']['username']="#{config_details['config_db_user']}"
node.override['appconfig']['password']="#{config_details['config_db_password']}"
node.override['appconfig']['driverclass']="#{config_details['config_db_driver']}"
node.override['appconfig']['jdbcurl']="#{config_details['config_db_jdbcurl']}"
node.override['appconfig']['pollfrequency']="#{config_details['config_pollfrequency']}"

template "#{node['gen2_base']['localdir']['config']}/config.bootstrap.properties" do
  source 'config.bootstrap.properties.erb'
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0754'
end
cookbook_file "#{node['avdls_service']['keystore']['location']}" do
  source "certs/keystore_#{node['environment_name']}.jks"
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0754'
end

include_recipe 'tomcat::startTomcat'

include_recipe 'splunkforwarder::register'
