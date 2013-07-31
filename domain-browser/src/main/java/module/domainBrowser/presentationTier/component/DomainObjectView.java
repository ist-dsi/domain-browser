package module.domainBrowser.presentationTier.component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.presentationTier.component.links.DomainObjectLink;

import org.vaadin.vaadinvisualizations.OrganizationalChart;

import pt.ist.fenixframework.DomainMetaObject;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicateSystem;
import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicatesConfig;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;
import pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class DomainObjectView extends GridLayout {

    private final DomainClass domainClass;
    protected final DomainObject domainObject;

    protected static final String VALUE_COLUMN = "Value";
    protected static final String SLOT_COLUMN = "Slot Name";
    protected static final String PLAYS_ROLE_COLUMN = "PlaysRole Name";
    protected static final String TYPE_COLUMN = "Type";

    public DomainObjectView(DomainObject domainObject) {
        super(6, 100);
        setSpacing(true);
        setMargin(true);
        setSizeFull();
        
        this.domainObject = domainObject;
        domainClass = FenixFramework.getDomainModel().findClass(domainObject.getClass().getName());
    }

    @Override
    public void attach() {
        super.attach();

        String label = "<h3>Browsing object - " + domainObject.getClass().getName();
        label += ":" + domainObject.getExternalId() + "</h3>";
        addComponent(new Label(label, Label.CONTENT_XHTML), 0, 0, 5, 0);

        addChart();
        addValueSlots();
        addRelationSlots();
        addRelationSets();
        hideRelationContents();
        addConsistencyPredicates();
    }

    private void addChart() {
        final OrganizationalChart chart = new OrganizationalChart();
        chart.setOption("size", "medium");
        chart.setOption("allowCollapse", false);
        chart.setHeight(100, UNITS_PERCENTAGE);
        chart.setWidth(100, UNITS_PERCENTAGE);
        for (DomainClass dclass = domainClass; dclass != null; dclass = (DomainClass) dclass.getSuperclass()) {
            DomainClass parent = (DomainClass) dclass.getSuperclass();
            chart.add(dclass.getName(), parent == null ? "" : parent.getName(), dclass.getFullName());
        }
        chart.setSizeFull();
        chart.setVisible(true);
        addComponent(chart, 0, 1, 0, 2);
        setComponentAlignment(chart, Alignment.TOP_LEFT);
    }

    private void addValueSlots() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(SLOT_COLUMN, String.class, null);
        container.addContainerProperty(VALUE_COLUMN, String.class, null);
        container.addContainerProperty(TYPE_COLUMN, String.class, null);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("<h3>Value Slots</h3>", Label.CONTENT_XHTML));
        addComponent(layout, 1, 1, 5, 1);
        setComponentAlignment(layout, Alignment.TOP_CENTER);

        Table table = new Table();
        table.setSizeFull();
        table.setPageLength(0);
        table.setContainerDataSource(container);
        for (Slot slot : DomainUtils.getSlots(domainClass)) {
            Item item = table.addItem(slot.getName());
            Object value = DomainUtils.getSlot(domainObject, slot);
            item.getItemProperty(SLOT_COLUMN).setValue(slot.getName());
            item.getItemProperty(VALUE_COLUMN).setValue(value == null ? "null" : value.toString());
            item.getItemProperty(TYPE_COLUMN).setValue(slot.getTypeName());
        }
        layout.addComponent(table);
    }

    private void addRelationSlots() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(SLOT_COLUMN, String.class, null);
        container.addContainerProperty(VALUE_COLUMN, Link.class, null);
        container.addContainerProperty(TYPE_COLUMN, String.class, null);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("<h3>Relation Slots</h3>", Label.CONTENT_XHTML));
        addComponent(layout, 1, 2, 5, 2);
        setComponentAlignment(layout, Alignment.TOP_CENTER);

        Table table = new Table();
        table.setSizeFull();
        table.setPageLength(0);
        table.setContainerDataSource(container);
        for (Role role : DomainUtils.getRelationSlots(domainClass)) {
            Item item = table.addItem(role.getName());
            item.getItemProperty(SLOT_COLUMN).setValue(role.getName());
            item.getItemProperty(VALUE_COLUMN).setValue(new DomainObjectLink(domainObject, role));
            item.getItemProperty(TYPE_COLUMN).setValue(role.getType().getFullName());
        }
        layout.addComponent(table);
    }

    private class DomainRelationListView extends VerticalLayout {
        protected final Set<DomainObject> relationSet;
        protected final String title;

        public DomainRelationListView(final DomainObject domainObject, final Set<DomainObject> relationSet, String title) {
            this.relationSet = relationSet;
            this.title = title;
        }

        @Override
        public void attach() {
            super.attach();
            addComponent(new Label("<h3>" + title + "</h3>", Label.CONTENT_XHTML));

            IndexedContainer container = new IndexedContainer();
            container.addContainerProperty(VALUE_COLUMN, Link.class, null);
            container.addContainerProperty(TYPE_COLUMN, String.class, null);

            final Table table = new Table();
            table.setSizeFull();
            table.setPageLength(0);
            table.setContainerDataSource(container);

            for (final DomainObject domainObject : relationSet) {
                final Item item = table.addItem(domainObject.getExternalId());
                item.getItemProperty(VALUE_COLUMN).setValue(new DomainObjectLink(domainObject));
                item.getItemProperty(TYPE_COLUMN).setValue(domainObject.getClass().getName());
            }

            // a fixed height forces the table to use lazy-loading
            if (relationSet.size() > 20) {
                table.setHeight("300px");
            }

            addComponent(table);
        }
    }

    private void addRelationSets() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(PLAYS_ROLE_COLUMN, Button.class, null);
        container.addContainerProperty(TYPE_COLUMN, String.class, null);

        final VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("<h3>Relation Lists</h3>", Label.CONTENT_XHTML));
        addComponent(layout, 0, 3, 5, 3);
        setComponentAlignment(layout, Alignment.TOP_CENTER);

        final Table table = new Table();
        table.setSizeFull();
        table.setPageLength(0);
        table.setContainerDataSource(container);

        for (final Role role : DomainUtils.getRelationSets(domainClass)) {
            Button viewRelationButton = new Button(role.getName(), new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    showRelationContents(domainObject, DomainUtils.getRelationSet(domainObject, role.getName()), role.getName());
                }
            });
            viewRelationButton.addStyleName(BaseTheme.BUTTON_LINK);

            final Item item = table.addItem(role.getName());
            item.getItemProperty(PLAYS_ROLE_COLUMN).setValue(viewRelationButton);
            item.getItemProperty(TYPE_COLUMN).setValue(role.getType().getFullName());
        }
        layout.addComponent(table);
    }

    private void hideRelationContents() {
        removeComponent(0, 3);
        Label label = new Label("Pick a relation from the table above to view its contents");
        addComponent(label, 0, 4, 5, 4);
    }

    private void showRelationContents(DomainObject domainObject, Set<DomainObject> relationSet, String playsRole) {
        removeComponent(0, 3);
        DomainRelationListView relationViewer =
                new DomainRelationListView(domainObject, relationSet, "Contents of relation: " + playsRole);

        addComponent(relationViewer, 0, 4, 5, 4);
    }

    private void addConsistencyPredicates() {
        Label title = new Label("<h3>Consistency Predicates:</h3>", Label.CONTENT_XHTML);
        addComponent(title, 0, 5, 5, 5);

        int iteration = 0;
        for (Method predicate : ConsistencyPredicateSystem.getPredicatesFor(domainObject)) {
            Label predicateName =
                    new Label(predicate.getDeclaringClass().getName() + ".<b>" + predicate.getName() + "()</b>",
                            Label.CONTENT_XHTML);
            addComponent(predicateName, 0, 5 + iteration * 2, 4, 5 + iteration * 2);
            setComponentAlignment(predicateName, Alignment.BOTTOM_LEFT);

            Embedded predicateResult;
            try {
                predicate.setAccessible(true);
                Object result = predicate.invoke(domainObject);
                if ((Boolean) result) {
                    predicateResult = new Embedded(null, new ThemeResource("icons/accept.gif"));
                } else {
                    predicateResult = new Embedded(null, new ThemeResource("icons/incorrect.gif"));
                }
            } catch (Exception ex) {
                predicateResult = new Embedded(null, new ThemeResource("icons/incorrect.gif"));
            }
            addComponent(predicateResult, 5, 6 + iteration * 2, 5, 6 + iteration * 2);
            setComponentAlignment(predicateResult, Alignment.BOTTOM_LEFT);

            if (ConsistencyPredicatesConfig.canCreateDomainMetaObjects()) {
                DomainMetaObject metaObject = DomainMetaObject.getDomainMetaObjectFor(domainObject);
                DomainConsistencyPredicate consistencyPredicate =
                        DomainConsistencyPredicate.readDomainConsistencyPredicate(predicate);
                DomainDependenceRecord dependenceRecord = metaObject.getOwnDependenceRecord(consistencyPredicate);

                if (dependenceRecord != null && !dependenceRecord.getDependedDomainMetaObjectSet().isEmpty()) {
                    Set<DomainObject> dependedObjs = new HashSet<DomainObject>();
                    for (DomainMetaObject dependedMeta : dependenceRecord.getDependedDomainMetaObjectSet()) {
                        dependedObjs.add(dependedMeta.getDomainObject());
                    }

                    DomainRelationListView relationViewer = new DomainRelationListView(domainObject, dependedObjs, "Depends on:");
                    addComponent(relationViewer, 0, 7 + iteration * 2, 5, 7 + iteration * 2);
                }
            }

            iteration++;
        }
    }
}
