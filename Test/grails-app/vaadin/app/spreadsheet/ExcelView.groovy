package app.spreadsheet

import static com.vaadin.shared.ui.colorpicker.Color.BLACK
import static com.vaadin.shared.ui.colorpicker.Color.WHITE
import groovy.util.logging.Log4j;

import java.text.DecimalFormat
import java.text.ParsePosition;

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFFont

import app.MyUI

import com.CustomStreamSource
import com.PDFStreamSource
import com.vaadin.addon.spreadsheet.Spreadsheet
import com.vaadin.addon.spreadsheet.Spreadsheet.SelectionChangeEvent
import com.vaadin.addon.spreadsheet.Spreadsheet.SelectionChangeListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.BrowserWindowOpener
import com.vaadin.server.FileDownloader
import com.vaadin.server.FontAwesome
import com.vaadin.server.StreamResource
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinResponse
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.shared.ui.colorpicker.Color
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button
import com.vaadin.ui.ColorPicker
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.components.colorpicker.ColorChangeEvent
import com.vaadin.ui.components.colorpicker.ColorChangeListener
@Log4j
class ExcelView extends CssLayout implements View,SelectionChangeListener  {

	String [] abecedario = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]

	@Override
	public void enter(ViewChangeEvent event) {

		layout = new VerticalLayout();
		layout.setSizeFull();
		initSpreadsheet();
		initStyleToolbar();

		layout.addComponents(stylingToolbar, spreadsheet)
		layout.setExpandRatio(stylingToolbar, 0f)
		layout.setExpandRatio(spreadsheet, 1.0f)
		layout.setComponentAlignment(stylingToolbar, Alignment.MIDDLE_RIGHT)
		addComponent(layout)
		setSizeFull()
	}

	private static final long serialVersionUID = 6330513476932056681L;

	private VerticalLayout layout;
	private HorizontalLayout stylingToolbar;
	private Spreadsheet spreadsheet;
	private ColorPicker backgroundColor;
	private ColorPicker fontColor;
	
	int numAlumnos
	int numEventos


	private void initSpreadsheet() {
		spreadsheet = new Spreadsheet();
		spreadsheet.setSheetName(0, "Curso 1")
		spreadsheet.addSelectionChangeListener(this)
		numAlumnos = randInt(0,30)
		numEventos = randInt(15,50)
		List<Cell> cells = new ArrayList<Cell>();
		for (int i = 0; i < numEventos; i++){
			spreadsheet.createCell(4, i+1, "Evento $i")
		}
		for ( int i = 0; i < numAlumnos; i++ ){
			spreadsheet.createCell(i+5, 0, "Alumno $i")
			for ( int ii = 1; ii <= numEventos; ii++ ){
				spreadsheet.createCell(i+5, ii,randDouble(0,10))
				if ( ii == numEventos ){
					def columnaInicio = 1
					def columnaFin = ii
					def fila = i + 5+1
					def columnaIndicadorInicio = abecedario[columnaInicio]
					def columnaIndicadorFin = "B"
					if ((columnaFin-1) < abecedario.size() ){
						columnaIndicadorFin = abecedario[(columnaFin)]
					}else{
						columnaIndicadorFin = "A"+ abecedario[(columnaFin+1) % 27]
					}
					Cell celda = spreadsheet.createCell(i+5, ii+1,0)
					celda.setCellFormula("ROUND(SUM($columnaIndicadorInicio$fila:$columnaIndicadorFin$fila)/$numEventos,2)")
					String valueFormated = spreadsheet.getCellValue(celda)
					DataFormatter format = new DataFormatter(MyUI.getCurrent().getLocale())
					DecimalFormat format2 = new DecimalFormat("##.##")
					ParsePosition position = new ParsePosition(2)
					position.setIndex(2)
					position.setErrorIndex(2)
					Double notaObtenida = format2.parse(valueFormated)
					if ( notaObtenida < 5 ){

						XSSFCellStyle style = (XSSFCellStyle) cloneStyle(celda);
						Color newColor = new Color(255,0,0)
						XSSFColor color = new XSSFColor(java.awt.Color.decode(newColor
								.getCSS()));
						// Set new color value
						style.setFillForegroundColor(color);
						celda.setCellStyle(style);

						cells.add(celda);
					}else{

						XSSFCellStyle style = (XSSFCellStyle) cloneStyle(celda);
						Color newColor = new Color(0,255,0)
						XSSFColor color = new XSSFColor(java.awt.Color.decode(newColor
								.getCSS()));
						// Set new color value
						style.setFillForegroundColor(color);
						celda.setCellStyle(style);

						cells.add(celda);
					}
				}
			}
		}
		// Update all edited cells
		spreadsheet.refreshCells(cells);
		def filaFin =  5 + numAlumnos 
		def filaInicio = 1+5
		for ( int ii = 1; ii <= numEventos; ii++ ){
			def columna = "B"
			if ((ii) < abecedario.size() ){
				columna = abecedario[ii]
			}else{
				columna = "A"+ abecedario[(ii % abecedario.size())]
			}
			spreadsheet.createCell(5+numAlumnos, ii,0).setCellFormula("ROUND(SUM($columna$filaInicio:$columna$filaFin)/$numAlumnos,2)")
		}
	}

	private void initStyleToolbar() {
		stylingToolbar = new HorizontalLayout();
		Button boldButton = new Button(FontAwesome.BOLD);
		boldButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						updateSelectedCellsBold();
					}
				});
		backgroundColor = new ColorPicker();
		backgroundColor.setCaption("Background Color");
		backgroundColor.addColorChangeListener(new ColorChangeListener() {
					@Override
					public void colorChanged(ColorChangeEvent event) {
						updateSelectedCellsBackgroundColor(event.getColor());
					}
				});
		fontColor = new ColorPicker();
		fontColor.setCaption("Font Color");
		fontColor.addColorChangeListener(new ColorChangeListener() {
					@Override
					public void colorChanged(ColorChangeEvent event) {
						updateSelectedCellsFontColor(event.getColor());
					}
				});
		Button exportExcelButton = new Button(FontAwesome.FILE_EXCEL_O);
		exportExcelButton.setDescription("Excel")
		CustomStreamSource source = new CustomStreamSource(spreadsheet)
		String filename = "Cursos.xls"
		StreamResource resource = new StreamResource(source, filename);
		resource.setMIMEType("application/xls");
		resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + filename);
		BrowserWindowOpener opener = new BrowserWindowOpener(resource);
		opener.extend(exportExcelButton);

		Button exportPDFButton = new Button(FontAwesome.FILE_PDF_O);
		exportPDFButton.setDescription("PDF")
		PDFStreamSource sourcePDF = new PDFStreamSource(spreadsheet,numEventos+2)
		String filenamePDF = "Cursos.pdf"
		StreamResource resourcePDF = new StreamResource(sourcePDF, filenamePDF);
		resourcePDF.setMIMEType("application/pdf");
		resourcePDF.getStream().setParameter("Content-Disposition", "attachment; filename=" + filename);
		BrowserWindowOpener openerPDF = new BrowserWindowOpener(resourcePDF);
		openerPDF.extend(exportPDFButton);

		stylingToolbar.addComponents(boldButton, backgroundColor, fontColor,exportExcelButton,exportPDFButton);
	}



	private void updateSelectedCellsBold() {
		if (spreadsheet != null) {
			List<Cell> cellsToRefresh = new ArrayList<Cell>();
			for (CellReference cellRef : spreadsheet
			.getSelectedCellReferences()) {
				// Obtain Cell using CellReference
				Cell cell = getOrCreateCell(cellRef);
				// Clone Cell CellStyle
				CellStyle style = cloneStyle(cell);
				// Clone CellStyle Font
				Font font = cloneFont(style);
				// Toggle current bold state
				font.setBold(!font.getBold());
				style.setFont(font);
				cell.setCellStyle(style);

				cellsToRefresh.add(cell);
			}
			// Update all edited cells
			spreadsheet.refreshCells(cellsToRefresh);
		}
	}

	private void updateSelectedCellsBackgroundColor(Color newColor) {
		if (spreadsheet != null && newColor != null) {
			List<Cell> cellsToRefresh = new ArrayList<Cell>();
			for (CellReference cellRef : spreadsheet
			.getSelectedCellReferences()) {
				// Obtain Cell using CellReference
				Cell cell = getOrCreateCell(cellRef);
				// Clone Cell CellStyle
				// This cast an only be done when using .xlsx files
				XSSFCellStyle style = (XSSFCellStyle) cloneStyle(cell);
				XSSFColor color = new XSSFColor(java.awt.Color.decode(newColor
						.getCSS()));
				// Set new color value
				style.setFillForegroundColor(color);
				cell.setCellStyle(style);

				cellsToRefresh.add(cell);
			}
			// Update all edited cells
			spreadsheet.refreshCells(cellsToRefresh);
		}
	}

	private void updateSelectedCellsFontColor(Color newColor) {
		if (spreadsheet != null && newColor != null) {
			List<Cell> cellsToRefresh = new ArrayList<Cell>();
			for (CellReference cellRef : spreadsheet
			.getSelectedCellReferences()) {
				Cell cell = getOrCreateCell(cellRef);
				// Workbook workbook = spreadsheet.getWorkbook();
				XSSFCellStyle style = (XSSFCellStyle) cloneStyle(cell);
				XSSFColor color = new XSSFColor(java.awt.Color.decode(newColor
						.getCSS()));
				XSSFFont font = (XSSFFont) cloneFont(style);
				font.setColor(color);
				style.setFont(font);
				cell.setCellStyle(style);
				cellsToRefresh.add(cell);
			}
			// Update all edited cells
			spreadsheet.refreshCells(cellsToRefresh);
		}
	}

	private Cell getOrCreateCell(CellReference cellRef) {
		Cell cell = spreadsheet.getCell(cellRef.getRow(), cellRef.getCol());
		if (cell == null) {
			cell = spreadsheet.createCell(cellRef.getRow(), cellRef.getCol(),
					"");
		}
		return cell;
	}

	private CellStyle cloneStyle(Cell cell) {
		CellStyle newStyle = spreadsheet.getWorkbook().createCellStyle();
		newStyle.cloneStyleFrom(cell.getCellStyle());
		return newStyle;
	}

	private Font cloneFont(CellStyle cellstyle) {
		Font newFont = spreadsheet.getWorkbook().createFont();
		Font originalFont = spreadsheet.getWorkbook().getFontAt(
				cellstyle.getFontIndex());
		if (originalFont != null) {
			newFont.setBold(originalFont.getBold());
			newFont.setItalic(originalFont.getItalic());
			newFont.setFontHeight(originalFont.getFontHeight());
			newFont.setUnderline(originalFont.getUnderline());
			newFont.setStrikeout(originalFont.getStrikeout());
			// This cast an only be done when using .xlsx files
			XSSFFont originalXFont = (XSSFFont) originalFont;
			XSSFFont newXFont = (XSSFFont) newFont;
			newXFont.setColor(originalXFont.getXSSFColor());
		}
		return newFont;
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent event) {
		CellReference selectedCell = event.getSelectedCellReference();
		Cell cell = spreadsheet.getCell(selectedCell.getRow(),
				selectedCell.getCol());
		backgroundColor.setColor(WHITE);
		fontColor.setColor(BLACK);
		if (cell != null) {
			// This cast an only be done when using .xlsx files
			XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();
			if (style != null) {
				XSSFFont font = style.getFont();
				if (font != null) {
					XSSFColor xssfFontColor = font.getXSSFColor();
					if (xssfFontColor != null) {
						fontColor.setColor(convertColor(xssfFontColor));
					}
				}
				XSSFColor foregroundColor = style.getFillForegroundColorColor();
				if (foregroundColor != null) {
					backgroundColor.setColor(convertColor(foregroundColor));
				}
			}
		}
	}

	private Color convertColor(XSSFColor foregroundColor) {
		byte[] argb = foregroundColor.getARgb();
		return new Color(byteToInt(argb[1]), byteToInt(argb[2]),
				byteToInt(argb[3]), byteToInt(argb[0]));
	}

	private int byteToInt(byte byteValue) {
		return byteValue & 0xFF;
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static Double randDouble(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		double randomNum = min + (max - min) * rand.nextDouble()

		return round(randomNum,2)
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
