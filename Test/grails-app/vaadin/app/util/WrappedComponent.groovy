package app.util

import com.vaadin.event.dd.DropHandler
import com.vaadin.ui.Component
import com.vaadin.ui.DragAndDropWrapper
import com.vaadin.ui.DragAndDropWrapper.DragStartMode

class WrappedComponent extends DragAndDropWrapper {
	
	private ReorderLayoutDropHandler dropHandler

	public WrappedComponent(Component content,ReorderLayoutDropHandler dropHandler) {
		super(content);
		setDragStartMode(DragStartMode.WRAPPER);
		this.dropHandler = dropHandler
	}

	@Override
	public DropHandler getDropHandler() {
		return dropHandler
	}
}