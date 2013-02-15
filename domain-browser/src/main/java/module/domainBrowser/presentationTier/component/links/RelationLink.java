package module.domainBrowser.presentationTier.component.links;

import pt.ist.fenixframework.DomainObject;
import dml.Role;

public class RelationLink extends DomainObjectLink {

    private static final long serialVersionUID = 1L;

    protected Role role;

    public RelationLink(final DomainObject domainObject, final Role role) {
        super(domainObject);
        this.role = role;
    }

    @Override
    protected String getObjectDescription() {
        return role.getName();
    }

    @Override
    public String getUrl() {
        return super.getUrl() + "&relationSet=" + role.getName();
    }

}
