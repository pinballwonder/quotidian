# Generated by Buildr 1.3.4, change to your liking
require 'buildr/scala'

# Maven 2.0 remote repository
repositories.remote << "http://www.ibiblio.org/maven2/"
# Scala Tools
repositories.remote << 'http://scala-tools.org/repo-releases'
# Google Maven Repository
repositories.remote << 'http://google-maven-repository.googlecode.com/svn/repository/'
# Google Maven Snapshot Repository
repositories.remote << 'http://google-maven-repository.googlecode.com/svn/snapshot-repository/'

# Constants
VERSION_NUMBER = '1.0'
# Dependencies
# Scala Jars should be included from SCALA_HOME environment variable (or macports install)
SCALA = group('scala-library','scala-compiler',:under => 'org.scala-lang', :version => '2.7.5')
# Testing Dependencies
SPECS = 'org.scala-tools.testing:specs:jar:1.5.0'
# Set to use Java 1.5 because Java 6 (1.6) is not working
ENV['JAVA_HOME'] = '/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home'
ENV['USE_FSC'] = 'yes'

desc 'Quotidian: A place to store and organize quotations'
define 'quotidian' do
	# Project config
	gaelibshared = FileList[_(ENV['HOME'],'Documents/src/gae','appengine-java-sdk-1.2.2','lib/shared','**/*.jar')]
	gaelibuser = FileList[_(ENV['HOME'],'Documents/src/gae','appengine-java-sdk-1.2.2','lib/user','**/*.jar')]
	libs = FileList[_('src/main/lib/*.jar')]
	DEPS = [SCALA] << gaelibuser << libs
	CLASSPATH = DEPS + gaelibshared
	TEST_DEPS = [SPECS] << DEPS
	# Build config
	webapp = _('src/main/webapp/*')
	resources = _('target/resources/*')
	lib = _('src/main/lib')
	src = _('target/classes/*')
	war = _('target/war')
	warclasses = _(war, 'WEB-INF/classes')
	warlib = _(war, 'WEB-INF/lib')
	
	# Buildr properties
	project.group = 'quotidian'
	project.version = VERSION_NUMBER
	manifest['Copyright'] = 'Bryan J Swift (C) 2009'
	compile.using(:warnings => 'true', :target => '1.5').with DEPS, CLASSPATH
	test.with TEST_DEPS
	test.using :specs
	package(:war, :id => 'quotidian').with DEPS

	task :explode => [package] do
		mkpath warlib
		sh 'cp -R ' + webapp + ' ' + war
		packages.detect { |pkg| pkg.to_s =~ /war$/ }.tap do |war|
			war.classes.each do |clazz|
				filter.from(clazz).into(warclasses).run
			end
			flattenedDeps = DEPS.flatten
			war.libs.each do |lib|
				flattenedDeps.each do |dep|
					if (dep.to_s == lib.to_s) then
						cp lib.to_s, warlib
					end
				end
			end
		end
	end
end
