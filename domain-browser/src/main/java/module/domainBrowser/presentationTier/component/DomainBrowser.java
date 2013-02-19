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

    @SuppressWarnings("unchecked")
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

    final TextField textField = new TextField();
    ComponentContainer domainObjectViewer = null;
    final AbstractLayout layout = new VerticalLayout();

    @Override
    public void attach() {
        super.attach();
        layout.setSizeFull();
        setCompositionRoot(layout);
        createSearchForm();

        if (domainObjectViewer != null) {
            layout.addComponent(domainObjectViewer);
        }
    }

    private void createSearchForm() {
        final HorizontalLayout searchPanelLayout = new HorizontalLayout();
        searchPanelLayout.setMargin(true);
        searchPanelLayout.setSpacing(true);

        final Label label = new Label("External ID");
        searchPanelLayout.addComponent(label);
        searchPanelLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

        searchPanelLayout.addComponent(textField);
        searchPanelLayout.setComponentAlignment(textField, Alignment.MIDDLE_LEFT);

        final ReadDomainObjectByExternalIdButton button = new ReadDomainObjectByExternalIdButton();
        searchPanelLayout.addComponent(button);
        searchPanelLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);

        final VerticalLayout paddingLeft = new VerticalLayout();
        paddingLeft.setWidth(100, UNITS_PIXELS);
        searchPanelLayout.addComponent(paddingLeft);

        //final Resource resource = new ExternalResource("https://fenix-ashes.ist.utl.pt/home.do?method=logo&virtualHostId=42949672961");
        final Resource resource = new ThemeResource("../../../images/BlueFenix.png");
        final Embedded embedded = new Embedded(null, resource);
        searchPanelLayout.addComponent(embedded);
        searchPanelLayout.setComponentAlignment(embedded, Alignment.MIDDLE_RIGHT);

        final VerticalLayout paddingRight = new VerticalLayout();
        paddingRight.setWidth(30, UNITS_PIXELS);
        searchPanelLayout.addComponent(paddingRight);

        searchPanelLayout.addComponent(new Quote());

        layout.addComponent(new Panel("<h2>Domain Browser</h2>", searchPanelLayout));

        layout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
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
}
