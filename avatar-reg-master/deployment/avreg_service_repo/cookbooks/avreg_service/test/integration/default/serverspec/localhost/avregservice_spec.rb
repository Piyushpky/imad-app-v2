require 'spec_helper'

describe user('webadmin') do
  it { should exist }
end

describe user('webadmin') do
  it { should belong_to_group 'root' }
end

describe port(8080) do
  it { should be_listening }
end

describe file('/opt/wpp/gen2/tomcat/bin/setenv.sh') do
  it { should exist }
end

describe command('ls -l /opt/wpp/gen2/tomcat/webapps/onramp*.war | wc -l') do
  its(:stdout) { should eq "1\n" }
end