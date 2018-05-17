#
# Cookbook Name:: avreg_db
# Recipe:: deployAVRegDB.rb
#
# Copyright (C) 2016 Vysshnavi
#
# All rights reserved - Do Not Redistribute
#

node.set['db']['service-name']="avreg_db"
#node.set['db']['service-url'] = '{service_url}'

#create project directories
include_recipe 'gen2_base::createProjectDirectories'
include_recipe 'gen2_database::deployDatabase'
