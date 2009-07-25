import java.io.File
import sbt._

class QuotidianProject(info:ProjectInfo) extends DefaultWebProject(info) {
	// locate the Home directory
	val userHome = system[File]("user.home")

	// Dependencies for compiling
	val velocity = "org.apache.velocity" % "velocity" % "1.6.1"
	val commonsCollections = "commons-collections" % "commons-collections" % "3.2.1"
	val commonsLang = "commons-lang" % "commons-lang" % "2.4"

	// Dependencies for testing
	val junit = "junit" % "junit" % "4.5" % "test->default"
	val specs = "org.scala-tools.testing" % "specs" % "1.5.0" % "test->default"

	// App Engine paths
	val gaeHome = Path.fromFile(userHome.value + "/Documents/src/gae/") / "appengine-java-sdk-1.2.2"
	val gaeSharedJars = gaeHome / "lib" / "shared" * "*.jar"

	// override looking for jars in ./lib
	override def dependencyPath = "src" / "main" / "lib"
	// override output of war to target/webapp
	override def temporaryWarPath = outputPath / "war"
	// compile with App Engine jars
	override def compileClasspath = super.compileClasspath +++ gaeSharedJars 
	// add App Engine jars to console classpath
	override def consoleClasspath = super.consoleClasspath +++ gaeSharedJars
	// compile tests with App Engine jars
	override def testClasspath = super.testClasspath +++ gaeSharedJars
	// override path to managed dependency cache
	override def managedDependencyPath = "project" / "lib_managed"
}
