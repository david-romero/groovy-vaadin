/**
 * 
 */
package app.calendar

import app.components.MovieDetailsWindow

import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.Container.Hierarchical
import com.vaadin.data.fieldgroup.BeanFieldGroup
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.data.util.HierarchicalContainer
import com.vaadin.data.util.converter.Converter
import com.vaadin.data.validator.RegexpValidator
import com.vaadin.event.DataBoundTransferable
import com.vaadin.event.dd.DragAndDropEvent
import com.vaadin.event.dd.DropHandler
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion
import com.vaadin.event.dd.acceptcriteria.And
import com.vaadin.event.dd.acceptcriteria.ClientSideCriterion
import com.vaadin.event.dd.acceptcriteria.SourceIs
import com.vaadin.grails.navigator.VaadinView
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.ExternalResource
import com.vaadin.server.Page
import com.vaadin.server.Resource
import com.vaadin.server.WebBrowser
import com.vaadin.server.Sizeable.Unit
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import com.vaadin.ui.TextField
import com.vaadin.ui.Tree
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails
import com.vaadin.ui.AbstractSelect.AcceptItem
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Grid.SelectionMode
import com.vaadin.ui.Table.TableDragMode
import com.vaadin.ui.Tree.TargetItemAllowsChildren
import com.vaadin.ui.Tree.TreeDragMode
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent
import com.vaadin.ui.components.calendar.event.CalendarEvent
import com.vaadin.ui.components.calendar.event.CalendarEventProvider
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler
import com.vaadin.ui.renderers.ImageRenderer
import com.vaadin.ui.themes.ValoTheme

import es.test.util.common.ImageConverter

@VaadinView(path="calendar")
/**
 * @author David
 *
 */
class CalendarView extends CssLayout implements View{

	def com.vaadin.ui.Calendar calendar;
	def Component tray;

	def movies
	def transactions

	private Table table1
	
	BeanItemContainer<Hardware> tableContainer

	private Tree tree

	def CalendarView(){
		Movie movie1 = new Movie(id:1L,duration:180,title:"Test Peli")
		Movie movie2 = new Movie(id:2L,duration:150,title:"Test Peli 2")
		movies = [movie1, movie2] as List
		def transaction1 = new Transaction(movieId:1L,time:new Date())
		def transaction2 = new Transaction(movieId:2L,time:new Date())
		transactions = [transaction1, transaction2] as List
	}

	@Override
	void enter(ViewChangeEvent event) {
		addComponent(new Label("Calendar"))

		setSizeFull()
		addStyleName("schedule")

		TabSheet tabs = new TabSheet()
		tabs.setSizeFull()
		tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR)

		tabs.addComponent(buildCalendarView())
		tabs.addComponent(buildCatalogView())
		tabs.addComponent(buildGridView())
		tabs.addComponent(buildDragView())

		addComponent(tabs)

		tray = buildTray()
		addComponent(tray)

