
package com.brassbullet.dbutils.xml.base;

import com.brassbullet.dbutils.xml.base.FieldBase;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class PrimaryKeyBase
    extends MarshallableObject
    implements Element
{

    private List _Fields = PredicatedLists.createInvalidating(this, new FieldsPredicate(), new ArrayList());
    private PredicatedLists.Predicate pred_Fields = new FieldsPredicate();

    public List getFields() {
        return _Fields;
    }

    public void deleteFields() {
        _Fields = null;
        invalidate();
    }

    public void emptyFields() {
        _Fields = PredicatedLists.createInvalidating(this, pred_Fields, new ArrayList());
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Fields == null) {
            throw new MissingContentException("Field");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        for (Iterator i = _Fields.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("PrimaryKey");
        for (Iterator i = _Fields.iterator(); i.hasNext(); ) {
            m.marshal(((MarshallableObject) i.next()));
        }
        w.end("PrimaryKey");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("PrimaryKey");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_Fields, new ArrayList());
            while (xs.atStart("Field")) {
                l.add(((FieldBase) u.unmarshal()));
            }
            _Fields = PredicatedLists.createInvalidating(this, pred_Fields, l);
        }
        xs.takeEnd("PrimaryKey");
    }

    public static PrimaryKeyBase unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static PrimaryKeyBase unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static PrimaryKeyBase unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((PrimaryKeyBase) d.unmarshal(xs, (PrimaryKeyBase.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof PrimaryKeyBase)) {
            return false;
        }
        PrimaryKeyBase tob = ((PrimaryKeyBase) ob);
        if (_Fields!= null) {
            if (tob._Fields == null) {
                return false;
            }
            if (!_Fields.equals(tob._Fields)) {
                return false;
            }
        } else {
            if (tob._Fields!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Fields!= null)?_Fields.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<PrimaryKey");
        if (_Fields!= null) {
            sb.append(" Field=");
            sb.append(_Fields.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return DatabaseBase.newDispatcher();
    }


    private static class FieldsPredicate
        implements PredicatedLists.Predicate
    {


        public void check(Object ob) {
            if (!(ob instanceof FieldBase)) {
                throw new InvalidContentObjectException(ob, (FieldBase.class));
            }
        }

    }

}
