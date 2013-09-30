package module.domainBrowser.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainMetaClass;
import pt.ist.fenixframework.DomainMetaObject;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.consistencyPredicates.DomainConsistencyPredicate;
import pt.ist.fenixframework.consistencyPredicates.DomainDependenceRecord;
import pt.ist.fenixframework.consistencyPredicates.PublicConsistencyPredicate;
import pt.ist.fenixframework.core.AbstractDomainObject;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

public class DomainUtils {

    public static final Comparator<Slot> SLOT_COMPARATOR = new Comparator<Slot>() {
        @Override
        public int compare(final Slot s1, final Slot s2) {
            return s1.getName().compareTo(s2.getName());
        }
    };

    public static final Comparator<Role> RELATION_SLOT_COMPARATOR = new Comparator<Role>() {
        @Override
        public int compare(final Role r1, final Role r2) {
            return compareRoleNames(r1, r2);
        }
    };

    private static int compareRoleNames(Role r1, Role r2) {
        if ((r1.getName() == null) && (r2.getName() != null)) {
            return -1;
        }
        if ((r1.getName() != null) && (r2.getName() == null)) {
            return 1;
        }
        if ((r1.getName() == null) && (r2.getName() == null)) {
            if (r1.getOtherRole().getName() == null || r2.getOtherRole().getName() == null) {
                throw new RuntimeException("Found a domain relation without any playsRole?!");
            }
            return compareRoleNames(r1.getOtherRole(), r2.getOtherRole());
        }
        return r1.getName().compareTo(r2.getName());
    }

