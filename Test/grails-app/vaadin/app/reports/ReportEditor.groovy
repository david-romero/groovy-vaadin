package app.reports

import java.text.SimpleDateFormat

import app.calendar.CalendarView.Transaction
import app.components.InlineTextEditor
import app.components.MovieRevenue
import app.components.TopGrossingMoviesChart
import app.components.TopSixTheatersChart
import app.components.TopTenMoviesTable
import app.components.TransactionsListing
import app.util.ReorderLayoutDropHandler
import app.util.WrappedComponent

import com.PDFUtil;
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.html.simpleparser.HTMLWorker
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.Image
import com.vaadin.addon.charts.Chart
import com.vaadin.addon.charts.model.Configuration
import com.vaadin.addon.charts.util.SVGGenerator
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.data.util.BeanItem
import com.vaadin.event.Transferable
import com.vaadin.event.LayoutEvents.LayoutClickEvent
import com.vaadin.event.LayoutEvents.LayoutClickListener
import com.vaadin.event.dd.DragAndDropEvent
import com.vaadin.event.dd.DropHandler
import com.vaadin.event.dd.acceptcriteria.AcceptAll
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion
import com.vaadin.server.FileDownloader
import com.vaadin.server.FileResource
import com.vaadin.server.FontAwesome
import com.vaadin.server.StreamResource
import com.vaadin.server.Sizeable.Unit
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.AbstractComponent
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.DragAndDropWrapper
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.DragAndDropWrapper.DragStartMode
import com.vaadin.ui.themes.ValoTheme


class ReportEditor extends VerticalLayout  {

    private final ReportEditorListener listener;
    private final SortableLayout canvas;

    public ReportEditor(ReportEditorListener listener) {
        this.listener = listener;
		setSizeFull()
        addStyleName("editor");
        addStyleName(ValoTheme.DRAG_AND_DROP_WRAPPER_NO_HORIZONTAL_DRAG_HINTS);

        Component palette = buildPalette();
        addComponent(palette);
		
		Button b = new Button(FontAwesome.FILE_PDF_O)
		b.addStyleName("float-button")
		addComponent(b);
		FileDownloader downloader = new FileDownloader(new FileResource(File.createTempFile("temp", "txt")));
		
		b.addClickListener(new ClickListener(){
			void buttonClick(com.vaadin.ui.Button$ClickEvent event){
				downloader.extend(b);
				StreamResource resource = new StreamResource(new StreamSource(){
						
					java.io.InputStream getStream(){
						return createPDF()
					}
					
				}, "Informe.pdf");
				resource.setMIMEType("application/pdf");
				resource.getStream().setParameter("Content-Disposition", "attachment; filename="+"Informe.pdf");
				downloader.setFileDownloadResource(resource);
			}
		})
		b.click()
        setComponentAlignment(palette, Alignment.TOP_CENTER);

        canvas = new SortableLayout();
        canvas.setWidth(100.0f, Unit.PERCENTAGE);
        canvas.addStyleName("canvas");
        addComponent(canvas);
        setExpandRatio(canvas, 1);
    }
	
