package es.test.common

import javax.servlet.ServletContext
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.hibernate.SessionFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * Permite acceder a recursos compartidos de la aplicación Grails
 * 
 * @author Aleph Sistemas de Información
 *
 */
@Singleton
class ApplicationContextHolder implements ApplicationContextAware {
    
    private ApplicationContext ctx
    
    void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext
    }

    static ApplicationContext getApplicationContext() {
        getInstance().ctx
    }
    
    static Object getBean(String name) {
        getApplicationContext().getBean(name)
     }
      
     static GrailsApplication getGrailsApplication() {
        getBean('grailsApplication')
     }
      
     static ConfigObject getConfig() {
        getGrailsApplication().config
     }
      
     static ServletContext getServletContext() {
        getBean('servletContext')
     }
      
     static GrailsPluginManager getPluginManager() {
        getBean('pluginManager')
     }
     
     static def getSessionFactory() {
         getBean('sessionFactory')
     }
     
     static Class getDomainObject(String name) {
         return getGrailsApplication().getClassForName(name)
     }
}
