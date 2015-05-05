/**
 * 
 */
package app.util

import app.calendar.CalendarView.Transaction
import app.components.InlineTextEditor
import app.components.TopGrossingMoviesChart
import app.components.TopSixTheatersChart
import app.components.TopTenMoviesTable
import app.components.TransactionsListing
import app.reports.ReportEditor.PaletteItemType

import com.vaadin.event.Transferable
import com.vaadin.event.dd.DragAndDropEvent
import com.vaadin.event.dd.DropHandler
import com.vaadin.event.dd.DropTarget
import com.vaadin.event.dd.TargetDetails
import com.vaadin.event.dd.acceptcriteria.AcceptAll
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion
import com.vaadin.shared.ui.dd.VerticalDropLocation
import com.vaadin.ui.AbstractComponent
import com.vaadin.ui.Component

/**
 * @author David
 *
 */
class ReorderLayoutDropHandler implements DropHandler {

	def Component layout

	public ReorderLayoutDropHandler(Component layout){
		this.layout = layout
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		// return new SourceIs(component)
		return AcceptAll.get();
	}

	@Override
	public void drop(final DragAndDropEvent dropEvent) {
		Transferable transferable = dropEvent.getTransferable();
		Component sourceComponent = transferable.getSourceComponent();

		TargetDetails dropTargetData = dropEvent.getTargetDetails();
		DropTarget target = dropTargetData.getTarget();

		if (sourceComponent.getParent() != layout) {
			Object paletteItemType = ((AbstractComponent) sourceComponent)
					.getData();
			Component comp = createComponentFromPaletteItem(
					(PaletteItemType) paletteItemType, null)
			AbstractComponent c = new WrappedComponent(comp,this);

			int index = 0;
			Iterator<Component> componentIterator = layout.iterator();
			Component next = null;
			while (next != target && componentIterator.hasNext()) {
				next = componentIterator.next();
				if (next != sourceComponent) {
					index++;
				}
			}

			if (dropTargetData.getData("verticalLocation").equals(
			VerticalDropLocation.TOP.toString())) {
				index--;
				if (index <= 0) {
					index = 1;
				}
			}

			layout.addComponent(c, index);
		}

		if (sourceComponent instanceof WrappedComponent) {
			// find the location where to move the dragged component
			boolean sourceWasAfterTarget = true;
			int index = 0;
			Iterator<Component> componentIterator = layout.iterator();
			Component next = null;
			while (next != target && componentIterator.hasNext()) {
				next = componentIterator.next();
				if (next != sourceComponent) {
					index++;
				} else {
					sourceWasAfterTarget = false;
				}
			}
			if (next == null || next != target) {
				// component not found - if dragging from another layout
				return;
			}

			// drop on top of target?
			if (dropTargetData.getData("verticalLocation").equals(
			VerticalDropLocation.MIDDLE.toString())) {
				if (sourceWasAfterTarget) {
					index--;
				}
			}

			// drop before the target?
			else if (dropTargetData.getData("verticalLocation").equals(
			VerticalDropLocation.TOP.toString())) {
				index--;
				if (index <= 0) {
					index = 1;
				}
			}

			// move component within the layout
			layout.removeComponent(sourceComponent);
			layout.addComponent(sourceComponent, index);
		}
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
