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
import java.util.Random;

import module.domainBrowser.domain.DomainUtils;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.DomainObject;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Luis Cruz
 * 
 */
@SuppressWarnings("serial")
@EmbeddedComponent(path = { "DomainBrowser" }, args = { "externalId" })
public class DomainBrowser extends VerticalLayout implements EmbeddedComponentContainer {

    private ComponentContainer domainView;

    public DomainBrowser() {
        setSizeFull();
        addComponent(new Panel("<h2>Domain Browser</h2>", new SearchPanel()));
        addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arg0) {
        final User user = UserView.getCurrentUser();
        return user != null && user.hasRoleType(RoleType.MANAGER);
    }

    private void viewDomainObject(String id) {
        final DomainObject domainObject = DomainUtils.readDomainObject(id);
        if (domainObject != null) {
            if (domainView != null) {
                removeComponent(domainView);
            }
            domainView = new DomainObjectView(domainObject);
            addComponent(domainView);
        }
    }

    @Override
    public void setArguments(final Map<String, String> args) {
        if (args != null) {
            viewDomainObject(args.get("externalId"));
        }
    }

    public class SearchPanel extends HorizontalLayout {

        private class ReadDomainObjectByExternalIdButton extends Button implements ClickListener {

            private ReadDomainObjectByExternalIdButton() {
                super("Search");
                addListener((ClickListener) this);
            }

            @Override
            public void buttonClick(final ClickEvent event) {
                viewDomainObject((String) textField.getValue());
            }
        }

        TextField textField = new TextField();

        public SearchPanel() {
            setMargin(true);
            setSpacing(true);

            Label label = new Label("Browse Object by External ID");
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

    public static class Quote extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        private static final Random RANDOM = new Random(System.currentTimeMillis());

        private static final String[][] QUOTES = new String[][] {
                new String[] { "With great power, comes great responsibility.", "Stan Lee (Peter Parker)" },
                new String[] { "Use the Force, Luke.", "George Lucas (Obi-Wan)" },
                new String[] { "This is all your fault.", "George Lucas (C-3PO)" },
                new String[] { "Help me, Obi-Wan Kenobi; you're my only hope.", "George Lucas (Princess Leia)" },
                new String[] { "I sense something; a presence I've not felt since...", "George Lucas (Darth Vader)" },
                new String[] { "The Force is strong with this one.", "George Lucas (Darth Vader)" },
                new String[] { "The Force will be with you, always.", "George Lucas (Obi-Wan)" },
                new String[] { "I have a very bad feeling about this.", "George Lucas (Luke)" },
                new String[] { "That malfunctioning little twirp, this is all his fault.", "George Lucas (C-3PO)" },
                new String[] { "Your eyes can deceive you; don't trust them.", "George Lucas (Obi-Wan)" },
                new String[] { "There is no spoon.", "Neo (The Matrix)" },
                new String[] { "Free your mind.", "Morpheus (The Matrix)" },
                new String[] { "Welcome to the real world.", "Morpheus (The Matrix)" },
                new String[] { "Never send a human to do a machine's job.", "Agent Smith (The Matrix)" },
                new String[] { "Ignorance is bliss.", "Cypher (The Matrix)" } };

        public Quote() {
            setSizeFull();
            String[] quote = QUOTES[RANDOM.nextInt(QUOTES.length)];
            final Label quoteText = new Label("<i>\"" + quote[0] + "\"</i>", Label.CONTENT_XHTML);
            addComponent(quoteText);
            setComponentAlignment(quoteText, Alignment.MIDDLE_LEFT);
            final Label quoteAuthor = new Label("-- " + quote[1]);
            addComponent(quoteAuthor);
            setComponentAlignment(quoteAuthor, Alignment.MIDDLE_RIGHT);
        }
    }
}
