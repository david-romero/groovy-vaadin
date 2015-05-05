package app.components

import com.vaadin.data.Property
import com.vaadin.data.util.ObjectProperty
import com.vaadin.event.LayoutEvents.LayoutClickEvent
import com.vaadin.event.LayoutEvents.LayoutClickListener
import com.vaadin.server.FontAwesome
import com.vaadin.server.ClientConnector.AttachEvent
import com.vaadin.server.ClientConnector.AttachListener
import com.vaadin.server.Sizeable.Unit
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.themes.ValoTheme

class InlineTextEditor extends CustomComponent implements ClickListener{

    /*
     * This Property contains the String type value to be edited. The same
     * object is passed as content source for the label in read-only mode as
     * well as the data source for the RichTextArea in edit mode. From there on
     * synchronization between the two is automatic.
     */
    private final Property<String> property = new ObjectProperty<String>(
            "Enter text here...");
    private Component editor
    private Component readOnly
	Button save
	Button editButton

    public InlineTextEditor(String initialValue) {
        setWidth(100.0f, Unit.PERCENTAGE);
        addStyleName("inline-text-editor");

        editor = buildEditor();
        readOnly = buildReadOnly();

        if (initialValue != null) {
            property.setValue(initialValue);
        }

        setCompositionRoot(editor);
    }

    private Component buildReadOnly() {
        Label text = new Label(property);
        text.setContentMode(ContentMode.HTML);

        editButton = new Button(FontAwesome.EDIT);
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButton.addClickListener(this);

        CssLayout result = new CssLayout(text, editButton);
        result.addStyleName("text-editor");
        result.setSizeFull();
        result.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                if (event.getChildComponent() == text && event.isDoubleClick()) {
                    setCompositionRoot(editor);
                }
            }
        });
        return result;
    }

    private Component buildEditor() {
        RichTextArea rta = new RichTextArea(property);
        rta.setWidth(100.0f, Unit.PERCENTAGE);
        rta.addAttachListener(new AttachListener() {
            @Override
            public void attach(final AttachEvent event) {
                rta.focus();
                rta.selectAll();
            }
        });

        save = new Button("Save");
        save.setDescription("Edit");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addStyleName(ValoTheme.BUTTON_SMALL);
        save.addClickListener(this);

        CssLayout result = new CssLayout(rta, save);
        result.addStyleName("edit");
        result.setSizeFull();
        return result;
    }

	@Override
	public void buttonClick(ClickEvent event) {
		if ( event.getButton().equals(save) ){
			setCompositionRoot(readOnly);
		}else if ( event.getButton().equals(editButton) ) {
			setCompositionRoot(editor);
		}
	}

	public Property<String> getProperty() {
		return property;
	}
	
	

}
