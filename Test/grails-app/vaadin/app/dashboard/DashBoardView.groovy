/**
 * 
 */
package app.dashboard

import app.components.TopSixTheatersChart
import app.components.TopTenMoviesTable

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.grails.navigator.VaadinView
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Responsive
import com.vaadin.server.Sizeable.Unit
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.Notification
import com.vaadin.ui.Panel
import com.vaadin.ui.TextArea
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.MenuBar.Command
import com.vaadin.ui.MenuBar.MenuItem
import com.vaadin.ui.Notification.Type
import com.vaadin.ui.themes.ValoTheme

@VaadinView(path="dashboard")
/**
 * @author David
 *
 */
class DashBoardView extends Panel implements View{

	@Override
	void enter(ViewChangeEvent event) {
		addStyleName(ValoTheme.PANEL_BORDERLESS)
		setSizeFull()


		root = new VerticalLayout()
		root.setSizeFull()
		root.setMargin(true)
		root.addStyleName("dashboard-view")
		setContent(root)
		Responsive.makeResponsive(root)

		root.addComponent(buildHeader())

		root.addComponent(buildSparklines())

		Component content = buildContent()
		root.addComponent(content)
		root.setExpandRatio(content, 1)


		//notificationsButton.updateNotificationsCount()
	}
	
	public static final String EDIT_ID = "dashboard-edit"
	public static final String TITLE_ID = "dashboard-title"

	Label titleLabel
	NotificationsButton notificationsButton
	CssLayout dashboardPanels
	VerticalLayout root
	Window notificationsWindow

	def DashboardView() {}

