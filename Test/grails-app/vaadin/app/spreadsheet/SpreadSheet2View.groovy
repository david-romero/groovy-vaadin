package app.spreadsheet

import java.text.ParseException
import java.text.SimpleDateFormat

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet

import app.MyUI
import app.calendar.CalendarView.Movie

import com.CustomStreamSource
import com.google.common.collect.Lists
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.vaadin.addon.spreadsheet.Spreadsheet
import com.vaadin.addon.spreadsheet.SpreadsheetComponentFactory
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.event.LayoutEvents.LayoutClickListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.BrowserWindowOpener
import com.vaadin.server.FileDownloader
import com.vaadin.server.FileResource
import com.vaadin.server.FontAwesome
import com.vaadin.server.StreamResource
import com.vaadin.server.ThemeResource
import com.vaadin.server.Sizeable.Unit
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickListener

class SpreadSheet2View extends HorizontalLayout implements View, SpreadsheetComponentFactory{

	protected Component layout
	
	protected Spreadsheet spreadSheet
	
	protected List<String> meses
	
	@Override
	public void enter(ViewChangeEvent event) {
		meses = Lists.newArrayList("Septiembre","Octubre","Noviembre","Diciembre","Enero","Febrero","Marzo","Abril","Mayo","Junio") 
		setSizeFull()
		buildSpreadSheet()
		 buildVerticalLayout()
		
		addComponent(spreadSheet)
		addComponent(layout)
		setExpandRatio(layout, 0.2f)
		setExpandRatio(spreadSheet, 0.8f)
	}
	
	def buildSpreadSheet(){
		spreadSheet = new Spreadsheet()
		spreadSheet.setSpreadsheetComponentFactory(this)
		spreadSheet.setImmediate(true)
		spreadSheet.setColumnWidth(0, 40)
		spreadSheet.setColumnWidth(1, 80)
		spreadSheet.setColumnWidth(2, 60)
		spreadSheet.setColumnWidth(3, 80)
		spreadSheet.setColumnWidth(4, 80)
		spreadSheet.setColumnWidth(5, 60)
		spreadSheet.setColumnWidth(6, 60)
		spreadSheet.setColumnWidth(7, 60)
		spreadSheet.setColumnWidth(8, 60)
		
	}

