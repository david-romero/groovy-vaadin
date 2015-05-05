package es.test.endesa.test

import es.test.endesa.test.security.User
import grails.transaction.Transactional

@Transactional
class UserService {

	@Transactional
    def getListOfUsers() {
		return es.test.endesa.test.security.User.findAll()
    }
	@Transactional
	def getAuthorities(User user){
		return user.getAuthorities().collectMany { it.authority }
	}
}
