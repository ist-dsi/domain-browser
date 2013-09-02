package module.domainBrowser.presentationTier.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import module.domainBrowser.domain.DomainUtils;
import module.domainBrowser.domain.DomainUtils.DomainClassLink;
import module.domainBrowser.domain.DomainUtils.DomainObjectLink;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.DomainMetaObject;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;
import pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord;

import com.google.common.collect.Iterators;
import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DomainClassView extends GridLayout {

    private class MetaObjectCollectionWrapper implements Collection<DomainMetaObject> {

        private final DomainMetaClass metaClass;

        private MetaObjectCollectionWrapper(DomainMetaClass metaClass) {
            this.metaClass = metaClass;
        }

        @Override
        public int size() {
            return DomainUtils.getObjectCountIncludingSubclasses(metaClass);
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
            return contains(metaClass, (DomainMetaObject) obj);
        }

        private boolean contains(DomainMetaClass metaClass, DomainMetaObject metaObject) {
            if (metaClass.getExistingDomainMetaObjects().contains(metaObject.getExternalId())) {
                return true;
            }

            for (DomainMetaClass metaSubclass : metaClass.getDomainMetaSubclassSet()) {
                if (contains(metaSubclass, metaObject)) {
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
            return iterator(metaClass);
        }

        private Iterator<DomainMetaObject> iterator(DomainMetaClass metaClass) {
            Iterator<DomainMetaObject> iterator = metaClass.getExistingDomainMetaObjects().iterator();
            for (DomainMetaClass metaSubclass : metaClass.getDomainMetaSubclassSet()) {
                iterator = Iterators.concat(iterator, iterator(metaSubclass));
            }
            return iterator;
        }

        public Collection<DomainMetaObject> first(int size) {
            Collection<DomainMetaObject> firstObjs = new HashSet<DomainMetaObject>();
            for (DomainMetaObject metaObject : this) {
                if (firstObjs.size() >= size) {
                    break;
                }
                firstObjs.add(metaObject);
            }

            return firstObjs;
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

    public DomainClassView(final DomainMetaClass metaClass) {
        super(3, 100);
        setSpacing(true);
        setSizeFull();

        addComponent(new Label(metaClass.getDomainClass().getPackage().toString()), 0, 0, 2, 0);

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setSpacing(true);
        titleLayout.setSizeFull();
        Label titleLabel = new Label("<h2>class " + metaClass.getDomainClass().getSimpleName() + "</h2>", Label.CONTENT_XHTML);
        titleLabel.setSizeUndefined();
        titleLayout.addComponent(titleLabel);
        if (metaClass.getDomainMetaSuperclass() != null) {
            Label extendsLabel = new Label("extends");
            extendsLabel.setSizeUndefined();
            titleLayout.addComponent(extendsLabel);
            titleLayout.setComponentAlignment(extendsLabel, Alignment.MIDDLE_RIGHT);
            DomainClassLink superClassLink = new DomainClassLink(metaClass.getDomainMetaSuperclass());
            superClassLink.setSizeUndefined();
            titleLayout.addComponent(superClassLink);
            titleLayout.setComponentAlignment(superClassLink, Alignment.MIDDLE_LEFT);
        }
        addComponent(titleLayout, 0, 1, 2, 1);

        Label objectsTitle = new Label("<h3>Objects</h3>", Label.CONTENT_XHTML);
        addComponent(objectsTitle, 0, 2, 2, 2);

        addComponent(new Label(DomainUtils.getObjectCountIncludingSubclasses(metaClass) + " total"), 0, 3, 0, 3);

        addComponent(new Label(metaClass.getExistingDomainMetaObjectsCount() + " excluding subclasses"), 1, 3, 1, 3);

        addComponent(new Label(DomainUtils.getInconsistencyCount(metaClass) + " inconsistent"), 2, 3, 2, 3);

        final MetaObjectCollectionWrapper existingObjects = new MetaObjectCollectionWrapper(metaClass);
        final BeanItemContainer<DomainMetaObject> container = new BeanItemContainer<DomainMetaObject>(DomainMetaObject.class);
        //Clear all properties; use only the table's generated columns
        container.getContainerPropertyIds().clear();
        container.addAll(existingObjects.first(25));

        final VerticalLayout objectsLayout = new VerticalLayout();
        final PagedTable objectsTable = new PagedTable();
        objectsTable.setSizeFull();
        objectsTable.setPageLength(existingObjects.first(25).size());
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
        addComponent(objectsLayout, 0, 4, 2, 4);

        Thread thread = new Thread() {
            @Override
            @Atomic(mode = Atomic.TxMode.READ)
            public void run() {
                // force the iteration through all the existingObjects to pre-load them in memory
                // to reduce the contention time inside the syncronized block
                for (DomainMetaObject metaObject : existingObjects) {
                    ;
                }
                synchronized (getApplication()) {
                    if (existingObjects.size() > 25) {
                        container.addAll(existingObjects);
                        HorizontalLayout controls = objectsTable.createControls();
                        controls.removeComponent(controls.getComponent(0)); //Remove page length control
                        objectsLayout.addComponent(controls);
                    }
                    objectsLayout.removeComponent(progress);
                }
            }
        };

        addComponent(new Label("<br/><h3>Consistency Predicates</h3>", Label.CONTENT_XHTML), 0, 5, 2, 5);
        addComponent(new Label(metaClass.getAllConsistencyPredicates().size() + " total (including inherited)"), 0, 6, 0, 6);
        addComponent(new Label(metaClass.getDeclaredConsistencyPredicateSet().size() + " declared"), 1, 6, 1, 6);
        addComponent(new Label(getConsistencyPredicatesWithInconsistencies(metaClass).size() + " inconsistent"), 2, 6, 2, 6);

        int iteration = 0;
        for (DomainConsistencyPredicate predicate : metaClass.getAllConsistencyPredicates()) {
            Label predicateName =
                    new Label(predicate.getDomainMetaClass().getDomainClass().getName() + ".<b>"
                            + predicate.getPredicate().getName() + "()</b>", Label.CONTENT_XHTML);
            addComponent(predicateName, 0, 7 + iteration, 2, 7 + iteration);
            setComponentAlignment(predicateName, Alignment.BOTTOM_LEFT);
            iteration++;
        }

        thread.start();
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
