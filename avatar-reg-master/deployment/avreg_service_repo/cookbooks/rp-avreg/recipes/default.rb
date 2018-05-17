#
# Cookbook Name:: rp-avreg
# Recipe:: default
#
# Copyright (c) 2015 The Authors, All Rights Reserved.
#install nginxclojure
include_recipe 'java_jdk::installOpenJdk7'
include_recipe 'nginxclojure::installNginxclojure'
include_recipe 'rp-avreg::applyEnvironment'

