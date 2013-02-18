package module.domainBrowser.presentationTier.component;

import pt.ist.fenixframework.DomainObject;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BasicDomainObjectViewer extends VerticalLayout {

    protected static final String VALUE_COLUMN = "Value";
    protected static final String SLOT_COLUMN = "Slot Name";
    protected static final String PLAYS_ROLE_COLUMN = "PlaysRole Name";
    protected static final String TYPE_COLUMN = "Type";

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
        protected SlotValueTypeContainer() {
            addContainerProperty(SLOT_COLUMN, String.class, null);
            addContainerProperty(VALUE_COLUMN, String.class, null);
            addContainerProperty(TYPE_COLUMN, String.class, null);
        }
    }

    protected class SlotDomainObjectContainer extends IndexedContainer {
        protected SlotDomainObjectContainer() {
            addContainerProperty(SLOT_COLUMN, String.class, null);
            addContainerProperty(VALUE_COLUMN, Link.class, null);
            addContainerProperty(TYPE_COLUMN, String.class, null);
        }
    }

    protected class RelationListButtonContainer extends IndexedContainer {
        protected RelationListButtonContainer() {
            addContainerProperty(PLAYS_ROLE_COLUMN, Button.class, null);
            addContainerProperty(TYPE_COLUMN, String.class, null);
        }
    }

    protected class RelationListContentContainer extends IndexedContainer {
        protected RelationListContentContainer() {
            addContainerProperty(VALUE_COLUMN, Link.class, null);
            addContainerProperty(TYPE_COLUMN, String.class, null);
        }
    }
}