	def buildVerticalLayout(){
		layout = new VerticalLayout()
		layout.addStyleName("valo-menu")

		List<String> strings = new ArrayList();
		strings.add("3 º")
		strings.add("5 º")
		strings.add("2 º")

		ComboBox box = new ComboBox(null, strings)
		box.setInputPrompt("Filtre por Curso")

		box.addValueChangeListener(new ValueChangeListener(){
					void valueChange(com.vaadin.data.Property$ValueChangeEvent e){
						Notification.show("Añadir filtro")
					}
				})

		( (VerticalLayout) layout).addComponent(box)

		List<Movie> movies = new ArrayList<Movie>()

		Movie m1 = new Movie()
		m1.setScore(25)
		m1.setTitle("Jose munoz perez")
		movies.add(m1)

		m1 = new Movie();
		m1.setScore(5)
		m1.setTitle("Jesus Rodriguez de La osa Martinez")
		movies.add(m1)

		m1 = new Movie();
		m1.setScore(10)
		m1.setTitle("lllllllllll")
		movies.add(m1)

		m1 = new Movie();
		m1.setScore(20)
		m1.setTitle("rtytrtyrty")
		movies.add(m1)

		m1 = new Movie();
		m1.setScore(15)
		m1.setTitle("ESffgSA")
		movies.add(m1)

		m1 = new Movie();
		m1.setScore(25)
		m1.setTitle("piopiopio")
		movies.add(m1)


		for (int i = 0; i < 6; i++) {
			HorizontalLayout l = new HorizontalLayout()
			MarginInfo marg = new MarginInfo(true, false, false, false)
			l.setMargin(marg)
			l.setSizeFull()
			l.setSpacing(false)
			Movie m = movies.get(i)
			ThemeResource r = new ThemeResource(
					"img/profile-pic-300px.jpg")
			Image img = new Image(null,r)
			img.removeStyleName("v-caption-on-top")
			img.addStyleName("v-icon")
			img.setWidth(40.0f, Unit.PIXELS)
			img.setHeight(100,Unit.PERCENTAGE)
			l.addComponent(img)
			Label lbl = new Label(m.getTitle().size() > 25 ? m.getTitle().substring(0,25) : m.getTitle())
			l.addComponent(lbl)
			l.addLayoutClickListener(new LayoutClickListener(){

			private static final String STYLE_SELECTED = "colored"
						void layoutClick(com.vaadin.event.LayoutEvents$LayoutClickEvent e){
							Iterator<Component> components =layout.iterator()
							while ( components.hasNext() ){
								Component c = components.next()
								if ( c instanceof HorizontalLayout ){
									if ( ((HorizontalLayout) c).getComponentCount() >= 1 ){
										((HorizontalLayout) c).getComponent(1).removeStyleName(STYLE_SELECTED)
									}
								}
							}
							lbl.addStyleName(STYLE_SELECTED)
							updateSpreadSheet(m)
						}
					})
			l.setExpandRatio(img, 0.2f)
			l.setExpandRatio(lbl, 0.8f)
			( (VerticalLayout) layout).addComponent(l)
		}
	}
	
	
	private void updateSpreadSheet(Movie mov){
		if ( !spreadSheet.getWorkbook().getSheetName(0).equals(mov.getTitle()) ){
			spreadSheet.setSheetName(0, mov.getTitle())
		}
		spreadSheet.refreshAllCellValues()
		int inicio = 1
		for ( int mes = 0; mes < meses.size()  ; mes++ ){
			spreadSheet.createCell(0, inicio+mes, meses.get(mes))
		}
		for ( int i = 1; i <= 31; i++ ){
			spreadSheet.createCell(i, 0, i)
			spreadSheet.setRowHeight(i, 24)
		}
		for ( int mes = 0; mes < meses.size()  ; mes++ ){
			for ( int i = 1; i <= 31; i++ ){
				spreadSheet.createCell( i,inicio+mes, "Item")
			}
		}
		spreadSheet.refreshAllCellValues()
	}

	@Override
	public Component getCustomComponentForCell(Cell cell, int rowIndex,
			int columnIndex, Spreadsheet spreadsheet, Sheet sheet) {
		if (cell != null &&  cell.getStringCellValue().equals("Item")){
			Button b = new Button(FontAwesome.EDIT)
			b.addStyleName("tiny")
			String mes = meses.get(columnIndex-1)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy",new Locale("ES","es"))
			sdf.setLenient(false);
			Calendar c = Calendar.getInstance()
			
			int year = c.get(Calendar.YEAR)
			
			int month = c.get(Calendar.MONTH)
			
			if ( month > 7 ){
				if ( columnIndex-1 > 3 ){
					year++
				}
			}else{
				if ( columnIndex-1 <= 3 ){
					year--
				}
			}
			Date fecha =  new Date()
			boolean fechaValida = false
			try{
				fecha = sdf.parse(rowIndex + "/" + mes + "/" + year)
				b.setDescription(rowIndex + " de " + mes + " de " + year)
				fechaValida = true
			}catch( ParseException e ){
				fechaValida = false
			}
			
			
			c.setTime(fecha)
			
			if (!fechaValida  || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				b.setReadOnly(true)
				b.setEnabled(false)
				b.setIcon(FontAwesome.TIMES_CIRCLE_O)
			}else{
				b.addClickListener(new ClickListener(){
					void buttonClick(com.vaadin.ui.Button$ClickEvent e){
						Window w = new Window()
						MyUI.getCurrent().addWindow(w)
					}
				})
			}
			
			return b
		}
                
		return null;
	}

	@Override
	public Component getCustomEditorForCell(Cell cell, int rowIndex,
			int columnIndex, Spreadsheet spreadsheet, Sheet sheet) {
		return null;
	}

	@Override
	public void onCustomEditorDisplayed(Cell cell, int rowIndex,
			int columnIndex,Spreadsheet spreadsheet, Sheet sheet,
			Component customEditor) {
		
		
	}
	
}
