#!/usr/bin/env ruby

BASEDIR = '/Users/jdcasey/workspace/freemakers/programming/freeki/freeki-java-server'
AS7_HOME = '/Users/jdcasey/apps/as7/current'
CONTROLLER_HOST_PORT = 'localhost:10999'
WAR = 'freeki-data.war'

cmds =<<-EOC
  undeploy #{WAR}
  deploy #{BASEDIR}/target/#{WAR}
EOC

path = '/tmp/redeploy.commands'
File.delete( path ) if File.exists?(path)
File.open(path, 'w+'){|f|
  f.puts(cmds)
}

Dir.chdir(AS7_HOME) {
  system( "bin/jboss-cli.sh --controller=#{CONTROLLER_HOST_PORT} --connect --file=#{path}" )
  ret = $?
  puts "Exit status: #{ret.exitstatus}"
  exit ret.exitstatus
}

