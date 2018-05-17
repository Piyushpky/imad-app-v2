#
# Cookbook Name:: avreg_service_config
# Recipe:: deployGatewayServiceConfig.rb
#
# Copyright (C) 2016 Mamatav
#
# All rights reserved - Do Not Redistribute
#

#create project directories
include_recipe 'gen2_base::createProjectDirectories'

include_recipe 'gen2_database::deployAppconfig'
include_recipe 'gen2_database::deployInfraconfig'
