package app.perfil

import java.text.SimpleDateFormat

import app.util.Alumno

import com.google.common.collect.Lists
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.data.util.converter.Converter
import com.vaadin.data.util.filter.Between
import com.vaadin.data.util.filter.SimpleStringFilter
import com.vaadin.event.FieldEvents.TextChangeEvent
import com.vaadin.event.FieldEvents.TextChangeListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.server.Resource
import com.vaadin.server.Responsive
import com.vaadin.server.StreamResource
import com.vaadin.server.ThemeResource
import com.vaadin.server.Page.BrowserWindowResizeEvent
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.DateField
import com.vaadin.ui.Grid
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.TabSheet
import com.vaadin.ui.TextField
import com.vaadin.ui.TreeTable
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Grid.HeaderCell
import com.vaadin.ui.Grid.HeaderRow
import com.vaadin.ui.TabSheet.TabSheetTabImpl
import com.vaadin.ui.renderers.DateRenderer
import com.vaadin.ui.renderers.HtmlRenderer

class PerfilView extends CssLayout implements View,com.vaadin.server.Page.BrowserWindowResizeListener{

	protected CssLayout profile
	
	protected TabSheet componentes
	
	static List<Alumno> alumnos
	
	def browserAncho = Page.getCurrent().getBrowserWindowWidth()
	
	//9 pulgadas en adelante
	def anchoTablet = 900
	//9 pulgadas en adelante
	def alturaTable = 600
	
	//9 pulgadas en adelante
	def anchoMovil = 320
	//9 pulgadas en adelante
	def alturaMovil = 480
	
