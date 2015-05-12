package app.criterios

import java.text.DecimalFormat

import app.spreadsheet.ExcelView

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.shared.ui.datefield.Resolution
import com.vaadin.shared.ui.slider.SliderOrientation
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.Component
import com.vaadin.ui.DateField
import com.vaadin.ui.DefaultFieldFactory
import com.vaadin.ui.Field
import com.vaadin.ui.ProgressBar
import com.vaadin.ui.Slider
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.TabSheet.Tab
import com.vaadin.ui.Table.Align
import com.vaadin.ui.Table.RowHeaderMode

class CriteriosView extends TabSheet implements View {

	protected static List<String> classes
	
	protected Map<String,List<Slider>> sliders = Maps.newHashMap()
	
	Double acum = new Double(0.0)
	
	static{
		classes = Lists.newArrayList()
		classes.add("Examen")
		classes.add("Cuaderno")
		classes.add("Trabajo")
		classes.add("Otro")
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		for ( int curso = 0; curso < 5; curso++ ){
			acum = new Double(0.0)
			String cursoString = (curso + 1) + "ª ESO"
			sliders.put(cursoString, Lists.newArrayList())
			VerticalLayout tab = new VerticalLayout()
			tab.setSizeFull()
			tab.setSpacing(true)
			tab.setMargin(true)
			
			Table table = new Table();
			table.setSizeFull();
			table.setEditable(true);
			table.setSelectable(false)
			
			Button btn = new Button()
	 
			table.addContainerProperty("name", String.class,"")
	 
			
			table.addContainerProperty("porcentaje", Slider.class,null)
			table.addContainerProperty("porcentajeString", String.class," 0 % ")
					/*.setRenderer(new ProgressBarRenderer()).setExpandRatio(2).setEditorField(progressEditor)*/
					
			table.setRowHeaderMode(RowHeaderMode.INDEX)
			
			table.setColumnHeaders("Item","Porcentaje","Porcentaje")
			
			table.setColumnAlignment("porcentajeString", Align.RIGHT)
							
			for ( int i = 0; i < classes.size(); i++ ){
				Double perce = ExcelView.randDouble(0, 50)
				acum += perce
				Object itemId = i
				Item item = table.addItem(itemId)
				ProgressBar bar = new ProgressBar(0.75)
				
				Slider sld = new Slider(0.0.toDouble(),100.0.toDouble(),0.toInteger())
				sld.setSizeFull()
				sliders.get(cursoString).add(sld)
				sld.setOrientation(SliderOrientation.HORIZONTAL);
				sld.setValue(perce)
				sld.setImmediate(true)
				
				sld.addValueChangeListener(new ValueChangeListener(){
					void valueChange(com.vaadin.data.Property$ValueChangeEvent e){
						
						acum = new Double(0.0)
						String pattern = "##.##";
						DecimalFormat decimalFormat = new DecimalFormat(pattern);
						for ( Slider slider : sliders.get(cursoString) ){
							acum += slider.getValue()
						}
						
						table.getContainerProperty(itemId, "porcentajeString").setValue(decimalFormat.format(e.getProperty().getValue()) + " %")
						table.refreshRowCache()
						table.setColumnFooter("porcentajeString", decimalFormat.format(acum) + " %")
						
						if ( acum != 100 ){
							btn.setReadOnly(true)
							btn.setEnabled(false)
						}else{
							btn.setReadOnly(false)
							btn.setEnabled(true)
						}
						
					}
				})
				
				item.getItemProperty("porcentaje").setValue(sld)
				item.getItemProperty("porcentajeString").setValue(perce.toString() + " %")
				item.getItemProperty("name").setValue(classes.get(i))
			}
			
			// Add a summary footer row to the table
			table.setFooterVisible(true);
			String pattern = "##.##";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			table.setColumnFooter("porcentajeString", decimalFormat.format(acum) + " %")
			
			if ( acum != 100 ){
				btn.setReadOnly(true)
				btn.setEnabled(false)
			}else{
				btn.setReadOnly(false)
				btn.setEnabled(true)
			}
			
			// Set a custom field factory that overrides the default factory
			table.setTableFieldFactory(new DefaultFieldFactory() {
				@Override
				public Field<?> createField(Container container, Object itemId,
						Object propertyId, Component uiContext) {
					// Create fields by their class
					Class<?> cls = container.getType(propertyId);
			
					// Create a DateField with year resolution for dates
					if (cls.equals(Date.class)) {
						DateField df = new DateField();
						df.setResolution(Resolution.YEAR);
						return df;
					}
					
					// Create a CheckBox for Boolean fields
					if (cls.equals(Boolean.class))
						return new CheckBox();
						
					if (cls.equals(String.class)){
						TextField tf = new TextField()
						tf.setSizeFull()
						tf.setReadOnly(true)
						tf.setImmediate(true)
						tf.setPropertyDataSource(container.getContainerProperty(itemId, propertyId))
						return tf
					}
					
					// Otherwise use the default field factory
					return super.createField(container, itemId, propertyId,
											 uiContext);
				}
			});

			table.setPageLength(table.getContainerDataSource().size())
			tab.addComponent(table)
			
			
			btn.setImmediate(true)
			
			btn.setCaption("Guardar")
			btn.setIcon(FontAwesome.SAVE)
			btn.addStyleName("friendly")
			tab.addComponent(btn)
			tab.setComponentAlignment(btn, Alignment.MIDDLE_RIGHT)
			Tab pestania = addTab(tab,  (curso + 1) + "ª ESO")
			pestania.setClosable(true)
			addStyleName("right-aligned-tabs")
			
		}

	}

	@Override
	public void attach() {
		// TODO Auto-generated method stub
		super.attach();
		
		Page.getCurrent().getStyles().add(".v-textfield {     text-align: right; }")
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
		super.detach();
		Page.getCurrent().getStyles().add(".v-textfield {     text-align: initial; }")
	}
	
	

}