		injectMovieCoverStyles();
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of ScheduleView is created every time it's navigated
		// to so we'll need to clean up references to it on detach.
		//DashboardEventBus.unregister(this);
	}

	private void injectMovieCoverStyles() {
		// Add all movie cover images as classes to CSSInject
		String styles = "";
		for (Movie m : movies) {
			WebBrowser webBrowser = Page.getCurrent().getWebBrowser();

			String bg = "url(VAADIN/themes/" + UI.getCurrent().getTheme()
			+ "/img/event-title-bg.png), url(" + m.thumbUrl + ")";

			// IE8 doesn't support multiple background images
			if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() == 8) {
				bg = "url(" + m.thumbUrl + ")";
			}

			styles += ".v-calendar-event-" + m.id
			+ " .v-calendar-event-content {background-image:" + bg
			+ ";}";
		}

		Page.getCurrent().getStyles().add(styles);
	}

	private Component buildCalendarView() {
		VerticalLayout calendarLayout = new VerticalLayout()
		calendarLayout.setCaption("Calendar")
		calendarLayout.setMargin(true)

		calendar = new com.vaadin.ui.Calendar(new MovieEventProvider())
		calendar.setWidth(100.0f, Unit.PERCENTAGE)
		calendar.setHeight(1000.0f, Unit.PIXELS)

		calendar.setHandler(new EventClickHandler() {
					@Override
					public void eventClick(final EventClick event) {
						setTrayVisible(false);
						MovieEvent movieEvent = (MovieEvent) event.getCalendarEvent();
						MovieDetailsWindow.open(movieEvent.getMovie(),
								movieEvent.getStart(), movieEvent.getEnd());
					}
				});
		calendarLayout.addComponent(calendar);

		calendar.setFirstVisibleHourOfDay(11);
		calendar.setLastVisibleHourOfDay(23);

		calendar.setHandler(new BasicEventMoveHandler() {
					@Override
					public void eventMove(final MoveEvent event) {
						CalendarEvent calendarEvent = event.getCalendarEvent();
						if (calendarEvent instanceof MovieEvent) {
							MovieEvent editableEvent = (MovieEvent) calendarEvent;

							Date newFromTime = event.getNewStart();

							// Update event dates
							long length = editableEvent.getEnd().getTime()
							- editableEvent.getStart().getTime()
							setDates(editableEvent, newFromTime,
									new Date(newFromTime.getTime() + length))
							setTrayVisible(true)
						}
					}

					protected void setDates(final MovieEvent event, final Date start,
							final Date end) {
						event.start = start
						event.end = end
					}
				});
		calendar.setHandler(new BasicEventResizeHandler() {
					@Override
					public void eventResize(final EventResize event) {
						Notification
								.show("You're not allowed to change the movie duration")
					}
				})

		java.util.Calendar initialView = java.util.Calendar.getInstance();
		initialView.add(java.util.Calendar.DAY_OF_WEEK,
				-initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
		calendar.setStartDate(initialView.getTime());

		initialView.add(java.util.Calendar.DAY_OF_WEEK, 6);
		calendar.setEndDate(initialView.getTime());

		return calendarLayout;
	}

	private Component buildDragView(){
		// First create the components to be able to refer to them as allowed
		// drag sources
		tree = new Tree()
		tree.setSelectable(false)
		table1 = new Table()
		table1.setWidth(100.0f, Unit.PERCENTAGE)
		table1.setHeight(300.0f, Unit.PIXELS)

		// Populate the table and set up drag & drop
		initializeTable(new SourceIs(table1))
		
		// Populate the tree and set up drag & drop
		initializeTree(new SourceIs(tree))

		

		HorizontalLayout layout = new HorizontalLayout(tree, table1)
		layout.setCaption("Tree")
		layout.setSpacing(true)

		return layout
	}


	private void initializeTree(final ClientSideCriterion acceptCriterion) {
		HierarchicalContainer container = new  HierarchicalContainer();
		
		Item desktops = container.addItem("Desktops")
		Item monitors = container.addItem("Monitors")
		Item laptops = container.addItem("Laptops")
		container.setChildrenAllowed("Desktops", true)
		container.setChildrenAllowed("Monitors", true)
		container.setChildrenAllowed("Laptops", true)
		
		Item desktop1 = container.addItem("Acer 1465465")
		
		Item monitor1 = container.addItem("Toshiba TU00HD")
		
		Item laptop1 = container.addItem("HP MP40")
		
		
		container.setParent("Acer 1465465", "Desktops")
		
		container.setParent("Toshiba TU00HD", "Monitors")
		
		container.setParent("HP MP40", "Laptops")
		
		tree.setContainerDataSource(container)

		// Expand all nodes
		for (final Iterator<?> it = tree.rootItemIds().iterator(); it.hasNext();) {
			tree.expandItemsRecursively(it.next())
		}
		tree.setDragMode(TreeDragMode.NODE)
		tree.setDropHandler(new DropHandler() {
					@Override
					public void drop(final DragAndDropEvent dropEvent) {
						// criteria verify that this is safe
						final DataBoundTransferable t = (DataBoundTransferable) dropEvent
								.getTransferable()
						final Container sourceContainer = t.getSourceContainer()
						final Object sourceItemId = t.getItemId()
						final Item sourceItem = sourceContainer.getItem(sourceItemId)
						final String name = sourceItem.getItemProperty("name")
								.toString()
						final String category = sourceItem.getItemProperty("category")
								.toString()

						final AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent
								.getTargetDetails());
						final Object targetItemId = dropData.getItemIdOver();

						// find category in target: the target node itself or its parent
						if (targetItemId != null && name != null && category != null) {
							final String treeCategory = getTreeNodeName(tree,
									targetItemId);
							if (category.equals(treeCategory)) {
								// move item from table to category'
								final Object newItemId = tree.addItem();
								tree.getItem(newItemId)
										.getItemProperty(ExampleUtil.hw_PROPERTY_NAME)
										.setValue(name);
								tree.setParent(newItemId, targetItemId);
								tree.setChildrenAllowed(newItemId, false);

								sourceContainer.removeItem(sourceItemId);
							} else {
								final String message = name
								+ " is not a "
								+ treeCategory.toLowerCase().replaceAll('s$',
										"");
								Notification.show(message,
										Notification.Type.WARNING_MESSAGE);
							}
						}
					}

					@Override
					public AcceptCriterion getAcceptCriterion() {
						// Only allow dropping of data bound transferables within
						// folders.
						// In this example, checking for the correct category in drop()
						// rather than in the criteria.
						return new And(acceptCriterion, TargetItemAllowsChildren.get(),
								AcceptItem.ALL);
					}
				});
	}



	private void initializeTable(final ClientSideCriterion acceptCriterion) {
		final BeanItemContainer<Hardware> tableContainer = new BeanItemContainer<Hardware>(
				Hardware.class);
		tableContainer.addItem(new Hardware("Dell 380", "Desktops"));
		tableContainer.addItem(new Hardware("Benq T900HD", "Monitors"));
		tableContainer.addItem(new Hardware("Lenovo T500", "Laptops"));
		table1.setContainerDataSource(tableContainer);
		table1.setVisibleColumns("category", "name");

		// Handle drop in table: move hardware item or subtree to the table
		table1.setDragMode(TableDragMode.ROW);
		table1.setDropHandler(new DropHandler() {
					@Override
					public void drop(final DragAndDropEvent dropEvent) {
						// criteria verify that this is safe
						final DataBoundTransferable t = (DataBoundTransferable) dropEvent
								.getTransferable();
						if (!(t.getSourceContainer() instanceof Container.Hierarchical)) {
							return;
						}
						final Container.Hierarchical source = (Container.Hierarchical) t
								.getSourceContainer();

						final Object sourceItemId = t.getItemId();

						// find and convert the item(s) to move

						final Object parentItemId = source.getParent(sourceItemId);
						// map from moved source item Id to the corresponding Hardware
						final LinkedHashMap<Object, Hardware> hardwareMap = new LinkedHashMap<Object, Hardware>();
						if (parentItemId == null) {
							// move the whole subtree
							final String category = getTreeNodeName(source,
									sourceItemId);
							final Collection<?> children = source
									.getChildren(sourceItemId);
							if (children != null) {
								for (final Object childId : children) {
									final String name = getTreeNodeName(source, childId);
									hardwareMap.put(childId, new Hardware(name,
											category));
								}
							}
						} else {
							// move a single hardware item
							final String category = getTreeNodeName(source,
									parentItemId);
							final String name = getTreeNodeName(source, sourceItemId);
							hardwareMap.put(sourceItemId, new Hardware(name, category));
						}

						// move item(s) to the correct location in the table

						final AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent
								.getTargetDetails());
						final Object targetItemId = dropData.getItemIdOver();

						for (final Object sourceId : hardwareMap.keySet()) {
							final Hardware hardware = hardwareMap.get(sourceId);
							if (targetItemId != null) {
								switch (dropData.getDropLocation()) {
									case BOTTOM:
										tableContainer.addItemAfter(targetItemId, hardware);
										break;
									case MIDDLE:
									case TOP:
										final Object prevItemId = tableContainer
										.prevItemId(targetItemId);
										tableContainer.addItemAfter(prevItemId, hardware);
										break;
								}
							} else {
								tableContainer.addItem(hardware);
							}
							source.removeItem(sourceId);
						}
					}

					@Override
					public AcceptCriterion getAcceptCriterion() {
						return new And(acceptCriterion, AcceptItem.ALL);
					}
				});
	}

	private static String getTreeNodeName(final Container.Hierarchical source,
			final Object sourceId) {
		return (String) source.getItem(sourceId)
				.getItemProperty("name").getValue();
	}



	private Component buildGridView() {
		VerticalLayout calendarLayout = new VerticalLayout()
		calendarLayout.setCaption("Grid")
		calendarLayout.setMargin(true)
		Grid grid = new Grid(exampleDataSource())
		grid.setSelectionMode(SelectionMode.MULTI)

		// Allow deleting the selected items
		Button deleteSelected = new Button("Delete Selected", new Button.ClickListener(){

					void buttonClick(com.vaadin.ui.Button$ClickEvent e){
						for (Object itemId: grid.getSelectedRows())
							grid.getContainerDataSource().removeItem(itemId)

						// Disable after deleting
						e.getButton().setEnabled(false)
					}

				});
		deleteSelected.setEnabled(false) // Enable later
		calendarLayout.addComponent(grid)
		Grid.Column imageColumn = grid.getColumn("thumbUrl")
		imageColumn.setRenderer(new ImageRenderer(), new ImageConverter())

		Grid.Column otherColumn = grid.getColumn("metaClass")
		otherColumn.setConverter(new Converter<String, MetaClass>(){
					@Override
					public MetaClass convertToModel(String value,
							Class<? extends String> targetType, Locale l)
					throws Converter.ConversionException {
						return MetaClass.class;
					}

					@Override
					public String convertToPresentation(MetaClass value,
							Class<? extends Resource> targetType, Locale l)
					throws Converter.ConversionException {
						return value.getTheClass().getSimpleName()
					}

					@Override
					public Class<MetaClass> getModelType() {
						return MetaClass.class;
					}

					@Override
					public Class<String> getPresentationType() {
						return String.class;
					}
				})

		grid.setEditorEnabled(true)

		// Enable bean validation for the data
		grid.setEditorFieldGroup(
				new BeanFieldGroup<Movie>(Movie.class))

		// Have some extra validation in a field
		TextField nameEditor = new TextField();
		nameEditor.addValidator(new RegexpValidator(
				'^\\p{Alpha}+ \\p{Alpha}+$',
				"Need first and last name"));
		grid.setEditorField("metaClass", nameEditor)

		return calendarLayout
	}

	def exampleDataSource(){
		return new BeanItemContainer<Movie>(Movie.class, movies)
	}

	private Component buildCatalogView() {
		CssLayout catalog = new CssLayout()
		catalog.setCaption("Catalog")
		catalog.addStyleName("catalog")

		for (Movie movie : movies) {
			VerticalLayout frame = new VerticalLayout()
			frame.addStyleName("frame")
			frame.setWidthUndefined()

			Image poster = new Image(null, new ExternalResource(
					movie.getThumbUrl()))
			poster.setWidth(100.0f, Unit.PIXELS);
			poster.setHeight(145.0f, Unit.PIXELS)
			frame.addComponent(poster)

			Label titleLabel = new Label(movie.getTitle())
			titleLabel.setWidth(120.0f, Unit.PIXELS)
			frame.addComponent(titleLabel)



			/*frame.addLayoutClickListener(new LayoutClickListener() {
			 @Override
			 public void layoutClick(final LayoutClickEvent event) {
			 if (event.getButton() == MouseButton.LEFT) {
			 MovieDetailsWindow.open(movie,null,null)
			 }
			 }
			 });*/
			catalog.addComponent(frame)
		}
		return catalog;
	}

	private Component buildTray() {
		final HorizontalLayout tray = new HorizontalLayout();
		tray.setWidth(100.0f, Unit.PERCENTAGE);
		tray.addStyleName("tray");
		tray.setSpacing(true);
		tray.setMargin(true);

		Label warning = new Label(
				"You have unsaved changes made to the schedule");
		warning.addStyleName("warning");
		warning.addStyleName("icon-attention");
		tray.addComponent(warning);
		tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
		tray.setExpandRatio(warning, 1);

		ClickListener close = new ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						setTrayVisible(false);
					}
				};

		Button confirm = new Button("Confirm");
		confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
		confirm.addClickListener(close);
		tray.addComponent(confirm);
		tray.setComponentAlignment(confirm, Alignment.MIDDLE_LEFT);

		Button discard = new Button("Discard");
		discard.addClickListener(close);
		discard.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						calendar.markAsDirty();
					}
				});
		tray.addComponent(discard);
		tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
		return tray;
	}

	private void setTrayVisible(final boolean visible) {
		final String styleReveal = "v-animate-reveal";
		if (visible) {
			tray.addStyleName(styleReveal);
		} else {
			tray.removeStyleName(styleReveal);
		}
	}


	private class MovieEventProvider implements CalendarEventProvider {

		@Override
		public List<CalendarEvent> getEvents(final Date startDate,
				final Date endDate) {
			// Transactions are dynamically fetched from the backend service
			// when needed.
			Collection<Transaction> transactions = getTransactionsBetween(startDate,
					endDate);
			List<CalendarEvent> result = new ArrayList<CalendarEvent>();
			for (Transaction transaction : transactions) {
				Movie movie = getMovie(
						transaction.getMovieId())[0]
				Date end = new Date(transaction.getTime().getTime()
						+ movie.getDuration() * 60 * 1000);
				result.add(new MovieEvent(transaction.getTime(), end, movie));
			}
			return result;
		}
	}

	public final class MovieEvent implements CalendarEvent {

		private Date start;
		private Date end;
		private Movie movie;

		public MovieEvent(final Date start, final Date end, final Movie movie) {
			this.start = start;
			this.end = end;
			this.movie = movie;
		}

		@Override
		public Date getStart() {
			return start;
		}

		@Override
		public Date getEnd() {
			return end;
		}

		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public String getStyleName() {
			return String.valueOf(movie.getId());
		}

		@Override
		public boolean isAllDay() {
			return false;
		}

		public Movie getMovie() {
			return movie;
		}

		public void setMovie(final Movie movie) {
			this.movie = movie;
		}

		public void setStart(final Date start) {
			this.start = start;
		}

		public void setEnd(final Date end) {
			this.end = end;
		}

		@Override
		public String getCaption() {
			return movie.getTitle();
		}

	}

	public final class Movie {
		private long id;
		private String title;
		private String synopsis;
		private String thumbUrl;
		private String posterUrl;
		private int duration;
		private Date releaseDate;
		private int score;

		public void setId(final long id) {
			this.id = id;
		}

		public void setTitle(final String title) {
			this.title = title;
		}

		public void setSynopsis(final String synopsis) {
			this.synopsis = synopsis;
		}

		public void setThumbUrl(final String thumbUrl) {
			this.thumbUrl = thumbUrl;
		}

		public void setPosterUrl(final String posterUrl) {
			this.posterUrl = posterUrl;
		}

		public void setDuration(final int duration) {
			this.duration = duration;
		}

		public Date getReleaseDate() {
			return releaseDate;
		}

		public void setReleaseDate(final Date releaseDate) {
			this.releaseDate = releaseDate;
		}

		public int getScore() {
			return score;
		}

		public void setScore(final int score) {
			this.score = score;
		}

		public String getTitle() {
			return title;
		}

		public String getSynopsis() {
			return synopsis;
		}

		public String getThumbUrl() {
			return "https://avatars1.githubusercontent.com/u/1222264?v=3&s=48";
		}

		public String getPosterUrl() {
			return posterUrl;
		}

		public int getDuration() {
			return duration;
		}

		public long getId() {
			return id;
		}

	}

	public final class Transaction {
		private Date time;
		private String country;
		private String city;
		private String theater;
		private String room;
		private int seats;
		private double price;
		private long movieId;
		private String title;

		public long getMovieId() {
			return movieId;
		}

		public void setMovieId(final long movieId) {
			this.movieId = movieId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(final String title) {
			this.title = title;
		}

		public Date getTime() {
			return time;
		}

		public void setTime(final Date time) {
			this.time = time;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(final String country) {
			this.country = country;
		}

		public String getCity() {
			return city;
		}

		public void setCity(final String city) {
			this.city = city;
		}

		public String getTheater() {
			return theater;
		}

		public void setTheater(final String theater) {
			this.theater = theater;
		}

		public String getRoom() {
			return room;
		}

		public void setRoom(final String room) {
			this.room = room;
		}

		public int getSeats() {
			return seats;
		}

		public void setSeats(final int seats) {
			this.seats = seats;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(final double price) {
			this.price = price;
		}

	}

	def getTransactionsBetween(Date start,Date end){
		String query1 = "it.time between start and end "
		return transactions.findAll {  query1 }
	}

	def getMovie(long movieId){
		String query1 = "it.movieId == movieId "
		String query2 = "it.movieId == 2L "
		return movies.findAll { movieId ? query1 : query2 }
	}

	public static class Hardware implements Serializable {
		private String name;
		private String category;

		public Hardware(final String name, final String category) {
			this.name = name;
			this.category = category;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setCategory(final String category) {
			this.category = category;
		}

		public String getCategory() {
			return category;
		}
	}

	
	def getContainerHardaware(){
		return new BeanItemContainer<Hardware>(Hardware.class, movies)
	}
	
}
