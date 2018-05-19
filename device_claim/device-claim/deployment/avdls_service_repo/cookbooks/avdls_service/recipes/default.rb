#
# Cookbook Name:: avdls_service
# Recipe:: default
#
# Copyright (C) 2015 Nandan
#
# All rights reserved - Do Not Redistribute
#
#create project directories
include_recipe 'gen2_base::createProjectDirectories'

#install java
#include_recipe 'java_jdk::installOpenJdk7'


#install tomcat
include_recipe 'tomcat::installTomcat'

#Stop tomcat if running
#include_recipe 'tomcat::stopTomcat'

include_recipe 'avdls_service::deployAVATAR-LOOKUP-SERVICE'

#start tomcat
#include_recipe 'tomcat::startTomcat'
