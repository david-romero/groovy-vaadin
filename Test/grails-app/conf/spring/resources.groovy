// Place your Spring DSL code here
import es.test.common.ApplicationContextHolder

beans = {
    // Muy importante esta llamada, este holder será utilizado por los motores de script para poder acceder
    // a base de datos, por ejemplo
    // Se inyectan la instancia necesaria para tener acceso al singleton con datos de la aplicación
    applicationContextHolder(ApplicationContextHolder) { bean ->
        bean.factoryMethod = 'getInstance'
    }
}
