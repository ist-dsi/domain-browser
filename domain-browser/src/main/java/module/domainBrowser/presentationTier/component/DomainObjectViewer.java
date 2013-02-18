package module.domainBrowser.presentationTier.component;

import java.util.Set;
import java.util.SortedSet;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.presentationTier.component.links.DomainObjectLink;

import org.vaadin.vaadinvisualizations.OrganizationalChart;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
public class DomainObjectViewer extends BasicDomainObjectViewer {

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
            super(5, 5);
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
            hideRelationContents();
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
                    return new Object[] { name, SLOT_COLUMN, name, VALUE_COLUMN, new DomainObjectLink(domainObject, role),
                            TYPE_COLUMN, role.getType().getFullName() };
                }
            };
        }

        private void addRelationSets() {
            new GridPart<Role>("Relation Lists", 0, 2, 4, 2, DomainUtils.getRelationSets(domainClass),
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
            Label label = (Label) getComponent(0, 3);
            if (label == null) {
                label = new Label();
                addComponent(label, 0, 3, 4, 3);
            }
            label.setCaption("Pick a relation from the table above to view its contents.");

            // remove the DomainRelationSetViewer
            removeComponent(0, 4);
        }

        private void showRelationContents(final DomainObject domainObject, final Set<DomainObject> relationSet, String playsRole) {
            Label label = (Label) getComponent(0, 3);
            label.setCaption("Relation of playsRole: " + playsRole);

            removeComponent(0, 4);
            DomainRelationListViewer relationViewer = new DomainRelationListViewer(domainObject, relationSet, playsRole);
            addComponent(relationViewer, 0, 4, 4, 4);
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
