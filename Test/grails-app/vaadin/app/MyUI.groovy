package app

import com.vaadin.annotations.Theme
import com.vaadin.grails.Grails
import com.vaadin.grails.navigator.Views
import com.vaadin.grails.ui.DefaultUI
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.Responsive
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinSession
import com.vaadin.ui.themes.ValoTheme

import es.test.endesa.test.UserService
import es.test.endesa.test.security.User

@Theme("dashboard")
@VaadinUI(path="/")
/**
 *
 *
 * @author
 */
class MyUI extends DefaultUI {

	def userService
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
		super.init(vaadinRequest)
		userService = Grails.get(UserService)
		
		def authors = userService.listOfUsers
		
		VaadinSession.getCurrent().setAttribute(User.class, authors.get(0))
		
		 setLocale(new Locale("Es","es"))

        Responsive.makeResponsive(this)
        addStyleName(ValoTheme.UI_WITH_MENU)
		
		
        updateContent()
    }
	
	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {
		User user = (User) VaadinSession.getCurrent().getAttribute(
				User.class)
		if (user != null && userService.getAuthorities(user).contains("ROLE_ADMIN") ) {
			// Authenticated user
			removeStyleName("loginview")
			Views.enter(MainView)
		} else {
			addStyleName("loginview")
			Views.enter(LoginView)
		}
	}

	
}
