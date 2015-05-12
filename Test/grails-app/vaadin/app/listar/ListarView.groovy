/**
 * 
 */
package app.listar

import app.util.Alumno

import com.google.common.collect.Lists
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.server.Resource
import com.vaadin.server.Responsive
import com.vaadin.server.StreamResource
import com.vaadin.server.ThemeResource
import com.vaadin.server.Page.BrowserWindowResizeEvent
import com.vaadin.server.Sizeable.Unit
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.TabSheet
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.TabSheet.Tab

/**
 * @author David
 *
 */
class ListarView extends TabSheet implements View ,com.vaadin.server.Page.BrowserWindowResizeListener{

	
	static List<Alumno> alumnos
	
	protected List<Component> tray = Lists.newArrayList()
	
	protected List<Tab> tabs = Lists.newArrayList()
	
	def browserAncho = Page.getCurrent().getBrowserWindowWidth()
	
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
		alumnos.add(new Alumno("Virgilio"))
		
		alumnos.add(new Alumno("Marco Aurelio"))
		alumnos.add(new Alumno("Jose"))
		alumnos.add(new Alumno("Augusto"))
		
		alumnos.add(new Alumno("Aaron"))
		alumnos.add(new Alumno("Descartes"))
		
		alumnos.add(new Alumno("Socrates"))
		
		alumnos.add(new Alumno("Tomás de Aquino"))
		
