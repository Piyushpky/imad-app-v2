#
# Cookbook Name:: avdls_dynamodb
# Recipe:: default
#
# Copyright (C) 2014 Timehop
#

include_recipe 'avdls_dynamodb::InsertItemRegistrationDomain'
include_recipe 'avdls_dynamodb::InsertItemSsnReferenceUrl'