	static{
		alumnos = Lists.newArrayList()
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Antonio"))
		alumnos.add(new Alumno("Jesus"))
		alumnos.add(new Alumno("Maria"))
		alumnos.add(new Alumno("Josefa"))
		
		alumnos.add(new Alumno("Marcelno"))
		alumnos.add(new Alumno("Rafael"))
		alumnos.add(new Alumno("David"))
		
		alumnos.add(new Alumno("Carlos"))
		alumnos.add(new Alumno("Jose David"))
		
		alumnos.add(new Alumno("Miguel Angel"))
		alumnos.add(new Alumno("Miguel"))
		alumnos.add(new Alumno("Eu"))
		
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		
		alumnos.add(new Alumno("Jose"))
		
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Jose"))
		
		alumnos.add(new Alumno("Jose"))
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		Responsive.makeResponsive(this);
		Page.getCurrent().addBrowserWindowResizeListener(this)
		/********************************************************
		 * RANDOM ***********************************************
		 *******************************************************/
		Random rand = new Random()
		Alumno alum = alumnos.get(rand.nextInt(alumnos.size()-1))
		
		setSizeFull()
		
		
		addStyleName("reports")
		
		Resource r;
		if (alum.foto != null && alum.foto.length > 0) {
			com.vaadin.server.StreamResource source;
			StreamSource source2 = new StreamResource.StreamSource() {
				/**
				 *
				 */
				private static final long serialVersionUID = -3823582436185258502L;

				public InputStream getStream() {
					InputStream reportStream = null;
					reportStream = new ByteArrayInputStream(alum.foto)
					return reportStream;
				}
			};
			r = new StreamResource(source2, "profile-picture.png");
		} else {
			r =  new ThemeResource("img/profile-pic-300px.jpg")
		}
		Image img = new Image(null,r)
		
		Label lblNombre = new Label(alum.nombre)
		Label lblCurso = new Label( (rand.nextInt(3)+1) + "º ESO" )
		Label lblTutores = new Label("Es tutorado por " + " @username1 " + " y " + " @username2 ")
		Button b = new Button(FontAwesome.SEND)
		b.addStyleName("float-button")
		VerticalLayout vlDatos = new VerticalLayout(lblNombre,lblCurso,lblTutores)
		Label lbl3 = new Label( " Ausencias <br> 3")
		lbl3.setContentMode(ContentMode.HTML)
		Label lbl4 = new Label(  " Actitud <br>" + FontAwesome.THUMBS_O_UP.getHtml()  )
		lbl4.setContentMode(ContentMode.HTML)
		Label lbl5 = new Label("Repetido: <br> 3º E.S.O.")
		lbl5.setContentMode(ContentMode.HTML)
		CssLayout others = new CssLayout(){
			protected String getCss(Component c){
				return "text-align: center;color: #FFF;"
			}
		}
		others.addComponent(lbl3)
		others.addComponent(lbl4)
		others.addComponent(lbl5)
		vlDatos.setWidth("50%")
		profile = new CssLayout(img){
			
			protected String getCss(Component c) {
				System.err.println(browserAncho)
				String style = "background-color: rgb(33, 150, 243);"
				if ( c instanceof Image ){
					style += "border-radius: 150px;box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.05);"
					if (  browserAncho >= 1280 &&  Page.getCurrent().getBrowserWindowHeight() <= 600 ){
						style += "margin-left: 10%;margin-top: 4.1%;margin-bottom: 4.1%;"
						style += "height: 150px;width: 150px;max-height: 150px;max-width: 150px;min-height: 150px;min-width: 150px;"
					}else if (  browserAncho > 900 &&  Page.getCurrent().getBrowserWindowHeight() <= 600 ){
						style += "margin-left: 5%;margin-top: 4.1%;margin-bottom: 4.1%;"
						style += "height: 200px;width: 200px;"
					}else if ( browserAncho > 590 && browserAncho < 768 && Page.getCurrent().getBrowserWindowHeight() >= 1280 ) {
						style += "max-height: 130px;max-width: 130px;min-height: 130px;min-width: 130px;width: 130px;height: 130px;margin-left: 39.1%;margin-right: 39.1%;margin-top:2%;margin-bottom: 0%;"
					}else if ( browserAncho > 550 && browserAncho <= 768 && Page.getCurrent().getBrowserWindowHeight() >= 1024 ) {
						style += "margin-left: 5%;margin-top: 7%;margin-bottom: 7%;margin-right: 0%;min-height: 200px;max-height: 200px;"
					}else if ( browserAncho > 550 && browserAncho < 750 && Page.getCurrent().getBrowserWindowHeight() <= 360 ) {
						style += "margin-left: 1%;margin-top:2%;margin-bottom:2%;margin-right: 0%;"
						style += "max-height: 80px;max-width: 80px;min-height: 80px;min-width: 80px;"
					}else if ( browserAncho > 440 && browserAncho < 550 && Page.getCurrent().getBrowserWindowHeight() <= 400 ) {
						style += "margin-left: 2%;margin-top:4%;margin-bottom:4%;margin-right: 0px;"
						style += "max-height: 90px;max-width: 90px;min-height: 90px;min-width: 90px;"
					}else if ( browserAncho <= 610 && browserAncho > 320  ){
						style += "margin-left: 30%;margin-top:2%;margin-bottom:0%;"
						style += "min-height: 130px;max-height: 130px;width: 130px;max-width: 130px;height: 130px;min-width: 130px;"
					}else if ( browserAncho <= 320 ){
						style += "margin-left: 30%;margin-right: 30%;margin-top: 1.5%;margin-bottom: 0%;"
						style += "height: 130px;width: 130px;max-height: 130px;max-width: 130px;min-height: 130px;min-width: 130px;"
					}else if ( browserAncho < 1024 && browserAncho >= 980 && Page.getCurrent().getBrowserWindowHeight() > 1024 && Page.getCurrent().getBrowserWindowHeight() < 1280 ){
						style += "margin-top:20%;margin-bottom: 20%;margin-left: 5.5%;"
						style += "width: 200px;max-width: 200px;height: 200px;max-height: 200px;"
					}else if ( browserAncho < 980 && browserAncho >= 800 &&  Page.getCurrent().getBrowserWindowHeight() <= 1280 ){
						style += "margin-top:11.5%;margin-bottom: 11.5%;margin-left: 5.5%;"
						style += "width: 200px;max-width: 200px;height: 200px;max-height: 200px;"
					}else if ( browserAncho <= 980 && browserAncho >= 800 &&  Page.getCurrent().getBrowserWindowHeight() <= 1280 ){
						style += "margin-top:20%;margin-bottom: 20%;margin-left: 5.5%;margin-right: 0%;"
						style += "width: 200px;max-width: 200px;height: 200px;max-height: 200px;"
					}else if ( browserAncho <= 1280 && browserAncho > 815 && Page.getCurrent().getBrowserWindowHeight() <= 800 ){
						style += "margin-top: 5.5%;margin-bottom: 5.5%;margin-left: 5.5%;margin-right: 0%;"
						style += "width: 200px;max-width: 200px;height: 200px;max-height: 200px;"
					}else if ( browserAncho  > 1280 && Page.getCurrent().getBrowserWindowHeight() <= 900 ){
						style += "margin-top: 4.6%;margin-bottom: 4.6%;margin-left: 10%;margin-right: 0%;"
						style += "width: 200px;max-width: 200px;height: 200px;max-height: 200px;"
					}else{
						style += "margin-left: 5%;margin-top: 7%;margin-bottom: 7%;width: 200px;max-width: 200px;height: 200px;max-height: 200px;"
					}
				}
				if ( c instanceof VerticalLayout ){
					style += "color:#FFF;"
					if ( browserAncho <= 815 && browserAncho > 768 && Page.getCurrent().getBrowserWindowHeight() >= 1024 ){
						style += "width: 65%;min-width: 65%;margin-top: 19.5%;margin-left: 2%;margin-bottom: 19.5%;max-width: 65%;"
					}else if ( browserAncho <= 815 && browserAncho > 620 && Page.getCurrent().getBrowserWindowHeight() > 400 ){
						style += "width: 60%;min-width: 60%;margin-top: 15.1%;margin-bottom: 15.1%;margin-left: 2%;"
					}else if ( browserAncho <= 1280 && browserAncho > 1024 && Page.getCurrent().getBrowserWindowHeight() <= 600 ){
						style += "margin-top: 7.7%;margin-bottom: 7.7%;margin-left: 5.5%;"
						style += "width: 65%;max-width: 65%;"
					}else if ( browserAncho <= 1280 && browserAncho > 1024 && Page.getCurrent().getBrowserWindowHeight() <= 800 ){
						style += "margin-top: 11.3%;margin-bottom: 11.3%;margin-left: 5.5%;"
						style += "width: 65%;max-width: 65%;"
					}else if ( browserAncho >= 1024  && Page.getCurrent().getBrowserWindowHeight() >= 768 ) {
						style += "width: 63%;margin-top: 14%;margin-bottom: 14%;margin-left: 6.5%;"
					}else if ( browserAncho > 550 && browserAncho <= 760 && Page.getCurrent().getBrowserWindowHeight() <= 400 ) {
						style += "width: 45%;min-width: 45%;max-width: 45%;margin-top: 1%;margin-bottom: 1%;margin-left: 1%;"
					}else if ( browserAncho > 440 && browserAncho < 550 && Page.getCurrent().getBrowserWindowHeight() <= 400 ) {
						style += "width: 34%;min-width: 34%;max-width: 34%;margin-top: 0%;margin-bottom: 0%;margin-left: 2%;"
					}else if ( browserAncho <= 620 && browserAncho > 320 ){
						style += "width: 100%;min-width: 100%;margin-top: 13.5%;margin-bottom: 0%;margin-left: 2%;"
					}else if ( browserAncho < 1024 && browserAncho >= 980 && Page.getCurrent().getBrowserWindowHeight() > 1024 ){
						style += "margin-top: 28%;margin-left: 3%;margin-bottom: 28%;"
					}else if ( browserAncho <= 320 ){
						style += "width: 100%;min-width: 100%;margin-top: 2.5%;margin-left: 2%;"
					}
				}
				if ( c instanceof Button){
					if ( browserAncho >= 1280  && Page.getCurrent().getBrowserWindowHeight() <= 600 ) {
						style += "bottom: 55%;right: 60px;"
					}else if ( browserAncho >= 1024  && Page.getCurrent().getBrowserWindowHeight() >= 768 ) {
						style += "bottom: 56%;right: 20px;"
					}else if ( browserAncho >= 980  && Page.getCurrent().getBrowserWindowHeight() >= 1024 ) {
						style += "bottom: 57.5%;right: 20px;"
					}else 	if ( browserAncho > 768 && browserAncho <= 800 ) {
						style += "bottom: 64.6%;right: 20px;"
					}else 	if ( browserAncho >= 768 ) {
						style += "bottom: 63%;right: 20px;"
					}else if ( browserAncho < 768 && browserAncho > 550 ){
						style += "bottom: 2%;"
					}else if ( browserAncho < 550 && browserAncho > 440 ){
						style += "bottom: 2%;right: 20px;"
					}else if ( browserAncho <= 440){
						style += "bottom: 2%;right: 20px;"
					}
				}
				if ( c instanceof CssLayout ){
					style += "display: inline-flex;"
					if (  browserAncho > 900 &&  Page.getCurrent().getBrowserWindowHeight() <= 600 ){
						style += "margin-left: 40%;margin-right: 40%;margin-top: -5%;min-width: 210px;"
					}else if ( browserAncho <= 1280 && browserAncho > 1024 && Page.getCurrent().getBrowserWindowHeight() <= 800 ){
						style += "margin-left: 38%;margin-right: 38%;margin-top: -7%;"
					}else if ( browserAncho >= 1280  && Page.getCurrent().getBrowserWindowHeight() > 800 ) {
						style += "margin-left: 41%;margin-right: 41%;margin-top: -4%;min-width: 210px;"
					}else if ( browserAncho >= 1024  && Page.getCurrent().getBrowserWindowHeight() >= 768 ) {
						style += "margin-left: 35%;margin-right: 35%;margin-top: -10%;min-width: 210px;"
					}else if ( browserAncho > 815 && Page.getCurrent().getBrowserWindowHeight() <= 800 ) {
						style += "margin-left: 38%;margin-right: 38%;;margin-top: -7%;"
					}else if ( browserAncho > 590 && browserAncho < 768 && Page.getCurrent().getBrowserWindowHeight() >= 1280 ) {
						style += "margin-left: 32.3%;margin-right: 32.3%;margin-top: 27%;min-width: 210px;"
					}else if ( browserAncho > 550 && browserAncho <= 768 && Page.getCurrent().getBrowserWindowHeight() >= 1024 ) {
						style += "margin-left: 36.3%;margin-right: 36.3%;margin-top: -7%;min-width: 210px;"
					}else if ( browserAncho > 550 && browserAncho <= 760 && Page.getCurrent().getBrowserWindowHeight() <= 400 ) {
						style += "margin-left: 60%;margin-top: -14%;margin-right: 2%;"
					}else if ( browserAncho > 440 && browserAncho < 550 && Page.getCurrent().getBrowserWindowHeight() <= 400 ) {
						style += "margin-left: 55%;margin-top: -20%;min-width: 210px;"
					}else if ( browserAncho <= 815 && browserAncho > 700 && Page.getCurrent().getBrowserWindowHeight() <= 1280 ){
						style += "margin-left: 37%;margin-right: 37%;margin-top: -7%;min-width: 210px;"
					}else if ( browserAncho <= 815 && browserAncho > 700 ){
						style += "margin-left: 38%;margin-right: 38%;margin-top: -7%;min-width: 210px;"
					}else if ( browserAncho <= 700 && browserAncho > 550 ){
						style += "margin-left: 38%;margin-right: 38%;min-width: 210px;"
					}else if ( browserAncho < 550 && browserAncho > 440 ){
						style += "margin-left: 10%;"
					}else if ( browserAncho <= 440){
						style += "margin-left: 18%;margin-top: 12%;margin-right: 18%;min-width: 210px;"
					}else if ( browserAncho < 1024 && browserAncho >= 980 && Page.getCurrent().getBrowserWindowHeight() > 1024 ){
						style += "margin-left: 37%;margin-right: 37%;margin-top: -7%;min-width: 210px;"
					}else if ( browserAncho  > 1280 && Page.getCurrent().getBrowserWindowHeight() <= 900 ){
						style += "margin-top: 8.3%;margin-bottom: 8.3%;margin-left: 10%;min-width: 210px"
					}else if ( browserAncho > 815 ) {
						style += "margin-left: 38%;margin-right: 38%;;min-width: 210px;"
					}
					
				}				
				return style;
			}
		}
		profile.addComponent(vlDatos)
		profile.addComponent(others)
		profile.addComponent(b)
		profile.setWidth("100%")
		//profile.setHeight("40%")
		addComponent(profile)
		componentes = new TabSheet()
		componentes.setWidth("100%")
		componentes.addStyleName("centered-tabs")
		//componentes.addStyleName("equal-width-tabs")
		componentes.addStyleName("icons-on-top")
		// Create a grid
		Grid grid = new Grid();
		
		
		// Define some columns
		grid.addColumn("materia", String.class);
		grid.addColumn("fecha", Date.class);
		grid.addColumn("justificada", Boolean.class);
		
		Grid.Column bornColumn1 = grid.getColumn("materia");
		bornColumn1.setHeaderCaption("Materia");
		
		Grid.Column bornColumn3 = grid.getColumn("justificada");
		bornColumn3.setHeaderCaption("Justificada");
		bornColumn3.setRenderer(new HtmlRenderer(),
                       new Converter<String,Boolean>(){
		    @Override
		    public Boolean convertToModel(String value,
		        Class<? extends Boolean> targetType, Locale locale)
		        throws Converter.ConversionException {
		        return true;
		    }
		
		    @Override
		    public String convertToPresentation(Boolean value,
		        Class<? extends String> targetType, Locale locale)
		        throws Converter.ConversionException {
					if (value){
						return "<div style=\"color: green;\">" + FontAwesome.CHECK.getHtml()   + "</div>"
					}else{
						return "<div style=\"color: red;\" >" + FontAwesome.TIMES.getHtml()  + "</div>"
					}
			}
		
		    @Override
		    public Class<Boolean> getModelType() {
		        return Boolean.class;
		    }
		
		    @Override
		    public Class<String> getPresentationType() {
		        return String.class;
		    }
		});
		
		Grid.Column bornColumn2 = grid.getColumn("fecha");
		bornColumn2.setHeaderCaption("Fecha");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
		bornColumn2.setRenderer(new DateRenderer(sdf))
		
		// Create a header row to hold column filters
		HeaderRow filterRow = grid.appendHeaderRow();
		
		
		List<FaltaAsistencia> collection = Lists.newArrayList()
		collection.add(new FaltaAsistencia("Matemáticas", new Date(System.currentTimeMillis()),true));
		collection.add(new FaltaAsistencia("Matemáticas", new Date(System.currentTimeMillis()),false));
		collection.add(new FaltaAsistencia("Lengua y Literatura", new Date(System.currentTimeMillis()),true));
		collection.add(new FaltaAsistencia("Historia", new Date(System.currentTimeMillis()),true));
		collection.add(new FaltaAsistencia("Matemáticas", new Date(System.currentTimeMillis()),false));
  		
		BeanItemContainer<FaltaAsistencia> container = new BeanItemContainer<FaltaAsistencia>(FaltaAsistencia.class, 
			collection)
		
		grid.setContainerDataSource(container)
		
		// Set up a filter for all columns
		for (Object pid: grid.getContainerDataSource()
							 .getContainerPropertyIds()) {
			switch ( pid.toString() ){
				case "materia":
					HeaderCell cell = filterRow.getCell(pid);
					
					// Have an input field to use for filter
					TextField filterField = new TextField();
					filterField.setColumns(8);
					
					// Update filter When the filter input is changed
					filterField.addTextChangeListener(new TextChangeListener(){
						
						void textChange(TextChangeEvent change){
							// Can't modify filters so need to replace
							container.removeContainerFilters(pid);
							
							// (Re)create the filter if necessary
							if (! change.getText().isEmpty())
								container.addContainerFilter(
									new SimpleStringFilter(pid,
										change.getText(), true, false));
						}
						
						
					});
				
					cell.setComponent(filterField);
					break;
				case "fecha":
					System.err.println(pid.toString())
					HeaderCell cell = filterRow.getCell("fecha");
					
					// Have an input field to use for filter
					DateField filterField = new DateField();
					
					filterField.addValueChangeListener(new ValueChangeListener(){
						void valueChange(com.vaadin.data.Property$ValueChangeEvent e){
							// Can't modify filters so need to replace
							container.removeContainerFilters("fecha");
							Date d =  (Date) e.getProperty().getValue()
							SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy")
							String dta = sdf2.format(d)
							sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm")
							Date inicio = sdf2.parse(dta + " 00:00")
							Date fin = sdf2.parse(dta + " 23:59")
							Between bt = new Between("fecha", inicio, fin)
							container.addContainerFilter(bt);
						}
					})
				
					cell.setComponent(filterField);
					break;
			}
		}
		grid.setSizeFull()
		grid.addStyleName("no-vertical-lines");
		grid.addStyleName("horizontal-lines");
		
		
		 com.vaadin.ui.Calendar cal = new  com.vaadin.ui.Calendar("My Calendar");
		cal.setSizeFull()
		
		// Create the Accordion.
		TreeTable sample;
		sample = new TreeTable();
		sample.setSizeFull();
		sample.setSelectable(true);
 
		sample.addContainerProperty("Nombre", String.class, "");
		sample.addContainerProperty("Calificacion", Double.class, 0.0);
 
		populateWithRandomHierarchicalData(sample);
 
		
		
		
		Grid grid2 = new Grid()
		
		componentes.addTab(grid, "Ausencias", FontAwesome.TIMES)
		componentes.addTab(sample, "Histórico", FontAwesome.TIMES)
		componentes.addTab(grid2, "Items Evaluables", FontAwesome.TIMES)
		componentes.addTab(cal, "Calendario", FontAwesome.CALENDAR)
		componentes.addTab(new VerticalLayout(new Label("5")), "Otros", FontAwesome.TIMES)
		componentes.addTab(new VerticalLayout(new Label("6")), "Ausencias", FontAwesome.TIMES)
		addComponent(componentes)
		setWidth("100%")
	}
	
