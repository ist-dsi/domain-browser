package module.domainBrowser.presentationTier.component.links;

import pt.ist.fenixframework.DomainObject;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

public class DomainObjectLink extends Link {

    private static final long serialVersionUID = 1L;

    protected DomainObject domainObject;

    public DomainObjectLink(final DomainObject domainObject) {
	this.domainObject = domainObject;
    }

    @Override
    public void attach() {
        super.attach();
        if (domainObject != null) {
            setCaption(caption());
            setResource(new ExternalResource(url()));
        }
    }

    protected String caption() {
	return domainObject.getExternalId();
    }

    protected String url() {
	return "vaadinContext.do?method=forwardToVaadin#DomainBrowser?externalId=" + domainObject.getExternalId();
    }

}
