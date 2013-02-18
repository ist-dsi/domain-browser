package module.domainBrowser.presentationTier.component;

import java.util.Set;

import module.domainBrowser.presentationTier.component.links.DomainObjectLink;
import pt.ist.fenixframework.DomainObject;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class DomainRelationListViewer extends BasicDomainObjectViewer {

    private static final long serialVersionUID = 1L;

    protected final Set<DomainObject> relationSet;

    protected final String playsRole;

    public DomainRelationListViewer(final DomainObject domainObject, final Set<DomainObject> relationSet, String playsRole) {
        super(domainObject);
        this.relationSet = relationSet;
        this.playsRole = playsRole;
    }

    @Override
    protected void addTitle() {
        // Do nothing. This DomainRelationListViewer is meant to be viewed inside the DomainObjectViewer,
        // which already has a title.
    }

    @Override
    public void attach() {
        super.attach();

        final Table table = createTable(relationSet.size(), new RelationListContentContainer());
        for (final DomainObject domainObject : relationSet) {
            final Item item = table.addItem(domainObject.getExternalId());
            item.getItemProperty(VALUE_COLUMN).setValue(new DomainObjectLink(domainObject));
            item.getItemProperty(TYPE_COLUMN).setValue(domainObject.getClass().getName());
        }
        addComponent(table);
    }

}
