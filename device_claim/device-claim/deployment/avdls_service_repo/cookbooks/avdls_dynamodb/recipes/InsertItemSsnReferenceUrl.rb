
dydbbconfig_details = data_bag_item("#{node['avdls_dynamodb']['name']}", "dydb_#{node['environment_name']}_config_properties")


node.override['avdls_dynamodb']['region'] ="#{dydbbconfig_details['dynamodb.region']}"
node.override['avdls_dynamodb']['accesskey'] ="#{dydbbconfig_details['dynamodb.accessKeyId']}"
node.override['avdls_dynamodb']['secretkey'] ="#{dydbbconfig_details['dynamodb.secretKey']}"
node.override['avdls_dynamodb']['tablename2'] ="ssn_reference_url_#{dydbbconfig_details['environment']}"
node.override['avdls_dynamodb']['filename2'] ="dydb_#{node['avdls_dynamodb']['tablename2']}_data_properties"
dydbbconfig_items = data_bag_item("#{node['avdls_dynamodb']['name']}", "#{node['avdls_dynamodb']['filename2']}")


node.override['avdls_dynamodb']['insert_items2'] ="#{dydbbconfig_items['insert_items']}"
node.override['avdls_dynamodb']['delete_items2'] ="#{dydbbconfig_items['delete_items']}"
node.override['avdls_dynamodb']['update_items2'] ="#{dydbbconfig_items['update_items']}"


template "InsertItemSsnReferenceUrl.rb" do
  path "/home/ec2-user/InsertItemSsnReferenceUrl.rb"
  source "InsertItemSsnReferenceUrl.rb.erb"
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
    Chef::Config.from_file("/home/ec2-user/InsertItemSsnReferenceUrl.rb")
  end
  action :run
end


