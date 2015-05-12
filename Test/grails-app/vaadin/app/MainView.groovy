package app

import app.calendar.CalendarView
import app.criterios.CriteriosView
import app.dashboard.DashBoardView
import app.drag.DragView
import app.listar.ListarView
import app.menu.Menu
import app.perfil.PerfilView
import app.reports.ReportView
import app.spreadsheet.ExcelView
import app.spreadsheet.SpreadSheet2View
import app.spreadsheet.SpreadSheetView

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.Page
import com.vaadin.server.Responsive
import com.vaadin.server.Page.BrowserWindowResizeEvent
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout

@VaadinView(path="main")
class MainView extends HorizontalLayout implements View,com.vaadin.server.Page.BrowserWindowResizeListener{

	def browserAncho = Page.getCurrent().getBrowserWindowWidth()
	
	@Override
	def void enter(ViewChangeEvent event) {
		Responsive.makeResponsive(this);
		Page.getCurrent().addBrowserWindowResizeListener(this)
		createView()
	}
	
	def createView(){
		setSizeFull()
		addStyleName("mainview")

		def menu = new Menu()
		
		def content = new CssLayout(){
			protected String getCss(Component c){
				if ( (browserAncho < 768) ||  (browserAncho <= 800 && Page.getCurrent().getBrowserWindowHeight() <= 1280) ){
					return "margin-top: 37px;overflow: scroll;"
				}else{
					return "margin-top: 0px;"
				}
			}
		}
		content.addStyleName("view-content")
		content.setSizeFull()
		
		def navigator = new Navigator(MyUI.getCurrent(),content)
		navigator.addView("Dashboard", DashBoardView.class)
		navigator.addView("Calendar", CalendarView.class)
		navigator.addView("Reports", ReportView.class)
		navigator.addView("SpreadSheet", ExcelView.class)
		navigator.addView("SpreadSheet2", SpreadSheetView.class)
		navigator.addView("SpreadSheet3", SpreadSheet2View.class)
		navigator.addView("Listar", ListarView.class)
		navigator.addView("Drag", DragView.class)
		navigator.addView("Perfil", PerfilView.class)
		navigator.addView("Criterios", CriteriosView.class)
		menu.setNavigator(navigator)
		
		addComponent(menu)
		
		addComponent(content)
		setExpandRatio(content, 1.0f)
	}
	
	@Override
	public void browserWindowResized(BrowserWindowResizeEvent event) {
		browserAncho = event.getWidth()
		attach()
	}
	@Override
	public void detach(){
		Page.getCurrent().removeBrowserWindowResizeListener(this)
	}
}
