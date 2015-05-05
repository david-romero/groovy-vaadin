grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0).
                               // Tomcat en Endesa es versión 6.0, con lo que debe estar en 2.5
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 1024, minMemory: 256, debug: false, maxPerm: 512, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, forkReserve:true],
    // configure settings for the run-war JVM
    war: [maxMemory: 1024, minMemory: 256, debug: false, maxPerm: 512, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "info" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://repo.spring.io/milestone/"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repo.grails.org/grails/libs-releases/"
        mavenRepo "http://m2repo.spockframework.org/ext/"
        mavenRepo "http://m2repo.spockframework.org/snapshots/"
        mavenRepo "http://maven.ala.org.au/repository/"
		// Repository for the vaadin provided addons
		mavenRepo 'http://maven.vaadin.com/vaadin-addons'
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

		compile "org.ow2.asm:asm:5.0.3"
		
        // runtime 'mysql:mysql-connector-java:5.1.22'
        //runtime 'org.postgresql:postgresql:9.3-1100-jdbc41'      
        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        test "org.grails:grails-datastore-test-support:1.0-grails-2.4"
        
        compile "org.apache.chemistry.opencmis:chemistry-opencmis-client-impl:0.12.0"
        compile 'commons-io:commons-io:2.4'
		
		//add-ons
		compile "com.vaadin.addon:vaadin-spreadsheet:1.0.2"
		compile "com.vaadin.addon:vaadin-charts:2.0.0"
		
		compile "com.itextpdf:itextpdf:5.3.4"
		compile "org.apache.xmlgraphics:batik-transcoder:1.7"
		compile "org.apache.xmlgraphics:batik-codec:1.7"
		compile "org.apache.xmlgraphics:batik-swing:1.7"
		compile "org.apache.xmlgraphics:batik-dom:1.7"
		compile "org.apache.xmlgraphics:batik-bridge:1.7"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.55"

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ":asset-pipeline:2.1.1"

        
        // Específicos para este proyecto
        compile ":webxml:1.4.1"
		compile ":vaadin:7.4.3"


        compile ":joda-time:1.5"
		
		// plugins needed at runtime but not for compilation
		runtime //":hibernate:3.6.10.16" // or ":hibernate4:4.3.1.1"
		runtime ":hibernate4:4.3.5.5"
		runtime ":database-migration:1.4.0"
		

        
    }
	
	grails.enable.native2ascii = false
}
