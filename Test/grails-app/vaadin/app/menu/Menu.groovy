package app.menu

import app.calendar.CalendarView
import app.criterios.CriteriosView
import app.dashboard.DashBoardView
import app.drag.DragView
import app.listar.ListarView
import app.perfil.PerfilView
import app.reports.ReportView
import app.spreadsheet.ExcelView
import app.spreadsheet.SpreadSheet2View
import app.spreadsheet.SpreadSheetView

import com.google.common.eventbus.Subscribe
import com.vaadin.event.dd.DragAndDropEvent
import com.vaadin.event.dd.DropHandler
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion
import com.vaadin.grails.navigator.Views
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.server.*
import com.vaadin.server.Sizeable.Unit
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.DragAndDropWrapper
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.Table
import com.vaadin.ui.AbstractSelect.AcceptItem
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.MenuBar.Command
import com.vaadin.ui.MenuBar.MenuItem
import com.vaadin.ui.themes.*

import es.test.endesa.test.security.User

class Menu extends CustomComponent {

    public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private Label notificationsBadge;
    private Label reportsBadge;
    private MenuItem settingsItem;
	
	private Navigator navigator

    public Menu() {
        addStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        // There's only one DashboardMenu per UI so this doesn't need to be
        // unregistered from the UI-scoped DashboardEventBus.
        //DashboardEventBus.register(this);

        setCompositionRoot(buildContent());
    }

	
	
    public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}



	private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        //menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("QuickTickets <strong>Dashboard</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        final User user = getCurrentUser();
        settingsItem = settings.addItem("", new ThemeResource(
                "img/profile-pic-300px.jpg"), null);
        updateUserName();
        settingsItem.addItem("Edit Profile", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                //ProfilePreferencesWindow.open(user, false);
            }
        });
        settingsItem.addItem("Preferences", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                //ProfilePreferencesWindow.open(user, true);
            }
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                //DashboardEventBus.post(new UserLoggedOutEvent());
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);

        for (ViewType view : ViewType.values()) {
            Component menuItemComponent = new ValoMenuItemButton(view);

            if (view == ViewType.REPORTS) {
                // Add drop target to reports button
                DragAndDropWrapper reports = new DragAndDropWrapper(
                       menuItemComponent);
                //reportsupdate.setDragStartMode(DragStartMode.NONE);
                reports.setDropHandler(new DropHandler() {

                    @Override
                 public void drop(final DragAndDropEvent event) {
                     Views.enter(ReportView)
                        Table table = (Table) event.getTransferable()
                                .getSourceComponent();
                       /*DashboardEventBus.post(new TransactionReportEvent(
                               (Collection<Transaction>) table.getValue()));*/
                    }

                    @Override
                    public AcceptCriterion getAcceptCriterion() {
                        return AcceptItem.ALL;
                    }

                });
                menuItemComponent = reports;
            }

            if (view == ViewType.DASHBOARD) {
                notificationsBadge = new Label();
                notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
                menuItemComponent = buildBadgeWrapper(menuItemComponent,
                        notificationsBadge);
            }
            if (view == ViewType.REPORTS) {
                reportsBadge = new Label();
                reportsBadge.setId(REPORTS_BADGE_ID);
                menuItemComponent = buildBadgeWrapper(menuItemComponent,
                        reportsBadge);
            }

            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;

    }

    private Component buildBadgeWrapper(final Component menuItemButton,
            final Component badgeLabel) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        //dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        dashboardWrapper.setWidth(100.0f, Unit.PERCENTAGE);
        //badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(false);
        dashboardWrapper.addComponent(badgeLabel);
        return dashboardWrapper;
    }

    @Override
    public void attach() {
        super.attach();
        updateNotificationsCount();
    }



    @Subscribe
    public void updateNotificationsCount() {
//        notificationsBadge.setValue(String.valueOf(1));
//        notificationsBadge.setVisible(1 > 0);
    }

    @Subscribe
    public void updateReportsCount() {
//        reportsBadge.setValue(String.valueOf(2));
//        reportsBadge.setVisible(2 > 0);
    }

    @Subscribe
    public void updateUserName() {
        User user = getCurrentUser();
        settingsItem.setText(user.username);
    }

    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final ViewType view;

        public ValoMenuItemButton(ViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(view.getViewName().substring(0, 1).toUpperCase()
                    + view.getViewName().substring(1));
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
					navigator.navigateTo(view.getViewName())
                }
            });

        }

      
    }
	
	enum ViewType {
		
			DASHBOARD ( "Dashboard",DashBoardView, com.vaadin.server.FontAwesome.HOME ,true ),
			CALENDAR ( "Calendar" , CalendarView, com.vaadin.server.FontAwesome.CALENDAR_O , true ),
			REPORTS ( "Reports" , ReportView , com.vaadin.server.FontAwesome.FILE_TEXT_O , true ),
			LISTAR ( "Listar" , ListarView , com.vaadin.server.FontAwesome.FILE_TEXT_O , true ),
			EXCEL ( "SpreadSheet" , ExcelView , com.vaadin.server.FontAwesome.FILE_EXCEL_O , true ),
			EXCEL2 ( "SpreadSheet2" , SpreadSheetView , com.vaadin.server.FontAwesome.FILE_EXCEL_O , true ),
			EXCEL3 ( "SpreadSheet3" , SpreadSheet2View , com.vaadin.server.FontAwesome.FILE_EXCEL_O , true ),
			DRAG ( "Drag" , DragView , com.vaadin.server.FontAwesome.TIMES , true  ),
			PERFIL ( "Perfil" , PerfilView , com.vaadin.server.FontAwesome.TIMES , true  ),
			CRITERIOS ("Criterios", CriteriosView, com.vaadin.server.FontAwesome.TIMES, true )
		
			String viewName
			Class<? extends View> viewClass
			Resource icon
			boolean stateful
		
			private ViewType(String viewName,
					 Class<? extends View> viewClass,Resource icon,
					boolean stateful) {
				this.viewName = viewName
				this.viewClass = viewClass
				this.icon = icon
				this.stateful = stateful
			}
		
			static ViewType getByViewName(String viewName) {
				return values().find { it.viewName == viewName }
			}
		
		}

}
