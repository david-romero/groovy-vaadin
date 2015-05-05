/**
 * 
 */
package app.spreadsheet

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellValue
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.util.CellReference

import app.MyUI
import app.components.TopSixTheatersChart.Movie

import com.CustomStreamSource
import com.PDFStreamSource
import com.google.common.collect.Sets
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.vaadin.addon.spreadsheet.Spreadsheet
import com.vaadin.addon.spreadsheet.Spreadsheet.CellValueChangeListener
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
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.themes.ValoTheme

/**
 * @author David
 *
 */
class SpreadSheetView extends CssLayout implements View {
	
	protected Component layout
	
	protected Spreadsheet spreadSheet
	
	protected Component tray
	
	protected HorizontalLayout layoutGeneral
	
	protected Set<Cell> celdasPropias;
	
	protected Set<Cell> celdasModificadas;

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		layoutGeneral = new HorizontalLayout()
		layoutGeneral.setSizeFull()
		setSizeFull()
		buildSpreadSheet()
		buildVerticalLayout()
		
		layoutGeneral.addComponent(spreadSheet)
		layoutGeneral.addComponent(layout)
		layoutGeneral.setExpandRatio(layout, 0.2f)
		layoutGeneral.setExpandRatio(spreadSheet, 0.8f)
		addStyleName("schedule");
		addComponent(layoutGeneral)
		tray = buildTray()
		addComponent(tray)
		
	}



	def buildVerticalLayout(){
		layout = new VerticalLayout()
		layout.addStyleName("valo-menu")
		
		
		HorizontalLayout layoutExport = new HorizontalLayout()
		Button exportExcelButton = new Button(FontAwesome.FILE_EXCEL_O)
		exportExcelButton.setDescription("Excel")
		CustomStreamSource source = new CustomStreamSource(spreadSheet)
		String filename = "Cursos.xls"
		StreamResource resource = new StreamResource(source, filename);
		resource.setMIMEType("application/xls");
		resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + filename);
		BrowserWindowOpener opener = new BrowserWindowOpener(resource);
		opener.extend(exportExcelButton);
		Button exportPDFButton = new Button(FontAwesome.FILE_PDF_O)
		exportPDFButton.setDescription("PDF")
		
		FileDownloader downloader = new FileDownloader(new FileResource(File.createTempFile("temp", "txt")));
		
		exportPDFButton.addClickListener(new ClickListener(){
			void buttonClick(com.vaadin.ui.Button$ClickEvent event){
				downloader.extend(exportPDFButton);
				StreamResource resource2 = new StreamResource(new StreamSource(){
						
					java.io.InputStream getStream(){
						return createPDF()
					}
					
				}, "Informe.pdf");
				resource2.setMIMEType("application/pdf");
				resource2.getStream().setParameter("Content-Disposition", "attachment; filename="+"Informe.pdf");
				downloader.setFileDownloadResource(resource2);
			}
		})
		exportPDFButton.click()
		
		layoutExport.addComponents(exportExcelButton,exportPDFButton)
		
		( (VerticalLayout) layout).addComponent(layoutExport)
		
		List<String> strings = new ArrayList();
		strings.add("3 º")
		strings.add("5 º")
		strings.add("2 º")
		
		ComboBox box = new ComboBox("Filtro", strings)
		
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
			img.addStyleName("v-icon");
			img.setWidth(40.0f, Unit.PIXELS);
			img.setHeight(100,Unit.PERCENTAGE);
			l.addComponent(img)
			Label lbl = new Label(m.getTitle())
			l.addComponent(lbl)
			l.addLayoutClickListener(new LayoutClickListener(){
				void layoutClick(com.vaadin.event.LayoutEvents$LayoutClickEvent e){
					Iterator<Component> components =layout.iterator()
					while ( components.hasNext() ){
						Component c = components.next()
						if ( c instanceof HorizontalLayout ){
							if ( ((HorizontalLayout) c).getComponentCount() >= 1 ){
								((HorizontalLayout) c).getComponent(1).removeStyleName("colored")
							}
						}
					}
					lbl.addStyleName("colored")
					updateSpreadSheet(m)
					
					
				}
			})
			l.setExpandRatio(img, 0.2f)
			l.setExpandRatio(lbl, 0.8f)
			( (VerticalLayout) layout).addComponent(l)
		}
	}
	
	def InputStream createPDF(){
		String titulo =  "TEST"
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		//We will create output PDF document objects at this point
		Document iText_xls_2_pdf = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(iText_xls_2_pdf,output );
		iText_xls_2_pdf.open();
		
		Paragraph para=new Paragraph();
		Font font = FontFactory.getFont("Open Sans", 24, Font.BOLD);
		font.setColor(64,67,70)
		para.setFont(font)
		para.setAlignment(Element.ALIGN_CENTER);
		para.add(titulo)
		
		iText_xls_2_pdf.add(para)
		
		//Dos lineas en blanco
		iText_xls_2_pdf.add( Chunk.NEWLINE)
		iText_xls_2_pdf.add( Chunk.NEWLINE)
		
		output.flush()
		iText_xls_2_pdf.close()
		output.close()
	
		return new ByteArrayInputStream(output.toByteArray());
	}
	
	def buildSpreadSheet(){
		spreadSheet = new Spreadsheet()
		spreadSheet.setColumnWidth(1, 100)
		spreadSheet.setColumnWidth(5, 100)
		spreadSheet.addCellValueChangeListener(new CellValueChangeListener(){
			void onCellValueChange(com.vaadin.addon.spreadsheet.Spreadsheet$CellValueChangeEvent e){
				for ( CellReference  cell : e.getChangedCells() ){
					Cell cell2 = spreadSheet.getCell(cell.getRow(), cell.getCol())
					if ( cell2.getCellType() == Cell.CELL_TYPE_NUMERIC ){
						celdasModificadas.add(cell2)
					}
					if ( cell2.getCellType() == Cell.CELL_TYPE_FORMULA ){
						celdasModificadas.add(cell2)
					}
				}
				if ( celdasModificadas.size() > 0 )
					setTrayVisible(true)
			}
		})
		spreadSheet.setImmediate(true)
	}
	
	private void updateSpreadSheet(Movie mov){
		spreadSheet.setSheetName(0, mov.getTitle())
		for ( Cell cell : celdasPropias ){
			cell.setCellValue("")
		}
		spreadSheet.refreshAllCellValues()
		celdasPropias = Sets.newHashSet()
		celdasModificadas = Sets.newHashSet()
		Random rd = new Random()
		int columnas = 2
		int evaluaciones = 3
		int filas = 7
		int columna = 0
		for ( int evaluacion = 0; evaluacion < evaluaciones; evaluacion ++ ){
			org.apache.poi.ss.usermodel.Cell cell = spreadSheet.createCell(5, columna, "Evaluacion")
			celdasPropias.add(cell)
			org.apache.poi.ss.usermodel.Cell cellEval = spreadSheet.createCell(5, columna+1,(evaluacion+1))
			celdasPropias.add(cellEval)
			for ( int fila = 1; fila <= filas; fila ++ ){
				org.apache.poi.ss.usermodel.Cell cell2 = spreadSheet.createCell(5+fila, columna, "Examen  " + fila)
				celdasPropias.add(cell2)
				Double nota = ExcelView.randDouble(0,10)
				org.apache.poi.ss.usermodel.Cell cell3 = spreadSheet.createCell(5+fila, columna+1, nota)
				celdasPropias.add(cell3)
			}
			columna +=2
			def filaInicio = 5+(evaluacion*filas)+1+evaluacion+1
			def filaFin = 5+(evaluacion*filas)+filas+evaluacion+1
			def columnaIndicadorInicio = "C"
			def columnaIndicadorFin = "C"
			spreadSheet.createCell(1+evaluacion, 5, "Evaluacion "+ (evaluacion+1) )
			Cell celda = spreadSheet.createCell(1+evaluacion, 6, 0)
			celda.setCellFormula("ROUND(SUM($columnaIndicadorInicio$filaInicio:$columnaIndicadorFin$filaFin)/$filas,2)")
		}
		Cell celda = spreadSheet.createCell(1+3, 6, 0)
		celda.setCellFormula("ROUND(SUM(G7:G9)/3,2)")
		spreadSheet.refreshCells(celdasPropias)
	}
	
	private Component buildTray() {
		final HorizontalLayout tray = new HorizontalLayout();
		tray.setWidth(100.0f, Unit.PERCENTAGE);
		tray.addStyleName("tray");
		tray.setSpacing(true);
		tray.setMargin(true);

		Label warning = new Label(
				"You have unsaved changes made to the spreadsheet");
		warning.addStyleName("warning");
		warning.addStyleName("icon-attention");
		tray.addComponent(warning);
		tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
		tray.setExpandRatio(warning, 1);

		ClickListener close = new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				setTrayVisible(false)
			}
		};

		Button confirm = new Button("Confirm");
		confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
		confirm.addClickListener(close);
		confirm.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(final ClickEvent event) {
				Set<Cell> intersection = new HashSet<Cell>(celdasPropias);
				Set<Cell> modificadas = new HashSet<Cell>(celdasModificadas);
				
				intersection.retainAll(modificadas);
				for ( Cell cell : intersection ){
					if ( cell.getCellType() == Cell.CELL_TYPE_NUMERIC ){
						Notification.show(cell.getNumericCellValue() + "")
					}
					if ( cell.getCellType() == Cell.CELL_TYPE_FORMULA ){
						FormulaEvaluator evaluator= spreadSheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
						CellValue value=null;
						try {
						  value=evaluator.evaluate(cell);
						}
						 catch (RuntimeException ignore) {
							 value = new CellValue(0.0);
						 }
						Notification.show(value.getStringValue())
					}
				}
				celdasModificadas = Sets.newHashSet()
			}
			
		})
		tray.addComponent(confirm);
		tray.setComponentAlignment(confirm, Alignment.MIDDLE_LEFT);

		Button discard = new Button("Discard");
		discard.addClickListener(close);
		discard.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				MyUI.getCurrent().getNavigator().navigateTo("SpreadSheet2")
			}
		});
		tray.addComponent(discard);
		tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
		return tray;
	}
	
	private void setTrayVisible(boolean visible) {
		String styleReveal = "v-animate-reveal";
		if (visible) {
			tray.addStyleName(styleReveal);
		} else {
			tray.removeStyleName(styleReveal);
		}
	}

}