	private Component buildSparklines() {
		CssLayout sparks = new CssLayout();
		sparks.addStyleName("sparks");
		sparks.setWidth("100%");
		Responsive.makeResponsive(sparks);

		return sparks
	}

	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);

		titleLabel = new Label("Dashboard");
		titleLabel.setId(TITLE_ID);
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(titleLabel);

		notificationsButton = buildNotificationsButton();
		Component edit = buildEditButton();
		HorizontalLayout tools = new HorizontalLayout(notificationsButton, edit);
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	private NotificationsButton buildNotificationsButton() {
		NotificationsButton result = new NotificationsButton();
		result.addClickListener(new Button.ClickListener(){
			void buttonClick(com.vaadin.ui.Button$ClickEvent event){
				openNotificationsPopup()
			}
		});
		return result;
	}

	private Component buildEditButton() {
		Button result = new Button();
		result.setId(EDIT_ID);
		result.setIcon(FontAwesome.EDIT);
		result.addStyleName("icon-edit");
		result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		result.setDescription("Edit Dashboard");

		return result;
	}

	private Component buildContent() {
		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);

		dashboardPanels.addComponent(buildTopGrossingMovies());
		dashboardPanels.addComponent(buildNotes());
		dashboardPanels.addComponent(buildTop10TitlesByRevenue());
		dashboardPanels.addComponent(buildPopularMovies());

		return dashboardPanels;
	}

	private Component buildTopGrossingMovies() {
		
		return createContentWrapper(new Label());
	}

	private Component buildNotes() {
		TextArea notes = new TextArea("Notes");
		notes.setValue("Remember to:\n· Zoom in and out in the Sales view\n· Filter the transactions and drag a set of them to the Reports tab\n· Create a new report\n· Change the schedule of the movie theater");
		notes.setSizeFull();
		notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		Component panel = createContentWrapper(notes);
		panel.addStyleName("notes");
		return panel;
	}

	private Component buildTop10TitlesByRevenue() {
		Component contentWrapper = createContentWrapper(new TopTenMoviesTable())
		contentWrapper.addStyleName("top10-revenue")
		return contentWrapper
	}

	private Component buildPopularMovies() {
		return createContentWrapper(new TopSixTheatersChart());
	}

	def createContentWrapper(Component content) {
		final CssLayout slot = new CssLayout();
		slot.setWidth("100%");
		slot.addStyleName("dashboard-panel-slot");

		CssLayout card = new CssLayout();
		card.setWidth("100%");
		card.addStyleName(ValoTheme.LAYOUT_CARD);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("dashboard-panel-toolbar");
		toolbar.setWidth("100%");

		Label caption = new Label(content.getCaption());
		caption.addStyleName(ValoTheme.LABEL_H4);
		caption.addStyleName(ValoTheme.LABEL_COLORED);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		content.setCaption(null);

		MenuBar tools = new MenuBar();
		tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command(){
			void menuSelected(com.vaadin.ui.MenuBar$MenuItem item){
				toggleMaximized(content, true)
			}
		});
		max.setStyleName("icon-only")
		MenuItem root = tools.addItem("", FontAwesome.COG, new Command(){
			void menuSelected(com.vaadin.ui.MenuBar$MenuItem item){
				Notification.show("Not Impemented",Type.HUMANIZED_MESSAGE)
			}
		})
		root.addItem("Configure", new Command(){
			void menuSelected(com.vaadin.ui.MenuBar$MenuItem item){
				Notification.show("Not Impemented",Type.HUMANIZED_MESSAGE)
			}
		})
		root.addSeparator()
		root.addItem("Close", new Command(){
			void menuSelected(com.vaadin.ui.MenuBar$MenuItem item){
				Notification.show("Not Impemented",Type.HUMANIZED_MESSAGE)
			}
		})

		toolbar.addComponents(caption, tools)
		toolbar.setExpandRatio(caption, 1)
		toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT)

		card.addComponents(toolbar, content)
		slot.addComponent(card)
		return slot
	}

	def openNotificationsPopup() {
		VerticalLayout notificationsLayout = new VerticalLayout();
		notificationsLayout.setMargin(true);
		notificationsLayout.setSpacing(true);

		Label title = new Label("Notifications");
		title.addStyleName(ValoTheme.LABEL_H3);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		notificationsLayout.addComponent(title);



		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth("100%");
		Button showAll = new Button("View All Notifications");
		showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		showAll.addStyleName(ValoTheme.BUTTON_SMALL);
		footer.addComponent(showAll);
		footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
		notificationsLayout.addComponent(footer);

		if (notificationsWindow == null) {
			notificationsWindow = new Window();
			notificationsWindow.setWidth(300.0f, Unit.PIXELS);
			notificationsWindow.addStyleName("notifications");
			notificationsWindow.setClosable(false);
			notificationsWindow.setResizable(false);
			notificationsWindow.setDraggable(false);
			notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
			notificationsWindow.setContent(notificationsLayout);
		}

		if (!notificationsWindow.isAttached() ) {
			getUI().addWindow(notificationsWindow);
			notificationsWindow.focus();
		} else {
			notificationsWindow.close();
		}
	}


	private void toggleMaximized(final Component panel, final boolean maximized) {
		for (Iterator<Component> it = root.iterator(); it.hasNext();) {
			it.next().setVisible(!maximized);
		}
		dashboardPanels.setVisible(true);

		for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
			Component c = it.next();
			c.setVisible(!maximized);
		}

		if (maximized) {
			panel.setVisible(true);
			panel.addStyleName("max");
		} else {
			panel.removeStyleName("max");
		}
	}

	public static final class NotificationsButton extends Button {
		private static final String STYLE_UNREAD = "unread";
		public static final String ID = "dashboard-notifications";

		public NotificationsButton() {
			setIcon(FontAwesome.BELL);
			setId(ID);
			addStyleName("notifications");
			addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}

		public void setUnreadCount(final int count) {
			setCaption(String.valueOf(count));

			String description = "Notifications";
			if (count > 0) {
				addStyleName(STYLE_UNREAD);
				description += " (" + count + " unread)";
			} else {
				removeStyleName(STYLE_UNREAD);
			}
			setDescription(description);
		}
	}

}
