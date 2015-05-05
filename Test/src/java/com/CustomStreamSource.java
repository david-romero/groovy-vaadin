package com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class CustomStreamSource implements com.vaadin.server.StreamResource.StreamSource{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7131462501046584510L;
	private Spreadsheet spreadsheet;
	
	public CustomStreamSource(Spreadsheet spreadsheet){
		this.spreadsheet = spreadsheet;
	}

	public java.io.InputStream getStream()
    {
        try
        {
        	org.apache.poi.ss.usermodel.Workbook workbook = spreadsheet.getActiveSheet().getWorkbook();
         // Write the output to a file
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            workbook.write(output);
            output.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            return inputStream;
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	
}
