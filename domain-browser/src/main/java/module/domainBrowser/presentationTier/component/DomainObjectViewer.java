package module.domainBrowser.presentationTier.component;

import java.util.SortedSet;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.presentationTier.component.links.RelationLink;
import module.domainBrowser.presentationTier.component.links.RoleLink;

import org.vaadin.vaadinvisualizations.OrganizationalChart;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import dml.DomainClass;
import dml.DomainModel;
import dml.Role;
import dml.Slot;

public class DomainObjectViewer extends BasicDomainObjectViewer {

    private static final long serialVersionUID = 1L;

    private class DomainObjectGrid extends GridLayout {

        private static final long serialVersionUID = 1L;

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
            super(5, 3);
            setSpacing(true);
            setMargin(true);
            setSizeFull();
        }

        @Override
        public void attach() {
            super.attach();
            addChart();
            addValueSlots();
            addRelationSlots();
            addRelationSets();
        }

        private void addValueSlots() {
            new GridPart<Slot>("Value Slots", 1, 0, 4, 0, DomainUtils.getSlots(domainClass), new SlotValueTypeContainer()) {
                @Override
                protected Object[] getValues(final Slot slot) {
                    final String name = slot.getName();
                    return new Object[] { name, SLOT_COLUMN, name, VALUE_COLUMN, getSlotValue(slot), TYPE_COLUMN,
                            slot.getTypeName() };
                }
            };
        }

        private void addRelationSlots() {
            new GridPart<Role>("Relation Slots", 1, 1, 4, 1, DomainUtils.getRelationSlots(domainClass),
                    new SlotDomainObjectContainer()) {
                @Override
                protected Object[] getValues(final Role role) {
                    final String name = role.getName();
                    return new Object[] { name, SLOT_COLUMN, name, VALUE_COLUMN, new RoleLink(domainObject, role), TYPE_COLUMN,
                            role.getType().getFullName() };
                }
            };
        }

        private void addRelationSets() {
            new GridPart<Role>("Relation Lists", 1, 2, 4, 2, DomainUtils.getRelationSets(domainClass),
                    new RelationListLinkContainer()) {
                @Override
                protected Object[] getValues(final Role role) {
                    return new Object[] { role.getName(), PLAYS_ROLE_COLUMN, new RelationLink(domainObject, role), TYPE_COLUMN,
                            role.getType().getFullName() };
                }
            };
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
            addComponent(oc, 0, 0, 0, 2);
            setComponentAlignment(oc, Alignment.TOP_LEFT);
        }

        private VerticalLayout addGridPart(final String label, int col1, int row1, int col2, int row2) {
            final VerticalLayout layout = new VerticalLayout();
            layout.addComponent(new Label("<h4>" + label + "</h4>", Label.CONTENT_XHTML));
            addComponent(layout, col1, row1, col2, row2);
            setComponentAlignment(layout, Alignment.TOP_CENTER);
            return layout;
        }

    }

    private final DomainClass domainClass;

    public DomainObjectViewer(final DomainObject domainObject) {
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
