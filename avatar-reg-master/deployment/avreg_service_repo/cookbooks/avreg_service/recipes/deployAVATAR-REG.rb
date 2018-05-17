
artifact_url="#{node['service']['artifact_url']}"
	
include_recipe 'tomcat::stopTomcat'

execute "Deleting all wars ... " do
    command "rm -rf #{node['tomcat']['catalina_base']}/webapps/#{node['avreg_service']['war_prefix']}*"
    cwd "#{node['tomcat']['catalina_base']}/webapps/"
    action :run
end

#Copy war to tomcat location
remote_file "#{node['tomcat']['catalina_base']}/webapps/#{node['avreg_service']['artifact']['name']}.war" do
  source "#{artifact_url}"
  mode "0764"
  owner node['tomcat']['user']
  group node['tomcat']['group']
  #notifies :start, "service[tomcat]", :delayed
end

#server.xml changes for access log format
template "#{node['tomcat']['catalina_base']}/conf/server.xml" do
  source 'server.xml.erb'
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0764'
end

#Create directory for keystore
directory node['avreg_service']['keystore']['cert_directory'] do
	Chef::Log.info("creating jks cert directory")
  mode "0764"
  action :create
  recursive true
end

#copy health check json to catalina base
cookbook_file "#{node['tomcat']['catalina_base']}/healthcheckconfig.json" do
  source 'health_check/healthcheckconfig.json'
  owner node['tomcat']['user']
  group node['tomcat']['group']
  mode '0754'
end

include_recipe 'avreg_service::applyEnvironment'
