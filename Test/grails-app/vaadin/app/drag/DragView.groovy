package app.drag

import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.data.util.HierarchicalContainer
import com.vaadin.event.DataBoundTransferable
import com.vaadin.event.dd.DragAndDropEvent
import com.vaadin.event.dd.DropHandler
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion
import com.vaadin.event.dd.acceptcriteria.And
import com.vaadin.event.dd.acceptcriteria.ClientSideCriterion
import com.vaadin.event.dd.acceptcriteria.SourceIs
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.Sizeable.Unit
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Notification
import com.vaadin.ui.Table
import com.vaadin.ui.Tree
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails
import com.vaadin.ui.AbstractSelect.AcceptItem
import com.vaadin.ui.Tree.TargetItemAllowsChildren
import com.vaadin.ui.Tree.TreeDragMode

class DragView extends HorizontalLayout implements View{

	def Tree tree

	def Table table

	@Override
	public void enter(ViewChangeEvent event) {
		// First create the components to be able to refer to them as allowed
		// drag sources
		tree = new Tree();
		tree.setSelectable(false);
		table = new Table();
		table.setWidth(100.0f, Unit.PERCENTAGE);
		table.setHeight(300.0f, Unit.PIXELS);

		// Populate the tree and set up drag & drop
		initializeTree(new SourceIs(table));

		// Populate the table and set up drag & drop
		initializeTable(new SourceIs(tree));

		addComponent tree
		addComponent table
		setSpacing(true);
	}

	private void initializeTree(ClientSideCriterion acceptCriterion) {
		tree.setContainerDataSource(getHardwareContainer());
		tree.setItemCaptionPropertyId("name");

		// Expand all nodes
		for (final Iterator<?> it = tree.rootItemIds().iterator(); it.hasNext();) {
			tree.expandItemsRecursively(it.next());
		}
		tree.setDragMode(TreeDragMode.NODE);
		tree.setDropHandler(new DropHandler() {
			@Override
			public void drop(final DragAndDropEvent dropEvent) {
				// criteria verify that this is safe
				final DataBoundTransferable t = (DataBoundTransferable) dropEvent
				.getTransferable();
				final Container sourceContainer = t.getSourceContainer();
				final Object sourceItemId = t.getItemId();
				final Item sourceItem = sourceContainer.getItem(sourceItemId);
				final String name = sourceItem.getItemProperty("name")
				.toString();
				final String category = sourceItem.getItemProperty("category")
				.toString();

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
						.getItemProperty("name")
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

	private void initializeTable(final ClientSideCriterion acceptCriterion) {
		final BeanItemContainer<Hardware> tableContainer = new BeanItemContainer<Hardware>(
		Hardware.class);
		tableContainer.addItem(new Hardware("Dell 380", "Desktops"));
		tableContainer.addItem(new Hardware("Benq T900HD", "Monitors"));
		tableContainer.addItem(new Hardware("Lenovo T500", "Laptops"));
		table.setContainerDataSource(tableContainer);
		table.setVisibleColumns("category name".split());

		// Handle drop in table: move hardware item or subtree to the table
		table.setDragMode(Table.TableDragMode.ROW);
		table.setDropHandler(new DropHandler() {
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
							case VerticalDropLocation.BOTTOM:
							tableContainer.addItemAfter(targetItemId, hardware);
							break;
							case VerticalDropLocation.MIDDLE:
							case VerticalDropLocation.TOP:
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

	public static final Object hw_PROPERTY_NAME = "name";

	public static final Object locale_PROPERTY_NAME = "name";
	private static final String[][] hardware = [ //
		[ "Desktops", "Dell OptiPlex GX240", "Dell OptiPlex GX260",
			"Dell OptiPlex GX280" ],
		[ "Monitors", "Benq T190HD", "Benq T220HD", "Benq T240HD" ],
		[ "Laptops", "IBM ThinkPad T40", "IBM ThinkPad T43",
			"IBM ThinkPad T60" ] ];

	public static HierarchicalContainer getHardwareContainer() {
		Item item = null;
		int itemId = 0; // Increasing numbering for itemId:s

		// Create new container
		HierarchicalContainer hwContainer = new HierarchicalContainer();
		// Create containerproperty for name
		hwContainer.addContainerProperty(hw_PROPERTY_NAME, String.class, null);
		for (int i = 0; i < hardware.length; i++) {
			// Add new item
			item = hwContainer.addItem(itemId);
			// Add name property for item
			item.getItemProperty(hw_PROPERTY_NAME).setValue(hardware[i][0]);
			// Allow children
			hwContainer.setChildrenAllowed(itemId, true);
			itemId++;
			for (int j = 1; j < hardware[i].length; j++) {
				// Add child items
				item = hwContainer.addItem(itemId);
				item.getItemProperty(hw_PROPERTY_NAME).setValue(hardware[i][j]);
				hwContainer.setParent(itemId, itemId - j);
				hwContainer.setChildrenAllowed(itemId, false);
				itemId++;
			}
		}
		return hwContainer;
	}

}
