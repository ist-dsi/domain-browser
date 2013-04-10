package module.domainBrowser.presentationTier.component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.SortedSet;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.presentationTier.component.links.DomainObjectLink;

import org.vaadin.vaadinvisualizations.OrganizationalChart;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.consistencyPredicates.ConsistencyPredicateSystem;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import dml.DomainClass;
import dml.DomainModel;
import dml.Role;
import dml.Slot;

@SuppressWarnings("serial")
public class DomainObjectView extends BasicDomainObjectView {

    private class DomainObjectGrid extends GridLayout {

        protected abstract class GridPart<T> {
            protected GridPart(final String caption, final int col1, final int row1, final int col2, final int row2,
                    final SortedSet<T> set, final IndexedContainer container) {
                final VerticalLayout slotLayout = addGridPart(caption, col1, row1, col2, row2);
                final Table table = createTable(set.size(), container);
                for (final T t : set) {
                    final Object[] o = getValues(t);
                    final Item item = table.addItem(o[0]);
                    for (int i = 1; i < o.length; i += 2) {
                        item.getItemProperty(o[i]).setValue(o[i + 1]);
                    }
                }
                slotLayout.addComponent(table);
            }

            protected abstract Object[] getValues(final T t);

        }

        private DomainObjectGrid() {
            super(6, 100);
            setSpacing(true);
            setMargin(true);
            setSizeFull();
        }

        private VerticalLayout addGridPart(final String label, int col1, int row1, int col2, int row2) {
            final VerticalLayout layout = new VerticalLayout();
            layout.addComponent(new Label("<h3>" + label + "</h3>", Label.CONTENT_XHTML));
            addComponent(layout, col1, row1, col2, row2);
            setComponentAlignment(layout, Alignment.TOP_CENTER);
            return layout;
        }

        @Override
        public void attach() {
            super.attach();
            addChart();
            addValueSlots();
            addRelationSlots();
            addRelationSets();
            hideRelationContents();
            addConsistencyPredicates();
        }

        private void addChart() {
            final OrganizationalChart oc = new OrganizationalChart();
            oc.setOption("size", "medium");
            oc.setOption("allowCollapse", false);
            oc.setHeight(100, UNITS_PERCENTAGE);
            oc.setWidth(100, UNITS_PERCENTAGE);
            for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
                final DomainClass parent = (DomainClass) dc.getSuperclass();
                oc.add(dc.getName(), parent == null ? "" : parent.getName(), dc.getFullName());
            }
            oc.setSizeFull();
            oc.setVisible(true);
            addComponent(oc, 0, 0, 0, 1);
            setComponentAlignment(oc, Alignment.TOP_LEFT);
        }

        private void addValueSlots() {
            new GridPart<Slot>("Value Slots", 1, 0, 5, 0, DomainUtils.getSlots(domainClass), new SlotValueTypeContainer()) {
                @Override
                protected Object[] getValues(final Slot slot) {
                    final String name = slot.getName();
                    return new Object[] { name, SLOT_COLUMN, name, VALUE_COLUMN, getSlotValue(slot), TYPE_COLUMN,
                            slot.getTypeName() };
                }
            };
        }

        private void addRelationSlots() {
            new GridPart<Role>("Relation Slots", 1, 1, 5, 1, DomainUtils.getRelationSlots(domainClass),
                    new SlotDomainObjectContainer()) {
                @Override
                protected Object[] getValues(final Role role) {
                    final String name = role.getName();
                    return new Object[] { name, SLOT_COLUMN, name, VALUE_COLUMN, new DomainObjectLink(domainObject, role),
                            TYPE_COLUMN, role.getType().getFullName() };
                }
            };
        }

        private void addRelationSets() {
            new GridPart<Role>("Relation Lists", 0, 2, 5, 2, DomainUtils.getRelationSets(domainClass),
                    new RelationListButtonContainer()) {
                @Override
                protected Object[] getValues(final Role role) {
                    Button viewRelationButton = new Button(role.getName(), new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            showRelationContents(domainObject, DomainUtils.getRelationSet(domainObject, role.getName()),
                                    role.getName());
                        }
                    });
                    viewRelationButton.addStyleName(BaseTheme.BUTTON_LINK);

                    return new Object[] { role.getName(), PLAYS_ROLE_COLUMN, viewRelationButton, TYPE_COLUMN,
                            role.getType().getFullName() };
                }
            };
        }

        private void hideRelationContents() {
            removeComponent(0, 3);
            Label label = new Label("Pick a relation from the table above to view its contents");
            addComponent(label, 0, 3, 5, 3);
        }

        private void showRelationContents(final DomainObject domainObject, final Set<DomainObject> relationSet, String playsRole) {
            removeComponent(0, 3);
            DomainRelationListView relationViewer = new DomainRelationListView(domainObject, relationSet, playsRole);
            addComponent(relationViewer, 0, 3, 5, 3);
        }

        private void addConsistencyPredicates() {
            Label title = new Label("<h3>Consistency Predicates:</h3>", Label.CONTENT_XHTML);
            addComponent(title, 0, 4, 5, 4);

            int iteration = 0;
            for (Method predicate : ConsistencyPredicateSystem.getPredicatesFor(domainObject)) {
                Label className = new Label(predicate.getDeclaringClass().getName() + ".");
                className.setSizeUndefined();
                addComponent(className, 0, 5 + iteration, 1, 5 + iteration);
                setComponentAlignment(className, Alignment.MIDDLE_RIGHT);
                Label predicateName = new Label("<b>" + predicate.getName() + "()</b>", Label.CONTENT_XHTML);
                addComponent(predicateName, 2, 5 + iteration, 4, 5 + iteration);

                Embedded personalTooltip;
                try {
                    predicate.setAccessible(true);
                    Object result = predicate.invoke(domainObject);
                    if ((Boolean) result) {
                        personalTooltip = new Embedded(null, new ThemeResource("icons/accept.gif"));
                    } else {
                        personalTooltip = new Embedded(null, new ThemeResource("icons/incorrect.gif"));
                    }
                } catch (Exception ex) {
                    personalTooltip = new Embedded(null, new ThemeResource("icons/incorrect.gif"));
                }
                addComponent(personalTooltip, 5, 5 + iteration, 5, 5 + iteration);

                iteration++;
            }
        }
    }

    private final DomainClass domainClass;

    public DomainObjectView(final DomainObject domainObject) {
        super(domainObject);
        final DomainModel domainModel = FenixFramework.getDomainModel();
        domainClass = domainModel.findClass(domainObject.getClass().getName());
    }

    @Override
    public void attach() {
        super.attach();
        final DomainObjectGrid grid = new DomainObjectGrid();
        addComponent(grid);
        setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
    }

    private String getSlotValue(final Slot slot) {
        final Object value = DomainUtils.getSlot(domainObject, slot);
        return value == null ? "null" : value.toString();
    }

}
