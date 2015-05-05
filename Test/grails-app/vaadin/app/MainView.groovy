package app

import app.calendar.CalendarView
import app.dashboard.DashBoardView
import app.menu.Menu
import app.reports.ReportView
import app.spreadsheet.ExcelView
import app.spreadsheet.SpreadSheetView;

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout

@VaadinView(path="main")
class MainView extends HorizontalLayout implements View{

	@Override
	def void enter(ViewChangeEvent event) {
		createView()
	}
	
	def createView(){
		setSizeFull()
		addStyleName("mainview")

		def menu = new Menu()
		
		def content = new CssLayout()
		content.addStyleName("view-content")
		content.setSizeFull()
		
		def navigator = new Navigator(MyUI.getCurrent(),content)
		navigator.addView("Dashboard", DashBoardView.class)
		navigator.addView("Calendar", CalendarView.class)
		navigator.addView("Reports", ReportView.class)
		navigator.addView("SpreadSheet", ExcelView.class)
		navigator.addView("SpreadSheet2", SpreadSheetView.class)
		menu.setNavigator(navigator)
		
		addComponent(menu)
		
		addComponent(content)
		setExpandRatio(content, 1.0f)
	}
}
