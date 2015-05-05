package com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.addon.spreadsheet.Spreadsheet;

public class PDFStreamSource implements
		com.vaadin.server.StreamResource.StreamSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7131462501046584510L;

	ByteArrayOutputStream output = new ByteArrayOutputStream();

	private Spreadsheet spreadsheet;
	
	private int columns;
	
	public PDFStreamSource(Spreadsheet spreadsheet,int columns) throws DocumentException,
			IOException {
		this.spreadsheet = spreadsheet;
		this.columns = columns;
	}

	public java.io.InputStream getStream() {
		Sheet my_worksheet = spreadsheet.getActiveSheet();
		// To iterate over the rows
        Iterator<Row> rowIterator = my_worksheet.iterator();
        
        try {
        	//We will create output PDF document objects at this point
            Document iText_xls_2_pdf = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance(iText_xls_2_pdf,output );
			iText_xls_2_pdf.open();
	        //we have two columns in the Excel sheet, so we create a PDF table with two columns
	        //Note: There are ways to make this dynamic in nature, if you want to.
	        PdfPTable my_table = new PdfPTable(columns);
	        
	        my_table.setWidthPercentage(100);
	        my_table.setSpacingBefore(0f);
	        my_table.setSpacingAfter(0f);
	        my_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	        my_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            Font font = FontFactory.getFont("Times-Roman", 10, Font.NORMAL);
            Font fontString = FontFactory.getFont("Times-Roman", 8, Font.NORMAL);
	        //We will use the object below to dynamically add new data to the table
	        PdfPCell table_cell;
	        
	        table_cell=new PdfPCell(new Phrase("",font));
            //first we add an empty cell
            my_table.addCell(table_cell);
            
            boolean primeraVez = true;
	        
            
	        //Loop through rows.
	        while(rowIterator.hasNext()) {
	                Row row = rowIterator.next(); 
	                Iterator<Cell> cellIterator = row.cellIterator();
	                        while(cellIterator.hasNext()) {
	                                Cell cell = cellIterator.next(); //Fetch CELL
	                                switch(cell.getCellType()) { //Identify CELL type
	                                        //you need to add more code here based on
	                                        //your requirement / transformations
	                                case Cell.CELL_TYPE_STRING: 
	                                        //Push the data from Excel to PDF Cell
	                                         table_cell=new PdfPCell(new Phrase(cell.getStringCellValue(),fontString));
	                                         my_table.addCell(table_cell); 
	                                        break;
	                                
	                                case Cell.CELL_TYPE_NUMERIC:
		                                	//Push the data from Excel to PDF Cell
	                                        table_cell=new PdfPCell(new Phrase(cell.getNumericCellValue() + "",font));
	                                        table_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                                        table_cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	                                        my_table.addCell(table_cell); 
	                                	break;
	                                	
	                                case Cell.CELL_TYPE_FORMULA:
	                                	FormulaEvaluator evaluator= spreadsheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
	                                	CellValue value=null;
	                                	try {
	                                	  value=evaluator.evaluate(cell);
	                                	}
	                                	 catch (RuntimeException ignore) {
	                                		 value = new CellValue(0.0);
	                                	 }
	                                	table_cell=new PdfPCell(new Phrase(value.formatAsString(),font));
	                                	table_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                                	table_cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	                                	int[] basico = new int[]{0,0,0};
		                                boolean esBasico =  basico == getRGBCellColor(cell);
		                                if (!esBasico){
		                                	int[] colorRGB  = getRGBCellColor(cell);
		                                	int redCode = colorRGB[0];
		                                    int greenCode = colorRGB[1];
		                                    int blueCode = colorRGB[2];
		                                	BaseColor color = new BaseColor(redCode, greenCode, blueCode);
		                                	table_cell.setBackgroundColor(color);
		                                }
	                                	my_table.addCell(table_cell); 
	                                	break;
	                                        
	                                        
	                                default:
		                                	table_cell=new PdfPCell(new Phrase("",font));
		                                	my_table.addCell(table_cell); 
	                                        //feel free to move the code below to suit to your needs
	                                        
	                                	break;
	                                }
	                                
	                                
	                        }
	                        if ( primeraVez ){
	                        	table_cell=new PdfPCell(new Phrase("",font));
	                            //first we add an empty cell
	                            my_table.addCell(table_cell);
	                            primeraVez = false;
	                        }

	        }
	        //Finally add the table to PDF document
	        iText_xls_2_pdf.add(my_table);                       
	        output.flush();
	        iText_xls_2_pdf.close();
	        output.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return new ByteArrayInputStream(output.toByteArray());
	}
	
	public int[] getRGBCellColor(Cell cell){
		XSSFCellStyle  style = (XSSFCellStyle) cell.getCellStyle();
		
	    XSSFColor color = null;
	    
	    color = style.getFillBackgroundXSSFColor();
	    
	    if ( color == null ){
	    	color = style.getFillForegroundXSSFColor();
	    }
	    byte[] argb = null;
	     
	    if ( color != null ){
	    	argb = color.getARgb();
	    }else{
	    	argb = new byte[]{0,0,0,0};
	    }
	    byte[] rgb = new byte[3];
	    System.arraycopy(argb, 1, rgb, 0, 3);
	    
	    int[] rgbInt = new int[3];
	    
	    int red = rgb[0] >= 0 ? rgb[0] : rgb[0] + 256;
	    int green = rgb[1] >= 0 ? rgb[1] : rgb[1] + 256;
	    int blue = rgb[2] >= 0 ? rgb[2] : rgb[2] + 256;
	    
	    rgbInt[0] = red;
	    rgbInt[1] = green;
	    rgbInt[2] = blue;
	    
	    // triplet will contain the actual rgb value
	    return rgbInt;
	}

}
