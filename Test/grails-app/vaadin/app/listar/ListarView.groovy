/**
 * 
 */
package app.listar

import app.util.Alumno

import com.google.common.collect.Lists
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Resource
import com.vaadin.server.StreamResource
import com.vaadin.server.ThemeResource
import com.vaadin.server.Sizeable.Unit
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
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
class ListarView extends TabSheet implements View {

	
	static List<Alumno> alumnos
	
	protected List<Component> tray = Lists.newArrayList()
	
	
	
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
		//TODO CURSO
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
				cards.setMargin(new MarginInfo(false,true,false,false))
				vl.addComponent(cards)
				vl.setComponentAlignment(cards, Alignment.MIDDLE_CENTER)
			}
			tray.add(buildTray(curso))
			vl.addComponent(tray.get(curso))
			setTrayVisible(false,curso)
			//TODO CURSO
			Tab pestania = addTab(tab,  (curso + 1) + "ª ESO")
			pestania.setClosable(true)
			addStyleName("right-aligned-tabs")
		}
		
	}
	
	def Component buildCardView(Alumno alum,int tab){
		VerticalLayout card = new VerticalLayout()
		card.setHeight(100, Unit.PERCENTAGE)
		card.setMargin(new MarginInfo(false,true,false,false))
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
		card.addComponent(img)
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
		aceptar.setSizeFull()
		Button cancelar = new Button(null,FontAwesome.TIMES)
		cancelar.setSizeFull()
		cancelar.addStyleName("danger")
		Label lbl = new Label(alum.nombre)
		lbl.addStyleName("colored")
		HorizontalLayout botonera = new HorizontalLayout(aceptar,cancelar,lbl)
		botonera.setComponentAlignment(lbl, Alignment.MIDDLE_CENTER)
		botonera.setSizeFull()
		botonera.setSpacing(true)
		card.addComponent(botonera)
		return card
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
}
