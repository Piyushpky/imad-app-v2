#
# Cookbook Name:: rp-avreg
# Recipe:: createAuthentication
#
# Copyright (c) 2015 The Authors, All Rights Reserved.
package "httpd"

#nginxconfig_details = data_bag_item("#{node['rp-avreg']['service-name']}", "#{node['environment_name']}Properties")

ruby_block "read_credentails" do
  	block do
		#credentials = nginxconfig_details['credentials']
		credentials = node['rp-avreg']['credentials']
		print "\n"
		c=1
		print "#{node['nginxclojure']['directory']}"
		credentials.each do |user| 
			print "User : "+user['user-name']+"\tPassword : "+user['password']+"\n"
			if c==1 
				c=0
				print `sudo htpasswd -cb "#{node['nginxclojure']['directory']}/nginx-clojure-0.4.3/.htpasswd" "#{user['user-name']}" "#{user['password']}"`
			else
				print `sudo htpasswd -b  "#{node['nginxclojure']['directory']}/nginx-clojure-0.4.3/.htpasswd" "#{user['user-name']}" "#{user['password']}"`
			end
		end
	end
end

