include_recipe 'tomcat::stopTomcat'
#zabbix agent registration for dev nodes is disabled
if "#{node['environment_name']}" != "dev"
	include_recipe 'zabbixagent::agent_autoreg'
	include_recipe 'users::default'
end
include_recipe 'splunkforwarder::register'


#create setenv.sh
#config_details = search("avreg_service", "env_name:#{node['environment_name']}").first
config_details = data_bag_item("avreg_service", "#{node['environment_name']}Properties")

node.override['avreg_service']['java']['opts']="#{config_details['java_opts']}"
node.override['avreg_service']['catalina']['opts']="#{config_details['catalina_opts']}"
node.override['avreg_service']['environment']="#{config_details['ENVIRONMENT']}"


template "#{node['tomcat']['catalina_base']}/bin/setenv.sh" do
  source 'setenv.sh.erb'
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0754'
  #notifies :start, "service[tomcat]", :delayed
end

if "#{node['environment_name']}" == "dev" || "#{node['environment_name']}" == "pie" || "#{node['environment_name']}" == "piepod1"
	config_details = data_bag_item("avreg_service", "db_#{node['environment_name']}_properties")
else
	config_details = data_bag_item("avreg_service", "db_#{node['environment_name']}_properties",IO.read("#{node['secret-file']}"))
end 

#config_details = data_bag_item("avreg_service", "db_#{node['environment_name']}_properties")
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

cookbook_file "#{node['avreg_service']['keystore']['cert_directory']}/java_keystore.jks" do
  source "certs/postcard_keystore_#{node['environment_name']}.jks"
  mode "0764"
  owner "root"
  group "root"
end

cookbook_file "#{node['avreg_service']['keystore']['location']}" do
  source "certs/keystore_#{node['environment_name']}.jks"
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0754'
end

include_recipe 'tomcat::startTomcat'