	@SuppressWarnings("unchecked")
	private void populateWithRandomHierarchicalData(final TreeTable sample) {
		final Random random = new Random();
		Double hours = new Double(0);
		final Object allProjects = sample.addItem([
				"All Projects", new Double(0.0) ].toArray(), null);
		for (final int year : Arrays.asList(2010, 2011, 2012, 2013)) {
			double yearHours = 0;
			final Object yearId = sample.addItem([ "Year " + year,
					yearHours ].toArray(), null);
			sample.setParent(yearId, allProjects);
			for (int project = 1; project < random.nextInt(4) + 2; project++) {
				double projectHours = 0;
				final Object projectId = sample.addItem(
						["Customer Project " + project,
								new Double(projectHours)].toArray(), null);
				sample.setParent(projectId, yearId);
				for (final String phase : Arrays.asList("Implementation",
						"Planning", "Prototype")) {
					final double phaseHours = new Double(random.nextInt(50));
					final Object phaseId = sample.addItem([phase,
							phaseHours].toArray(), null);
					sample.setParent(phaseId, projectId);
					sample.setChildrenAllowed(phaseId, false);
					sample.setCollapsed(phaseId, false);
					projectHours += phaseHours;
				}
				yearHours += projectHours;
				sample.getItem(projectId).getItemProperty("Calificacion")
						.setValue(projectHours);
				sample.setCollapsed(projectId, false);
			}
			hours += yearHours;
			sample.getItem(yearId).getItemProperty("Calificacion")
					.setValue(yearHours);
			sample.setCollapsed(yearId, false);
		}
		sample.getItem(allProjects).getItemProperty("Calificacion")
				.setValue(hours);
		sample.setCollapsed(allProjects, false);
	}
	