		alumnos.add(new Alumno("Sofía"))
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		Responsive.makeResponsive(this);
		Page.getCurrent().addBrowserWindowResizeListener(this)
		//TODO CURSO
		//pintarDatos()
		
	}

	private pintarDatos() {
		for ( Tab tab : tabs ){
			removeTab(tab)
		}
		tabs = Lists.newArrayList()
		for ( int curso = 0; curso < 5; curso++ ){
			VerticalLayout tab = new VerticalLayout()
			Panel p = new Panel()
			tab.addComponent(p)
			tab.setComponentAlignment(p, Alignment.MIDDLE_CENTER)
			p.setSizeFull()
			setSizeFull()
			VerticalLayout vl = new VerticalLayout()
			p.setContent(vl)
			p.getContent().setSizeUndefined();
			vl.setSpacing(true)
			vl.setMargin(true)
			vl.setWidth(100, Unit.PERCENTAGE )

			if ( browserAncho > 630  ){
				for ( int i = 0; i < alumnos.size() ; i += 3 ){
					Alumno primer = alumnos.get(i)
					HorizontalLayout cards
					if ( i + 2 < alumnos.size() ){
						Alumno segundo = alumnos.get(i+1)
						Alumno tercero = alumnos.get(i+2)
						cards = new HorizontalLayout(buildCardView(primer,curso),buildCardView(segundo,curso),buildCardView(tercero,curso))
						cards.setSizeFull()
					}else if ( i + 1 < alumnos.size() ){
						Alumno segundo = alumnos.get(i+1)
						cards = new HorizontalLayout(buildCardView(primer,curso),buildCardView(segundo,curso))
						cards.setWidth("68%")
					}else{
						cards = new HorizontalLayout(buildCardView(primer,curso))
						cards.setSizeFull()
					}
					cards.setHeight(100, Unit.PERCENTAGE)
					cards.setMargin(new MarginInfo(false,false,false,true))
					vl.addComponent(cards)
					vl.setComponentAlignment(cards, Alignment.MIDDLE_CENTER)
				}
			}else{
				for ( int i = 0; i < alumnos.size() ; i += 2 ){
					Alumno primer = alumnos.get(i)
					HorizontalLayout cards
					if ( i + 1 < alumnos.size() ){
						Alumno segundo = alumnos.get(i+1)
						cards = new HorizontalLayout(buildCardView(primer,curso),buildCardView(segundo,curso))
						cards.setSizeFull()
					}else{
						cards = new HorizontalLayout(buildCardView(primer,curso))
						cards.setSizeFull()
					}
					cards.setHeight(100, Unit.PERCENTAGE)
					cards.setMargin(new MarginInfo(false,false,false,true))
					vl.addComponent(cards)
					vl.setComponentAlignment(cards, Alignment.MIDDLE_CENTER)
				}
			}
			tray.add(buildTray(curso))
			vl.addComponent(tray.get(curso))
			setTrayVisible(false,curso)
			//TODO CURSO
			Tab pestania = addTab(tab,  (curso + 1) + "ª ESO")
			pestania.setClosable(true)
			tabs.add(pestania)
			addStyleName("right-aligned-tabs")
		}
	}
	
	def Component buildCardView(Alumno alum,int tab){
		CssLayout cssCard = new CssLayout(){
			protected String getCss(Component c) {
				return "border: 1px solid #E1E1E1;box-shadow: -1px 1px #E1E1E1, -2px 2px #E1E1E1, -1px 3px #E1E1E1, 0px 4px #E1E1E1, 0px 5px #E1E1E1;max-height: 309px;"
			}
		}
		VerticalLayout card = new VerticalLayout()
		card.setHeight(100, Unit.PERCENTAGE)
		card.setWidth("90%")
		CssLayout css = new CssLayout(){
			protected String getCss(Component c) {
				if ( browserAncho > 1024 ){
					return "text-align: center;float: left;position: relative;left: 0px;top: -35px;background-color: #92AD40;padding: 5px;color: #FFF;font-weight: bold;"
				}else{
					return "text-align: center;float: left;position: relative;left: 0px;top: -20px;background-color: #92AD40;padding: 5px;color: #FFF;font-weight: bold;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;-o-text-overflow: ellipsis;"
				}
			}
		}
		
		
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
		CssLayout cssImg = new CssLayout(){
			protected String getCss(Component c) {
				def style = ""
				if ( browserAncho <= 660 ){
					style += "width: 95px;min-width: 95px;max-width: 95px;"
				}else{
					style += "width: 100%;min-width: 100%;max-width: 100%;"
				}
				return style
			}
		}
		img.setWidth("100%")
		cssImg.addComponent(img)
		card.addComponent(cssImg)
		card.setComponentAlignment(cssImg, Alignment.MIDDLE_CENTER)
		Button aceptar = new Button(null,FontAwesome.CHECK)
		ClickListener cl = new ClickListener(){
			void buttonClick(com.vaadin.ui.Button$ClickEvent e){
				card.setVisible(false)
				if ( ( (HorizontalLayout) card.getParent()).getComponentIndex(card) == 1 ){
					( (HorizontalLayout) card.getParent()).setVisible(false)
				}
				boolean hayMasAlumnos = false
				VerticalLayout vl =  (VerticalLayout) ( (HorizontalLayout) card.getParent()).getParent()
				for ( int i = 0; i < vl.getComponentCount() ;i++ ){
					Component children = vl.getComponent(i)
					if ( children.isVisible() ){
						hayMasAlumnos = true
						i = vl.getComponentCount()
					}
				}
				if ( !hayMasAlumnos ){
					setTrayVisible(true,tab)
				}
			}
		}
		aceptar.addStyleName("friendly")
		aceptar.addClickListener(cl)
		aceptar.setWidth("100%")
		Button cancelar = new Button(null,FontAwesome.TIMES)
		cancelar.setWidth("100%")
		cancelar.addStyleName("danger") 
		
		HorizontalLayout botonera = new HorizontalLayout(aceptar,cancelar)
		botonera.setSizeFull()
		botonera.setSpacing(true)
		
		CssLayout cssBotonera = new CssLayout(botonera){
			protected String getCss(Component c) {
				if ( browserAncho > 1024 ){
					return "margin-top:-26px;"
				}else{
					return "margin-top:-12px;"
				}
			}
		}
		cssBotonera.setWidth("100%")
		
		card.addComponent(css)
		
		cssBotonera.addComponent(botonera)
		
		card.addComponent(cssBotonera)
		
		card.setComponentAlignment(cssBotonera, Alignment.MIDDLE_CENTER)
		
		Label lbl = new Label(alum.nombre)
		lbl.addStyleName("colored")
		css.addComponent(lbl)
		css.setHeight("0px")
		css.setWidth("100%")
		
		
		cssCard.addComponent(card)
		return cssCard
	}
	
	private Component buildTray(int tab) {
		HorizontalLayout tray = new HorizontalLayout();
		tray.setWidth(100.0f, Unit.PERCENTAGE);
		tray.addStyleName("tray");
		tray.setSpacing(true);
		tray.setMargin(true);

		Label warning = new Label(
				"No quedan mas alumnos");
		warning.addStyleName("warning");
		warning.addStyleName("icon-attention");
		tray.addComponent(warning);
		tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
		tray.setExpandRatio(warning, 1);

		ClickListener close = new ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						setTrayVisible(false,tab)
					}
				};


		Button discard = new Button(null,FontAwesome.TIMES);
		discard.addClickListener(close);
		tray.addComponent(discard);
		tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
		return tray;
	}

	private void setTrayVisible(boolean visible,int tab) {
		String styleReveal = "v-animate-reveal";
		if (visible) {
			tray.get(tab).addStyleName(styleReveal);
		} else {
			tray.get(tab).removeStyleName(styleReveal);
		}
		tray.get(tab).setVisible(visible)
	}
	
	@Override
	public void browserWindowResized(BrowserWindowResizeEvent event) {
		browserAncho = event.getWidth()
		attach()
	}
	@Override
	public void detach(){
		super.detach()
		Page.getCurrent().removeBrowserWindowResizeListener(this)
	}
	
	@Override
	public void attach(){
		super.attach()
		//TODO CURSO
		pintarDatos()
	}
}
