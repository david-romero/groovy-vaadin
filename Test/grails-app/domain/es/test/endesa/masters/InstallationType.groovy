package es.test.endesa.masters


/**
 * Posibles tipos de instalaciones de centros de recarga: En vía pública o en un parking
 * pueden ser posibles valores a almacenar en la tabla.
 * 
 * @author Aleph Sistemas de Información
 *
 */
class InstallationType implements Serializable {

	String name
	/** En los códigos de las instalaciones, será este acrónimo el que se usará */
	String acronym
	/** Icono para este tipo de instalacion a usar en los mapas*/
	String mapsIconName
    String iconName
	
    static constraints = {
		name(nullable: false, blank: false)
		acronym(nullable: false, blank: false)
    }
	
	String toString(){
		return "${name}"
	}
}
