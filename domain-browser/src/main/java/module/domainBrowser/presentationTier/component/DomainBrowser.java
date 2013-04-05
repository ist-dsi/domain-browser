/*
 * @(#)DomainBrowser.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Sérgio Silva
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the File Management Module.
 *
 *   The Domain Browser Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Domain Browser Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the File Management  Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.domainBrowser.presentationTier.component;

import java.util.Map;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.presentationTier.component.funStuff.Quote;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@EmbeddedComponent(path = { "DomainBrowser" }, args = { "externalId" })
/**
 * 
 * @author Luis Cruz
 * 
 */
public class DomainBrowser extends CustomComponent implements EmbeddedComponentContainer {

    private static final long serialVersionUID = 1L;

    private ComponentContainer domainObjectViewer = null;

    private AbstractLayout layout;

    public DomainBrowser() {
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(new Panel("<h2>Domain Browser</h2>", new SearchPanel()));
        layout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
        setCompositionRoot(layout);
    }

    @Override
    public void attach() {
        super.attach();

        if (domainObjectViewer != null) {
            layout.addComponent(domainObjectViewer);
        }
    }

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arg0) {
        final User user = UserView.getCurrentUser();
        return user != null && user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    public void setArguments(final Map<String, String> args) {
        if (domainObjectViewer != null) {
            layout.removeComponent(domainObjectViewer);
        }
        if (args != null) {
            final String externalId = args.get("externalId");
            final DomainObject domainObject = DomainUtils.readDomainObject(externalId);
            if (domainObject != null) {
                domainObjectViewer = new DomainObjectView(domainObject);
            }
        }
    }

    public class SearchPanel extends HorizontalLayout {

        private class ReadDomainObjectByExternalIdButton extends Button implements ClickListener {

            private static final long serialVersionUID = 1L;

            private ReadDomainObjectByExternalIdButton() {
                super("Search");
                final ClickListener clickListener = this;
                addListener(clickListener);
            }

            @Override
            public void buttonClick(final ClickEvent event) {
                if (domainObjectViewer != null) {
                    layout.removeComponent(domainObjectViewer);
                }

                final String value = (String) textField.getValue();
                if (value != null && !value.isEmpty() && StringUtils.isNumeric(value)) {
                    final DomainObject domainObject = AbstractDomainObject.fromExternalId(value);
                    if (domainObject == null) {
                    } else {
                        domainObjectViewer = new DomainObjectView(domainObject);
                        layout.addComponent(domainObjectViewer);
                    }
                }
            }
        }

        TextField textField = new TextField();

        private static final long serialVersionUID = 1L;

        public SearchPanel() {
            setMargin(true);
            setSpacing(true);

            Label label = new Label("External ID");
            addComponent(label);
            setComponentAlignment(label, Alignment.MIDDLE_LEFT);

            addComponent(textField);
            setComponentAlignment(textField, Alignment.MIDDLE_LEFT);

            ReadDomainObjectByExternalIdButton button = new ReadDomainObjectByExternalIdButton();
            addComponent(button);
            setComponentAlignment(button, Alignment.MIDDLE_LEFT);

            VerticalLayout paddingLeft = new VerticalLayout();
            paddingLeft.setWidth(100, UNITS_PIXELS);
            addComponent(paddingLeft);

            Resource resource = new ThemeResource("../../../images/BlueFenix.png");
            Embedded embedded = new Embedded(null, resource);
            addComponent(embedded);
            setComponentAlignment(embedded, Alignment.MIDDLE_RIGHT);

            VerticalLayout paddingRight = new VerticalLayout();
            paddingRight.setWidth(30, UNITS_PIXELS);
            addComponent(paddingRight);

            addComponent(new Quote());
        }
    }
}
