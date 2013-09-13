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

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicatesConfig;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * 
 * @author Luis Cruz
 * 
 */
@SuppressWarnings("serial")
@EmbeddedComponent(path = { "DomainBrowser" }, args = { "externalId", "viewAllClasses", "classSearch", "className" })
public class DomainBrowser extends VerticalLayout implements EmbeddedComponentContainer {

    private static final String NO_DOMAIN_META_OBJECTS_WARNING =
            "To view domain class information, the fenix framework must be configured to create meta objects (see Config.canCreateDomainMetaObjects)";
    private Component domainView;

    private SearchPanel searchPanel = new SearchPanel();

    public DomainBrowser() {
        setSizeFull();
        addComponent(new Panel("<h2>Domain Browser</h2>", searchPanel));
        addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arg0) {
        User user = UserView.getCurrentUser();
        return user != null && user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    public void setArguments(final Map<String, String> args) {
        if (args == null) {
            return;
        }
        String externalId = args.get("externalId");
        if (externalId != null) {
            viewDomainObject(externalId);
            searchPanel.setInputValue(externalId);
            return;
        }
        String viewAllClasses = args.get("viewAllClasses");
        if (viewAllClasses != null) {
            searchClasses("");
            return;
        }
        String classSearch = args.get("classSearch");
        if (classSearch != null) {
            searchClasses(classSearch);
            searchPanel.setInputValue(classSearch);
            return;
        }
        String className = args.get("className");
        if (className != null) {
            viewDomainClass(className);
        }
    }

    private void viewDomainObject(String id) {
        DomainObject domainObject = DomainUtils.readDomainObject(id);
        if (domainObject != null) {
            changeDomainView(new DomainObjectView(domainObject));
        } else {
            changeDomainView(new Label("No such object found: " + id));
        }
    }

    private void searchClasses(String classSearch) {
        if (!ConsistencyPredicatesConfig.canCreateDomainMetaObjects()) {
            changeDomainView(new Label(NO_DOMAIN_META_OBJECTS_WARNING));
            return;
        }
        changeDomainView(new DomainClassListView(classSearch));
    }

    private void viewDomainClass(String className) {
        if (!ConsistencyPredicatesConfig.canCreateDomainMetaObjects()) {
            changeDomainView(new Label(NO_DOMAIN_META_OBJECTS_WARNING));
            return;
        }
        try {
            Class<? extends DomainObject> targetClass = (Class<? extends DomainObject>) Class.forName(className);
            if (DomainObject.class.isAssignableFrom(targetClass)) {
                DomainMetaClass metaClass = DomainMetaClass.readDomainMetaClass(targetClass);
                if (metaClass != null) {
                    changeDomainView(new DomainClassView(metaClass));
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void changeDomainView(Component newDomainView) {
        if (domainView != null) {
            removeComponent(domainView);
        }
        domainView = newDomainView;
        addComponent(domainView);
    }

    public class SearchPanel extends GridLayout {
        private class SearchButton extends Button implements ClickListener {

            private SearchButton() {
                super("Search");
                addListener((ClickListener) this);
            }

            @Override
            public void buttonClick(final ClickEvent event) {
                String fieldValue = ((String) textField.getValue()).trim();
                if (StringUtils.isEmpty(fieldValue)) {
                    return;
                }
                if (StringUtils.isNumeric(fieldValue)) {
                    EmbeddedApplication.open(getApplication(), DomainBrowser.class, fieldValue);
                } else {
                    fieldValue = fieldValue.replaceAll("[^a-zA-Z0-9_\\*]", "");
                    EmbeddedApplication.open(getApplication(), DomainBrowser.class, null, null, fieldValue);
                }
            }
        }

        private class ViewAllDomainClassesButtonLink extends Button implements ClickListener {
            private ViewAllDomainClassesButtonLink() {
                super("View all domain classes");
                addListener((ClickListener) this);
                addStyleName(BaseTheme.BUTTON_LINK);
            }

            @Override
            public void buttonClick(final ClickEvent event) {
                EmbeddedApplication.open(getApplication(), DomainBrowser.class, null, "true");
            }
        }

        TextField textField = new TextField();

        public SearchPanel() {
            super();
            setSizeFull();
            setColumns(5);
            setRows(2);

            setMargin(true);
            setSpacing(true);

            Label browseObjectlabel = new Label("Search Class or Object");
            browseObjectlabel.setSizeUndefined();
            addComponent(browseObjectlabel, 0, 0);
            setComponentAlignment(browseObjectlabel, Alignment.MIDDLE_RIGHT);
            setColumnExpandRatio(0, 0);

            textField.setSizeUndefined();
            textField.focus();
            addComponent(textField, 1, 0);
            setColumnExpandRatio(1, 0);

            SearchButton browseObjectButton = new SearchButton();
            browseObjectButton.setClickShortcut(KeyCode.ENTER);
            browseObjectButton.setSizeUndefined();
            addComponent(browseObjectButton, 2, 0);
            setComponentAlignment(browseObjectButton, Alignment.MIDDLE_LEFT);
            setColumnExpandRatio(2, 0);

            ViewAllDomainClassesButtonLink browseClassesLink = new ViewAllDomainClassesButtonLink();
            browseClassesLink.setSizeUndefined();
            addComponent(browseClassesLink, 0, 1, 2, 1);

            Embedded icon = new Embedded(null, new ThemeResource("../../../images/BlueFenix.png"));
            icon.setSizeUndefined();
            addComponent(icon, 3, 0, 3, 1);
            setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
            setColumnExpandRatio(3, 55);

            Quote quote = new Quote();
            Label quoteText = new Label("<i>\"" + quote.getText() + "\"</i>", Label.CONTENT_XHTML);
            quoteText.setSizeUndefined();
            addComponent(quoteText, 4, 0);
            setComponentAlignment(quoteText, Alignment.MIDDLE_LEFT);

            Label quoteAuthor = new Label("-- " + quote.getAuthor());
            quoteAuthor.setSizeUndefined();
            addComponent(quoteAuthor, 4, 1);
            setComponentAlignment(quoteAuthor, Alignment.MIDDLE_RIGHT);
            setColumnExpandRatio(4, 45);
        }

        public void setInputValue(String value) {
            textField.setValue(value);
        }
    }

    public static class Quote {

        private static final long serialVersionUID = 1L;

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

                new String[] { "What we do in life echoes in eternity.", "Maximus (Gladiator)" },

                new String[] { "There is no spoon.", "Neo (The Matrix)" },
                new String[] { "Free your mind.", "Morpheus (The Matrix)" },
                new String[] { "Welcome to the real world.", "Morpheus (The Matrix)" },
                new String[] { "Never send a human to do a machine's job.", "Agent Smith (The Matrix)" },
                new String[] { "Ignorance is bliss.", "Cypher (The Matrix)" },
                new String[] { "This is my world! My world!", "Agent Smith (The Matrix)" } };

        private final String text;

        private final String author;

        public String getText() {
            return text;
        }

        public String getAuthor() {
            return author;
        }

        public Quote() {
            Random RANDOM = new Random(System.currentTimeMillis());
            String[] quote = QUOTES[RANDOM.nextInt(QUOTES.length)];

            text = quote[0];
            author = quote[1];
        }
    }
}
