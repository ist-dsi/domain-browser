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
    protected String caption() {
        return role.getName();
    }

    @Override
    protected String url() {
        return super.url() + "&relationSet=" + role.getName();
    }

}
