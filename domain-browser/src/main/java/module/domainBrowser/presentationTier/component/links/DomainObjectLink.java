package module.domainBrowser.presentationTier.component.links;

import module.domainBrowser.domain.DomainUtils;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.dml.Role;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

public class DomainObjectLink extends Link {

    private static final long serialVersionUID = 1L;

    protected DomainObject domainObject;

    public DomainObjectLink(final DomainObject domainObject) {
        this.domainObject = domainObject;
    }

    public DomainObjectLink(final DomainObject domainObject, final Role role) {
        this(DomainUtils.getRelationSlot(domainObject, role));
    }

    @Override
    public void attach() {
        super.attach();
        if (domainObject != null) {
            setCaption(getObjectDescription());
            setResource(new ExternalResource(getUrl()));
        }
    }

    protected String getObjectDescription() {
        return domainObject.getExternalId();
    }

    public String getUrl() {
        return "vaadinContext.do?method=forwardToVaadin#DomainBrowser?externalId=" + domainObject.getExternalId();
    }

}
