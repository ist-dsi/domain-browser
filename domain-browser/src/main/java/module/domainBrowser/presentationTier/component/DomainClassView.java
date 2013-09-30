package module.domainBrowser.presentationTier.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.domain.DomainUtils.DomainClassLink;
import module.domainBrowser.domain.DomainUtils.DomainObjectLink;

import org.vaadin.vaadinvisualizations.OrganizationalChart;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.DomainMetaObject;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;
import pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord;
import pt.ist.vaadinframework.EmbeddedApplication;

import com.google.common.collect.Iterators;
import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DomainClassView extends GridLayout {

    private final DomainMetaClass metaClass;

    protected static class MetaObjectCollectionWrapper implements Collection<DomainMetaObject> {

        private final DomainMetaClass[] metaClasses;

        protected MetaObjectCollectionWrapper(DomainMetaClass... metaClasses) {
            this.metaClasses = metaClasses;
        }

        @Override
        public int size() {
            int objectCount = 0;
            for (DomainMetaClass metaClass : metaClasses) {
                objectCount += metaClass.getExistingDomainMetaObjectsCount();
            }
            return objectCount;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean contains(Object obj) {
            if (!(obj instanceof DomainMetaObject)) {
                return false;
            }
            for (DomainMetaClass metaClass : metaClasses) {
                if (metaClass.getExistingDomainMetaObjects().contains(((DomainMetaObject) obj).getExternalId())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> coll) {
            for (Object obj : coll) {
                if (!contains(obj)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Iterator<DomainMetaObject> iterator() {
            Iterator<DomainMetaObject> iterator = Iterators.emptyIterator();
            for (DomainMetaClass metaClass : metaClasses) {
                iterator = Iterators.concat(iterator, metaClass.getExistingDomainMetaObjects().iterator());
            }
            return iterator;
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean add(DomainMetaObject e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends DomainMetaObject> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    public DomainClassView(DomainMetaClass metaClass) {
        super(3, 100);
        setSpacing(true);
        setSizeFull();
        this.metaClass = metaClass;
    }

    @Override
    public void attach() {
        super.attach();

        addHeader();
        addChart();
        addObjects();
        addPredicates();
    }

    private void addHeader() {
        addComponent(new Label(metaClass.getDomainClass().getPackage().toString()), 0, 0, 2, 0);

        Label titleLabel = new Label("<h2>class " + metaClass.getDomainClass().getSimpleName() + "</h2>", Label.CONTENT_XHTML);
        titleLabel.setSizeUndefined();
        addComponent(titleLabel, 0, 1, 2, 1);
    }

    private void addChart() {
        Panel panel = new Panel();
        panel.setSizeFull();

        OrganizationalChart chart = new OrganizationalChart();
        final Map<String, String> fullClassNameMap = new TreeMap<String, String>();
        chart.setOption("size", "medium");

        int levelCount = 1;
        DomainMetaClass metaSuperclass = metaClass.getDomainMetaSuperclass();
        String className = "> " + metaClass.getDomainClass().getSimpleName() + " <";
        String fullClassName = metaClass.getDomainClass().getName();
        chart.add(className, metaSuperclass == null ? "" : metaSuperclass.getDomainClass().getSimpleName(), fullClassName);
        fullClassNameMap.put(className, fullClassName);
        if (metaSuperclass != null) {
            className = metaSuperclass.getDomainClass().getSimpleName();
            fullClassName = metaSuperclass.getDomainClass().getName();
            chart.add(className, "", fullClassName);
            fullClassNameMap.put(className, fullClassName);
            levelCount++;
        }
        if (!metaClass.getDomainMetaSubclassSet().isEmpty()) {
            levelCount++;
            for (DomainMetaClass metaSubclass : metaClass.getDomainMetaSubclassSet()) {
                className = metaSubclass.getDomainClass().getSimpleName();
                fullClassName = metaSubclass.getDomainClass().getName();
                chart.add(className, "> " + metaClass.getDomainClass().getSimpleName() + " <", fullClassName);
                fullClassNameMap.put(className, fullClassName);
            }
        }

        // The inner layout of the panel needs an undefined size to allow horizontal scroll
        panel.getContent().setSizeUndefined();
        // The inner layout of the panel must have a shorter height than the panel itself
        // to make space for the horizontal scroll bar 
        panel.setHeight(20 + 70 * levelCount, UNITS_PIXELS);
        panel.getContent().setHeight(70 * levelCount, UNITS_PIXELS);

        chart.setHeight(70 * levelCount, UNITS_PIXELS);
        if (metaClass.getDomainMetaSubclassSet().isEmpty()) {
            chart.setWidth(230, UNITS_PIXELS);
        } else {
            chart.setWidth(230 * metaClass.getDomainMetaSubclassSet().size(), UNITS_PIXELS);
        }
        panel.setScrollable(true);
        panel.setScrollLeft(Math.round((chart.getWidth() / 3)));

        chart.addListener(new OrganizationalChart.SelectionListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChanged(List<String> selectedItems) {
                if (!selectedItems.isEmpty()) {
                    EmbeddedApplication.open(getApplication(), DomainBrowser.class, null, null, null,
                            fullClassNameMap.get(selectedItems.get(0)));
                }
            }
        });

        panel.addComponent(chart);
        addComponent(panel, 0, 2, 2, 2);
    }

    private static Component notInitializedObjectsLayout = new VerticalLayout();
    private static Component notInitializedInconsistenciesLayout = new VerticalLayout();

    private void addObjects() {
        addComponent(new Label("<h3>Objects</h3>", Label.CONTENT_XHTML), 0, 3, 2, 3);
        final TabSheet tabs = new TabSheet();
        MetaObjectCollectionWrapper allObjects = new MetaObjectCollectionWrapper(DomainUtils.getAllMetaSubClasses(metaClass));
        tabs.addTab(createObjectsLayout(allObjects), allObjects.size() + " total");
        tabs.addTab(notInitializedObjectsLayout, metaClass.getExistingDomainMetaObjectsCount() + " excluding subclasses");
        tabs.addTab(notInitializedInconsistenciesLayout, DomainUtils.getInconsistencyCount(metaClass) + " inconsistent");
        addComponent(tabs, 0, 4, 2, 4);

        tabs.addListener(new TabSheet.SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                if (tabs.getSelectedTab().equals(notInitializedObjectsLayout)) {
                    tabs.removeComponent(notInitializedObjectsLayout);
                    tabs.addTab(createObjectsLayout(new MetaObjectCollectionWrapper(metaClass)),
                            metaClass.getExistingDomainMetaObjectsCount() + " excluding subclasses", null, 1);
                    tabs.setSelectedTab(1);
                }
                if (tabs.getSelectedTab().equals(notInitializedInconsistenciesLayout)) {
                    tabs.removeComponent(notInitializedInconsistenciesLayout);
                    tabs.addTab(createObjectsLayout(DomainUtils.getInconsistentObjects(metaClass)),
                            DomainUtils.getInconsistencyCount(metaClass) + " inconsistent", null, 2);
                    tabs.setSelectedTab(2);
                }
            }
        });
    }

    private VerticalLayout createObjectsLayout(final Collection<DomainMetaObject> objects) {
        final BeanItemContainer<DomainMetaObject> container = new BeanItemContainer<DomainMetaObject>(DomainMetaObject.class);
        //Clear all properties; use only the table's generated columns
        container.getContainerPropertyIds().clear();
        container.addAll(first(objects, 25));

        final VerticalLayout objectsLayout = new VerticalLayout();
        final PagedTable objectsTable = new PagedTable();
        objectsTable.setSizeFull();
        objectsTable.setPageLength(first(objects, 25).size());
        objectsTable.setContainerDataSource(container);
        objectsTable.addGeneratedColumn("Object", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return new DomainObjectLink(((DomainMetaObject) itemId).getDomainObject());
            }
        });
        objectsTable.addGeneratedColumn("Type", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return new DomainClassLink(((DomainMetaObject) itemId).getDomainMetaClass());
            }
        });
        objectsLayout.addComponent(objectsTable);
        final ProgressIndicator progress = new ProgressIndicator();
        progress.setIndeterminate(true);
        progress.setEnabled(true);
        objectsLayout.addComponent(progress);
        objectsLayout.setComponentAlignment(progress, Alignment.MIDDLE_CENTER);

        Thread thread = new Thread() {
            @Override
            @Atomic(mode = Atomic.TxMode.READ)
            public void run() {
                // force the iteration through all the existingObjects to pre-load them in memory
                // to reduce the contention time inside the syncronized block
                for (DomainMetaObject metaObject : objects) {
                    ;
                }
                synchronized (getApplication()) {
                    if (objects.size() > 25) {
                        container.addAll(objects);
                        HorizontalLayout controls = objectsTable.createControls();
                        controls.removeComponent(controls.getComponent(0)); //Remove page length control
                        objectsLayout.addComponent(controls);
                    }
                    objectsLayout.removeComponent(progress);
                }
            }
        };
        thread.start();

        return objectsLayout;
    }

    public static Collection<DomainMetaObject> first(Collection<DomainMetaObject> objects, int size) {
        Collection<DomainMetaObject> firstObjs = new HashSet<DomainMetaObject>();
        for (DomainMetaObject metaObject : objects) {
            if (firstObjs.size() >= size) {
                break;
            }
            firstObjs.add(metaObject);
        }

        return firstObjs;
    }

    private void addPredicates() {
        addComponent(new Label("<br/><h3>Consistency Predicates</h3>", Label.CONTENT_XHTML), 0, 5, 2, 5);
        TabSheet tabs = new TabSheet();
        Collection<DomainConsistencyPredicate> allPredicates = metaClass.getAllConsistencyPredicates();
        Collection<DomainConsistencyPredicate> declaredPredicates = metaClass.getDeclaredConsistencyPredicateSet();
        Collection<DomainConsistencyPredicate> inconsistentPredicates = getConsistencyPredicatesWithInconsistencies(metaClass);
        tabs.addTab(createPredicatesTable(allPredicates), allPredicates.size() + " total (including inherited)");
        tabs.addTab(createPredicatesTable(declaredPredicates), declaredPredicates.size() + " declared");
        tabs.addTab(createPredicatesTable(inconsistentPredicates), inconsistentPredicates.size() + " inconsistent");
        addComponent(tabs, 0, 6, 2, 6);
    }

    private Table createPredicatesTable(Collection<DomainConsistencyPredicate> predicates) {
        final BeanItemContainer<DomainConsistencyPredicate> container =
                new BeanItemContainer<DomainConsistencyPredicate>(DomainConsistencyPredicate.class, predicates);
        //Clear all properties; use only the table's generated columns
        container.getContainerPropertyIds().clear();
        Table predicatesTable = new Table();
        predicatesTable.setSizeFull();
        predicatesTable.setPageLength((container.size() > 15) ? 15 : 0);
        predicatesTable.setContainerDataSource(container);
        predicatesTable.addGeneratedColumn("Predicate", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                DomainConsistencyPredicate predicate = (DomainConsistencyPredicate) itemId;
                Label label =
                        new Label(predicate.getPredicate().getDeclaringClass().getName() + ".<b>"
                                + predicate.getPredicate().getName() + "()</b>", Label.CONTENT_XHTML);
                label.setSizeUndefined();
                return label;
            }
        });
        predicatesTable.addGeneratedColumn("Affected Objects", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return DomainUtils.getAffectedObjectsCount((DomainConsistencyPredicate) itemId);
            }
        });
        predicatesTable.addGeneratedColumn("Inconsistent Objects", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return ((DomainConsistencyPredicate) itemId).getInconsistentDependenceRecordSet().size();
            }
        });
        return predicatesTable;
    }

    private static Collection<DomainConsistencyPredicate> getConsistencyPredicatesWithInconsistencies(DomainMetaClass metaClass) {
        Collection<DomainConsistencyPredicate> predicatesWithInconsistencies = new ArrayList<DomainConsistencyPredicate>();
        for (DomainConsistencyPredicate predicate : metaClass.getAllConsistencyPredicates()) {
            if (DomainUtils.isInherited(predicate, metaClass)) {
                // For inherited predicates, count them only if at least one of their inconsistent objects
                // belongs to the current metaClass hierarchy
                for (DomainDependenceRecord inconsistentRecord : predicate.getInconsistentDependenceRecordSet()) {
                    if (metaClass.getDomainClass().isAssignableFrom(inconsistentRecord.getDependent().getClass())) {
                        predicatesWithInconsistencies.add(predicate);
                        break;
                    }
                }
            } else {
                // Declared predicates can only affect objects of the current metaClass hierarchy
                if (predicate.getInconsistentDependenceRecordSet().size() > 0) {
                    predicatesWithInconsistencies.add(predicate);
                }
            }
        }
        return predicatesWithInconsistencies;
    }
}