	protected String getCss(Component c) {
		String style = "width:100%;"
		if ( c instanceof CssLayout ){
			if ( browserAncho > 815 ) {
				style += "margin-top:0%;height: 40%;min-height: 40%;max-height: 40%;"
			}else if ( browserAncho > 320 && browserAncho <= 480 && Page.getCurrent().getBrowserWindowHeight() <= 320 ){
				style += "height: 40%;min-height: 40%;max-height: 40%;"
			}else if ( browserAncho <= 815 && browserAncho > 800 ){
				style += "margin-top:0%;height: 40%;min-height: 40%;"
			}else if ( browserAncho <= 800 && browserAncho > 620 ){
				style += "margin-top:0%;height: 30%;min-height: 30%;max-height: 30%;"
			}else if ( browserAncho <= 620 && browserAncho > 550 ){
				style += "margin-top:0%;height: 40%;min-height: 40%;"
			}else if ( browserAncho < 550 && browserAncho > 440 ){
				style += "height: 50%;min-height: 50%;"
			}else if ( browserAncho > 320 && browserAncho <= 480 && Page.getCurrent().getBrowserWindowHeight() <= 320 ){
				style += "height: 40%;min-height: 40%;max-height: 40%;"
			}else if ( browserAncho > 320 && browserAncho <= 440 ){
				style += "height: 53%;min-height: 56%;"
			}
			else if ( browserAncho <= 320){
				style += "min-height: 70%;height: 70%;"
			}
			style += "background-color: rgb(33, 150, 243);"
		}
		if ( c instanceof TabSheet ){
			if ( browserAncho > 815 ) {
				style += "height: 60%;min-height: 60%;"
			}else if ( browserAncho <= 815 && browserAncho > 800 ){
				style += "height: 60%;min-height: 60%;"
			}else if ( browserAncho <= 800 && browserAncho > 620 ){
				//style += "height: 30%;min-height: 30%;"
			}else if ( browserAncho <= 620 && browserAncho > 550 ){
				//style += "height: 40%;min-height: 40%;"
			}else if ( browserAncho < 550 && browserAncho > 440 ){
				//style += "height: 50%;min-height: 50%;"
			}else if ( browserAncho > 320 && browserAncho <= 440 ){
				//style += "height: 57%;min-height: 57%;"
			}
			else if ( browserAncho <= 440){
				( (com.vaadin.ui.Calendar) ( (TabSheetTabImpl) ( (TabSheet) c ).getTab(3)).getComponent()).setStartDate(new Date())
				( (com.vaadin.ui.Calendar) ( (TabSheetTabImpl) ( (TabSheet) c ).getTab(3)).getComponent()).setEndDate(new Date())
				//style += "min-height: 75%;height: 75%;"
			}
		}
		return style;
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
	
	
	class FaltaAsistencia{
		def materia
		def Date fecha
		def Boolean justificada
		public FaltaAsistencia(Object materia, Date fecha,Boolean justificada) {
			super();
			this.materia = materia;
			this.fecha = fecha;
			this.justificada = justificada
		}
		
		
	}

}