	def InputStream createPDF(){
		Iterator<Component> ite = canvas.iterator()
		if ( ite.hasNext() ){
			Component layoutSortable = ite.next()
			if ( !layoutSortable instanceof VerticalLayout ){
				throw new IllegalArgumentException("NO valido")
			}
			VerticalLayout layout = layoutSortable
			String titulo =  (  (TextField) layout.getComponent(0)).getValue()
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
			
			Iterator<Component> componentesArrastrados = layout.iterator()
			
			/*
			 * Aqui tengo que iterar sobre los diferentes elementos del canvas para exportarlos al pdf
			 */
			
			while ( componentesArrastrados.hasNext() ){
				Component t = componentesArrastrados.next()
				if ( t instanceof DragAndDropWrapper ){
					t = ( (DragAndDropWrapper) t).iterator().next()
					//Patron factory deberia implementarse
					if ( t instanceof InlineTextEditor ){
						InlineTextEditor editor = t
						String html = editor.getProperty().getValue()
						StringReader strReader = new StringReader(html)
						ArrayList p=new ArrayList();
						p = HTMLWorker.parseToList(strReader, null);
						Paragraph paragraph=new Paragraph();
						font = FontFactory.getFont("Open Sans", 16, Font.NORMAL);
						font.setColor(70,70,70)
						paragraph.setFont(font)
						for (int k = 0; k < p.size(); ++k){
							paragraph.add((com.itextpdf.text.Element)p.get(k));
						}
						iText_xls_2_pdf.add(paragraph);
					}
					if ( t instanceof TopTenMoviesTable ){
						TopTenMoviesTable tablaAParsear = t;
						PdfPTable my_table = new PdfPTable(2);
						font = FontFactory.getFont("Times-Roman", 10, Font.NORMAL);
						my_table.setWidthPercentage(100);
						my_table.setSpacingBefore(0f);
						my_table.setSpacingAfter(0f);
						my_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER)
						my_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE)
						
						Collection<Object> itemsIds = tablaAParsear.getContainerDataSource().getItemIds()
						for ( Object itemId : itemsIds ){
							BeanItem item = tablaAParsear.getContainerDataSource().getItem(itemId)
							MovieRevenue mv = item.getBean()
							PdfPCell table_cell=new PdfPCell(new Phrase(mv.getTitle(),font));
							table_cell.disableBorderSide(Rectangle.TOP);
							table_cell.disableBorderSide(Rectangle.LEFT);
							table_cell.disableBorderSide(Rectangle.RIGHT);
							my_table.addCell(table_cell)
							table_cell=new PdfPCell(new Phrase('$'+mv.getRevenue(),font));
							table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table_cell.disableBorderSide(Rectangle.TOP);
							table_cell.disableBorderSide(Rectangle.LEFT);
							table_cell.disableBorderSide(Rectangle.RIGHT);
							my_table.addCell(table_cell)
						}
						
						iText_xls_2_pdf.add(my_table)
					}
					if ( t instanceof Chart ){
						System.setProperty("phantom.exec","C:/bin/phantomjs/bin/phantomjs.exe")
						Configuration conf = ( (Chart) t ).getConfiguration()
						String svg = SVGGenerator.getInstance().generate(conf)
						Image img = PDFUtil.drawUnscaledSvg(writer.getDirectContent(), svg)
						iText_xls_2_pdf.add(img)
					}
					System.err.println(t.getClass().getSimpleName())
					iText_xls_2_pdf.add( Chunk.NEWLINE)
					iText_xls_2_pdf.add( Chunk.NEWLINE)
				}
			}
			
			output.flush()
			iText_xls_2_pdf.close()
			output.close()
		
