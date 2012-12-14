package module.domainBrowser.presentationTier.component.links;

import module.domainBrowser.domain.DomainUtils;
import pt.ist.fenixframework.DomainObject;
import dml.Role;

public class RoleLink extends DomainObjectLink {

    private static final long serialVersionUID = 1L;

    public RoleLink(final DomainObject domainObject, final Role role) {
	super(DomainUtils.getRelationSlot(domainObject, role));
    }

}
