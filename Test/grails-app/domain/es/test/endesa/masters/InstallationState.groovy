package es.test.endesa.masters



/**
 * Estados en los que puede estar una instalación (Funcionando, En mantenimiento... podrían
 * ser algunos de sus valores)
 * @author david
 *
 */
class InstallationState implements Serializable {
	
	String name
	
    static constraints = {
    }
    
	String toString(){
		return "${name}"
	}
}
