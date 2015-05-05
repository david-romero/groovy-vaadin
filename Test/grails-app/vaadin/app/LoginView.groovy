package app

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.grails.navigator.VaadinView
import com.vaadin.grails.navigator.Views
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Responsive
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.themes.ValoTheme

@VaadinView(path = "login")
class LoginView  extends VerticalLayout implements View{

	def buildLoginForm() {
		def loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");

		loginPanel.addComponent(buildLabels());
		loginPanel.addComponent(buildFields());
		loginPanel.addComponent(new CheckBox("Remember me", true));
		return loginPanel;
	}

	def buildFields() {
		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addStyleName("fields");

		TextField username = new TextField("Username");
		username.setIcon(FontAwesome.USER);
		username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		PasswordField password = new PasswordField("Password");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		Button signin = new Button("Sign In");
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		fields.addComponents(username, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		signin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				Views.enter(MainView)
			}
		});
		return fields;
	}

	def buildLabels() {
		CssLayout labels = new CssLayout()
		labels.addStyleName("labels")

		Label welcome = new Label("Welcome")
		welcome.setSizeUndefined()
		welcome.addStyleName(ValoTheme.LABEL_H4)
		welcome.addStyleName(ValoTheme.LABEL_COLORED)
		labels.addComponent(welcome)

		Label title = new Label("QuickTickets Dashboard")
		title.setSizeUndefined()
		title.addStyleName(ValoTheme.LABEL_H3)
		title.addStyleName(ValoTheme.LABEL_LIGHT)
		labels.addComponent(title)
		return labels
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();

		Component loginForm = buildLoginForm();
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}
 
}
