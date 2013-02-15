package module.domainBrowser.presentationTier.component.links;

import pt.ist.fenixframework.DomainObject;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

public class DomainObjectLink extends Link {

    private static final long serialVersionUID = 1L;

    protected DomainObject domainObject;

    private boolean showClassName = false;

    public DomainObjectLink(final DomainObject domainObject) {
        this(domainObject, false);
    }

    public DomainObjectLink(final DomainObject domainObject, boolean showClassName) {
        this.domainObject = domainObject;
        this.showClassName = showClassName;
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
        String description = "";
        if (showClassName) {
            description += domainObject.getClass().getName();
            description += ": ";
        }
        description += domainObject.getExternalId();
        return description;
    }

    public String getUrl() {
        return "vaadinContext.do?method=forwardToVaadin#DomainBrowser?externalId=" + domainObject.getExternalId();
    }

}
