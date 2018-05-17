require 'spec_helper'

describe command('ps -ef | grep tomcat | grep -v grep | wc -l') do
  its(:stdout) { should eq "1\n" }
end
