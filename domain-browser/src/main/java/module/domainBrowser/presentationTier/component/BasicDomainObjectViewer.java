package module.domainBrowser.presentationTier.component;

import pt.ist.fenixframework.DomainObject;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BasicDomainObjectViewer extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    protected final DomainObject domainObject;

    public BasicDomainObjectViewer(final DomainObject domainObject) {
	this.domainObject = domainObject;
    }

    @Override
    public void attach() {
	super.attach();
	addTitle();
    }

    protected void addTitle() {
	final StringBuilder builder = new StringBuilder("<h3>");
	builder.append(domainObject.getClass().getName());
	builder.append(": ");
	builder.append(domainObject.getExternalId());
	builder.append("</h3>");
	addComponent(new Label(builder.toString(), Label.CONTENT_XHTML));
    }

    protected Table createTable(final int size, final IndexedContainer container) {
	final Table table = new Table();
	table.setWidth(100, UNITS_PERCENTAGE);
	int height = size * 20 + 25;
	table.setHeight(height, UNITS_PIXELS);
	table.setContainerDataSource(container);
	return table;
    }

    protected class SlotValueTypeContainer extends IndexedContainer {

	private static final long serialVersionUID = 1L;

	protected SlotValueTypeContainer() {
	    addContainerProperty("Slot", String.class, null);
	    addContainerProperty("Value", String.class, null);
	    addContainerProperty("Type", String.class, null);
	}

    }

    protected class SlotLinkTypeContainer extends IndexedContainer {

	private static final long serialVersionUID = 1L;

	protected SlotLinkTypeContainer() {
	    addContainerProperty("Slot", String.class, null);
	    addContainerProperty("Value", Link.class, null);
	    addContainerProperty("Type", String.class, null);
	}

    }

    protected class LinkTypeContainer extends IndexedContainer {

	private static final long serialVersionUID = 1L;

	protected LinkTypeContainer() {
	    addContainerProperty("Value", Link.class, null);
	    addContainerProperty("Type", String.class, null);
	}

    }

}
