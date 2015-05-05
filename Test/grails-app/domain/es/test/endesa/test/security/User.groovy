package es.test.endesa.test.security


/**
 * Clase básica de Spring Security para el control de sesiones y usuarios
 * 
 * @author Aleph Sistemas de Información
 *
 */
class User {


	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    String docRepoUser
    String docRepoPassword


	static constraints = {
		username blank: false, unique: true
		password blank: false
        docRepoUser nullable: true, blank: true
        docRepoPassword nullable: true, blank: true
	}

	static mapping = {
		password column: '`password`'
        table 'test_user'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
	}

	def beforeUpdate() {
	}


}