    public static DomainObject readDomainObject(final String externalId) {
        if (externalId == null || externalId.isEmpty() || !StringUtils.isNumeric(externalId)) {
            return null;
        }
        AbstractDomainObject object = FenixFramework.getDomainObject(externalId);
        try {
            DomainMetaObject.getDomainMetaObjectFor(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return object;
    }

    public static Object getSlot(final DomainObject domainObject, final Slot slot) {
        final Method method = getMethod(domainObject, slot);
        if (method != null) {
            try {
                return method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    public static DomainObject getRelationSlot(final DomainObject domainObject, final Role role) {
        final Method method = getMethod(domainObject, role);
        if (method != null) {
            try {
                return (DomainObject) method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Set<DomainObject> getRelationSet(final DomainObject domainObject, final String slotName) {
        final Method method = getMethod(domainObject, getterMethod(slotName));
        if (method != null) {
            try {
                return (Set<DomainObject>) method.invoke(domainObject);
            } catch (final IllegalAccessException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            } catch (final InvocationTargetException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    protected static Method getMethod(final DomainObject domainObject, final Slot slot) {
        return getMethod(domainObject, getterMethod(slot));
    }

    protected static Method getMethod(final DomainObject domainObject, final Role role) {
        return getMethod(domainObject, getterMethod(role));
    }

    protected static Method getMethod(final DomainObject domainObject, final String slotName) {
        if (domainObject != null && slotName != null && !slotName.isEmpty()) {
            final Class<? extends DomainObject> clazz = domainObject.getClass();
            try {
                return clazz.getMethod(slotName);
            } catch (final NoSuchMethodException e) {
                throw new Error(e);
            } catch (final SecurityException e) {
                throw new Error(e);
            } catch (final IllegalArgumentException e) {
                throw new Error(e);
            }
        }
        return null;
    }

    protected static String getterMethod(final String slotName) {
        return slotName == null || slotName.isEmpty() ? null : "get" + StringUtils.capitalize(slotName);
    }

    protected static String getterMethod(final Slot slot) {
        return getterMethod(slot.getName());
    }

    protected static String getterMethod(final Role role) {
        return getterMethod(role.getName());
    }

    public static SortedSet<Slot> getSlots(final DomainClass domainClass) {
        final SortedSet<Slot> result = new TreeSet<Slot>(DomainUtils.SLOT_COMPARATOR);
        for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
            for (final Slot slot : dc.getSlotsList()) {
                result.add(slot);
            }
        }
        return result;
    }

    public static SortedSet<Role> getRelationSlots(final DomainClass domainClass) {
        final SortedSet<Role> result = new TreeSet<Role>(RELATION_SLOT_COMPARATOR);
        for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
            for (final Role role : dc.getRoleSlotsList()) {
                //Roles without name are unidirectional and should be skipped
                if (role.getName() == null) {
                    continue;
                }
                if (role.getMultiplicityUpper() == 1) {
                    result.add(role);
                }
            }
        }
        return result;
    }

    public static SortedSet<Role> getRelationSets(final DomainClass domainClass) {
        final SortedSet<Role> result = new TreeSet<Role>(RELATION_SLOT_COMPARATOR);
        for (DomainClass dc = domainClass; dc != null; dc = (DomainClass) dc.getSuperclass()) {
            for (final Role role : dc.getRoleSlotsList()) {
                //Roles without name are unidirectional and should be skipped
                if (role.getName() == null) {
                    continue;
                }
                if (role.getMultiplicityUpper() != 0 && role.getMultiplicityUpper() != 1) {
                    result.add(role);
                }
            }
        }
        return result;
    }

    public static DomainMetaClass[] getAllMetaSubClasses(DomainMetaClass metaClass) {
        Collection<DomainMetaClass> allMetaSubClasses = getAllMetaSubClassesCollection(metaClass);
        return allMetaSubClasses.toArray(new DomainMetaClass[allMetaSubClasses.size()]);
    }

    private static Collection<DomainMetaClass> getAllMetaSubClassesCollection(DomainMetaClass metaClass) {
        Collection<DomainMetaClass> allMetaClasses = new HashSet<DomainMetaClass>();
        allMetaClasses.add(metaClass);
        for (DomainMetaClass metaSubclass : metaClass.getDomainMetaSubclassSet()) {
            allMetaClasses.addAll(getAllMetaSubClassesCollection(metaSubclass));
        }
        return allMetaClasses;
    }

    public static int getInconsistencyCount(DomainMetaClass metaClass) {
        int inconsistencies = 0;
        for (DomainConsistencyPredicate predicate : metaClass.getAllConsistencyPredicates()) {
            if (isInherited(predicate, metaClass)) {
                // For inherited predicates, count only the inconsistent objects that belong to the current metaClass hierarchy
                for (DomainDependenceRecord inconsistentRecord : predicate.getInconsistentDependenceRecordSet()) {
                    if (metaClass.getDomainClass().isAssignableFrom(inconsistentRecord.getDependent().getClass())) {
                        inconsistencies++;
                    }
                }
            } else {
                // Declared predicates can only affect objects of the current metaClass hierarchy
                inconsistencies += predicate.getInconsistentDependenceRecordSet().size();
            }
        }
        return inconsistencies;
    }

    public static Collection<DomainMetaObject> getInconsistentObjects(DomainMetaClass metaClass) {
        Collection<DomainMetaObject> inconsistentObjects = new HashSet<DomainMetaObject>();
        for (DomainConsistencyPredicate predicate : metaClass.getAllConsistencyPredicates()) {
            if (isInherited(predicate, metaClass)) {
                // For inherited predicates, count only the inconsistent objects that belong to the current metaClass hierarchy
                for (DomainDependenceRecord inconsistentRecord : predicate.getInconsistentDependenceRecordSet()) {
                    if (metaClass.getDomainClass().isAssignableFrom(inconsistentRecord.getDependent().getClass())) {
                        inconsistentObjects.add(inconsistentRecord.getDependentDomainMetaObject());
                    }
                }
            } else {
                // Declared predicates can only affect objects of the current metaClass hierarchy
                for (DomainDependenceRecord dependenceRecord : predicate.getInconsistentDependenceRecordSet()) {
                    inconsistentObjects.add(dependenceRecord.getDependentDomainMetaObject());
                }
            }
        }
        return inconsistentObjects;
    }

    public static boolean isInherited(DomainConsistencyPredicate predicate, DomainMetaClass metaClass) {
        if (metaClass.getDomainMetaSuperclass() == null) {
            return false;
        }
        if (metaClass.getDomainMetaSuperclass().getAllConsistencyPredicates().contains(predicate)) {
            return true;
        }
        return false;
    }

    public static int getAffectedObjectsCount(DomainConsistencyPredicate predicate) {
        int objectCount = 0;
        for (DomainMetaClass metaClass : getAffectedMetaClasses(predicate, predicate.getDomainMetaClass())) {
            objectCount += metaClass.getExistingDomainMetaObjectsCount();
        }
        return objectCount;
    }

    private static Collection<DomainMetaClass> getAffectedMetaClasses(DomainConsistencyPredicate predicate,
            DomainMetaClass metaClass) {
        Collection<DomainMetaClass> affectedClasses =
                new TreeSet<DomainMetaClass>(DomainMetaClass.COMPARATOR_BY_META_CLASS_HIERARCHY_TOP_DOWN);

        if (predicate instanceof PublicConsistencyPredicate) {
            if (metaClass == predicate.getDomainMetaClass()) {
                // The metaClass is this very predicate's declaring class, so it is not a subclass yet.
                affectedClasses.add(metaClass);
            } else {
                try {
                    metaClass.getDomainClass().getDeclaredMethod(predicate.getPredicate().getName());
                    // If no exception was thrown, the method is being overridden from this class downwards,
                    // so stop and don't search in subclasses.
                    return affectedClasses;
                } catch (NoSuchMethodException e) {
                    // The method is not being overridden here, so include this class and search for subclasses.
                    affectedClasses.add(metaClass);
                }
            }
        } else {
            affectedClasses.add(metaClass);
        }

        for (DomainMetaClass subclass : metaClass.getDomainMetaSubclassSet()) {
            affectedClasses.addAll(getAffectedMetaClasses(predicate, subclass));
        }

        return affectedClasses;
    }

    public static class DomainObjectLink extends Link {
        private static final long serialVersionUID = 1L;

        private DomainObject domainObject;

        public DomainObjectLink(DomainObject domainObject) {
            this.domainObject = domainObject;
        }

        @Override
        public void attach() {
            super.attach();
            if (domainObject != null) {
                String externalId = domainObject.getExternalId();
                setCaption(externalId);
                setResource(new ExternalResource("vaadinContext.do?method=forwardToVaadin#DomainBrowser?externalId=" + externalId));
            }
        }
    }

    public static class DomainClassLink extends Link {
        private static final long serialVersionUID = 1L;

        private DomainMetaClass metaClass;

        public DomainClassLink(DomainMetaClass metaClass) {
            this.metaClass = metaClass;
        }

        @Override
        public void attach() {
            super.attach();
            if (metaClass != null) {
                String className = metaClass.getDomainClass().getName();
                setCaption(className);
                setResource(new ExternalResource("vaadinContext.do?method=forwardToVaadin#DomainBrowser?className=" + className));
            }
        }
    }
}
