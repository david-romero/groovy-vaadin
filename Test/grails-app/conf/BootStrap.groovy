import org.codehaus.groovy.grails.web.json.JSONObject
import es.test.endesa.test.security.User
import grails.converters.JSON

class BootStrap {

    def springSecurityService
    
    def init = { servletContext ->
        // Muy importante para las igualdades a NULL de objetos JSON
        JSONObject.NULL.metaClass.asBoolean = {-> false}
        // Formato de fechas
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy-MM-dd'T'HH:mm:ssZ")
         }
		
		if (grails.util.Environment.current == grails.util.Environment.DEVELOPMENT) { 
			User raymond = new User(username: 'Raymond',password:'$2a$10$UQtFl2dNJ6LsXjQjUDIMae6gwZDE0C4GaQkqGTlcmSgGzOKFcHQWu') 
			raymond.save(failOnError: true)
			
			User pug = new User(username: 'Pug',password:'$2a$10$UQtFl2dNJ6LsXjQjUDIMae6gwZDE0C4GaQkqGTlcmSgGzOKFcHQWu') 
			pug.save(failOnError: true)
		}
    }
    
    def destroy = {
    }
}
