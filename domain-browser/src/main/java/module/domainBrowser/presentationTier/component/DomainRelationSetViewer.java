package module.domainBrowser.presentationTier.component;

import java.util.Set;

import module.domainBrowser.presentationTier.component.links.DomainObjectLink;
import pt.ist.fenixframework.DomainObject;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class DomainRelationSetViewer extends BasicDomainObjectViewer {

    private static final long serialVersionUID = 1L;

    protected final Set<DomainObject> relationSet;

    public DomainRelationSetViewer(final DomainObject domainObject, final Set<DomainObject> relationSet) {
	super(domainObject);
	this.relationSet = relationSet;
    }

    @Override
    public void attach() {
	super.attach();
	final Table table = createTable(relationSet.size(), new LinkTypeContainer());
	for (final DomainObject domainObject : relationSet) {
	    final Item item = table.addItem(domainObject.getExternalId());
	    item.getItemProperty("Value").setValue(new DomainObjectLink(domainObject));
	    item.getItemProperty("Type").setValue(domainObject.getClass().getName());
	}
	addComponent(table);
    }

}