			return new ByteArrayInputStream(output.toByteArray());
		}else{
			return new ByteArrayInputStream();
		}
	}

    public void setTitle(final String title) {
        canvas.setTitle(title);
    }

    private Component buildPalette() {
        HorizontalLayout paletteLayout = new HorizontalLayout();
        paletteLayout.setSpacing(true);
        paletteLayout.setWidthUndefined();
        paletteLayout.addStyleName("palette");

        paletteLayout.addComponent(buildPaletteItem(PaletteItemType.TEXT));
        paletteLayout.addComponent(buildPaletteItem(PaletteItemType.TABLE));
        paletteLayout.addComponent(buildPaletteItem(PaletteItemType.CHART));
		paletteLayout.addComponent(buildPaletteItem(PaletteItemType.COLUMN_CHART));

        paletteLayout.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                if (event.getChildComponent() != null) {
                    PaletteItemType data = (PaletteItemType) ((DragAndDropWrapper) event
                            .getChildComponent()).getData();
                    addWidget(data, null);
                }
            }
        });

        return paletteLayout;
    }

    private Component buildPaletteItem(final PaletteItemType type) {
        Label caption = new Label(type.getIcon().getHtml() + type.getTitle(),
                ContentMode.HTML);
        caption.setSizeUndefined();

        DragAndDropWrapper ddWrap = new DragAndDropWrapper(caption);
        ddWrap.setSizeUndefined();
        ddWrap.setDragStartMode(DragStartMode.WRAPPER);
        ddWrap.setData(type);
        return ddWrap;
    }

    public void addWidget(final PaletteItemType paletteItemType,
            final Object prefillData) {
        canvas.addComponent(paletteItemType, prefillData);
    }

    class SortableLayout extends CustomComponent  {

        private VerticalLayout layout;
        private DropHandler dropHandler;
        private TextField titleLabel;
        private DragAndDropWrapper placeholder;

        public SortableLayout() {
            layout = new VerticalLayout();
            setCompositionRoot(layout);
            layout.addStyleName("canvas-layout");

            titleLabel = new TextField();
            titleLabel.addStyleName("title");
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern("M/dd/yyyy");

            titleLabel.addValueChangeListener(new ValueChangeListener() {
				
                @Override
                public void valueChange(ValueChangeEvent event) {
                    String t = titleLabel.getValue();
                    if (t == null || t.equals("")) {
                        t = " ";
                    }
                    listener.titleChanged(t, ReportEditor.this);
                }
            });
            layout.addComponent(titleLabel);

            dropHandler = new ReorderLayoutDropHandler(layout);

            Label l = new Label("Drag items here");
            l.setSizeUndefined();

            placeholder = new DragAndDropWrapper(l);
            placeholder.addStyleName("placeholder");
            placeholder.setDropHandler(new DropHandler() {

                @Override
                public AcceptCriterion getAcceptCriterion() {
                    return AcceptAll.get();
                }

                @Override
                public void drop(final DragAndDropEvent event) {
                    Transferable transferable = event.getTransferable();
                    Component sourceComponent = transferable
                            .getSourceComponent();

                    if (sourceComponent != layout.getParent()) {
                        Object type = ((AbstractComponent) sourceComponent)
                                .getData();
                        addComponent((PaletteItemType) type, null);
                    }
                }
            });
            layout.addComponent(placeholder);
        }

        public void setTitle(final String title) {
            titleLabel.setValue(title);
        }

        public void addComponent(final PaletteItemType paletteItemType,
                final Object prefillData) {
            if (placeholder.getParent() != null) {
                layout.removeComponent(placeholder);
            }
            layout.addComponent(
                    new WrappedComponent(createComponentFromPaletteItem(
                            paletteItemType, prefillData),dropHandler), 1);
        }

        private Component createComponentFromPaletteItem(
                final PaletteItemType type, final Object prefillData) {
            Component result = null;
            if (type == PaletteItemType.TEXT) {
                result = new InlineTextEditor(
                        prefillData != null ? String.valueOf(prefillData)
                                : null);
            } else if (type == PaletteItemType.TABLE) {
                result = new TopTenMoviesTable();
            }else if (type == PaletteItemType.CHART) {
                result = new TopSixTheatersChart()
            } else if (type == PaletteItemType.COLUMN_CHART) {
                result = new TopGrossingMoviesChart()
            } else if (type == PaletteItemType.TRANSACTIONS) {
                result = new TransactionsListing(
                        (Collection<Transaction>) prefillData);
            }

            return result;
        }


    }

    interface ReportEditorListener {
        void titleChanged(String newTitle, ReportEditor editor);
    }

    public enum PaletteItemType {
        TEXT("Text Block", FontAwesome.FONT), 
		TABLE("Top 10 Movies",FontAwesome.TABLE), 
		CHART("Top 6 Revenue",FontAwesome.BAR_CHART_O), 
		COLUMN_CHART("Top 6 Movies",FontAwesome.COLUMNS),
		TRANSACTIONS("Latest transactions", null);

        private final String title;
        private final FontAwesome icon;

        PaletteItemType(final String title, final FontAwesome icon) {
            this.title = title;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public FontAwesome getIcon() {
            return icon;
        }

    }
}
