package es.test.endesa.test.security


/**
 * Roles disponibles para los usuarios. Existirán dos roles básicos: ROLE_ADMIN, y ROLE_CUSTOMER.
 * Tendremos después tantos roles diferentes como propietarios de instalación existan
 * @author Aleph Sistemas de Información
 *
 */
class Role {

	String authority

	static mapping = {
		cache true
        table 'test_role'
	}

	static constraints = {
		authority blank: false, unique: true
	}
    
    String toString() {
        return authority
    }
}
