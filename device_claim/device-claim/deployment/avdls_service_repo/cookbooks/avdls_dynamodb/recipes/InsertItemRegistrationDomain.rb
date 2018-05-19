
dydbbconfig_details = data_bag_item("#{node['avdls_dynamodb']['name']}", "dydb_#{node['environment_name']}_config_properties")


node.override['avdls_dynamodb']['region'] ="#{dydbbconfig_details['dynamodb.region']}"
node.override['avdls_dynamodb']['accesskey'] ="#{dydbbconfig_details['dynamodb.accessKeyId']}"
node.override['avdls_dynamodb']['secretkey'] ="#{dydbbconfig_details['dynamodb.secretKey']}"
node.override['avdls_dynamodb']['tablename1'] ="registration_domain_#{dydbbconfig_details['environment']}"
node.override['avdls_dynamodb']['filename1'] ="dydb_#{node['avdls_dynamodb']['tablename1']}_data_properties"
dydbbconfig_items = data_bag_item("#{node['avdls_dynamodb']['name']}", "#{node['avdls_dynamodb']['filename1']}")


node.override['avdls_dynamodb']['insert_items1'] ="#{dydbbconfig_items['insert_items']}"
node.override['avdls_dynamodb']['delete_items1'] ="#{dydbbconfig_items['delete_items']}"
node.override['avdls_dynamodb']['update_items1'] ="#{dydbbconfig_items['update_items']}"


template "InsertItemRegistrationDomain.rb" do
  path "/home/ec2-user/InsertItemRegistrationDomain.rb"
  source "InsertItemRegistrationDomain.rb.erb"
  owner "root"
  group "root"
  mode "0644"
end

chef_gem "aws-sdk" do
  action [:install,:upgrade]
  version '2.7.1'
end

ruby_block 'run_rubyscript_insert_tableitems' do
  block do
    Chef::Config.from_file("/home/ec2-user/InsertItemRegistrationDomain.rb")
  end
  action :run
end


