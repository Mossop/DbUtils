
package com.brassbullet.dbutils.xml.base;

import com.brassbullet.dbutils.xml.base.FieldBase;
import com.brassbullet.dbutils.xml.base.PrimaryKeyBase;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
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


public class TableBase
    extends MarshallableObject
    implements Element
{

    private String _Name;
    private String _Comment;
    private PrimaryKeyBase _PrimaryKey;
    private List _Fields = PredicatedLists.createInvalidating(this, new FieldsPredicate(), new ArrayList());
    private PredicatedLists.Predicate pred_Fields = new FieldsPredicate();

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
        if (_Name == null) {
            invalidate();
        }
    }

    public String getComment() {
        return _Comment;
    }

    public void setComment(String _Comment) {
        this._Comment = _Comment;
        if (_Comment == null) {
            invalidate();
        }
    }

    public PrimaryKeyBase getPrimaryKey() {
        return _PrimaryKey;
    }

    public void setPrimaryKey(PrimaryKeyBase _PrimaryKey) {
        this._PrimaryKey = _PrimaryKey;
        if (_PrimaryKey == null) {
            invalidate();
        }
    }

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
        if (_Name == null) {
            throw new MissingAttributeException("name");
        }
        if (_PrimaryKey == null) {
            throw new MissingContentException("PrimaryKey");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        v.validate(_PrimaryKey);
        for (Iterator i = _Fields.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Table");
        w.attribute("name", _Name.toString());
        if (_Comment!= null) {
            w.leaf("Comment", _Comment.toString());
        }
        m.marshal(_PrimaryKey);
        if (_Fields.size()> 0) {
            for (Iterator i = _Fields.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("Table");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Table");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("name")) {
                if (_Name!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Name = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        if (xs.atStart("Comment")) {
            xs.takeStart("Comment");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Comment = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Comment", x);
            }
            xs.takeEnd("Comment");
        }
        _PrimaryKey = ((PrimaryKeyBase) u.unmarshal());
        {
            List l = PredicatedLists.create(this, pred_Fields, new ArrayList());
            while (xs.atStart("Field")) {
                l.add(((FieldBase) u.unmarshal()));
            }
            _Fields = PredicatedLists.createInvalidating(this, pred_Fields, l);
        }
        xs.takeEnd("Table");
    }

    public static TableBase unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static TableBase unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static TableBase unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((TableBase) d.unmarshal(xs, (TableBase.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof TableBase)) {
            return false;
        }
        TableBase tob = ((TableBase) ob);
        if (_Name!= null) {
            if (tob._Name == null) {
                return false;
            }
            if (!_Name.equals(tob._Name)) {
                return false;
            }
        } else {
            if (tob._Name!= null) {
                return false;
            }
        }
        if (_Comment!= null) {
            if (tob._Comment == null) {
                return false;
            }
            if (!_Comment.equals(tob._Comment)) {
                return false;
            }
        } else {
            if (tob._Comment!= null) {
                return false;
            }
        }
        if (_PrimaryKey!= null) {
            if (tob._PrimaryKey == null) {
                return false;
            }
            if (!_PrimaryKey.equals(tob._PrimaryKey)) {
                return false;
            }
        } else {
            if (tob._PrimaryKey!= null) {
                return false;
            }
        }
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
        h = ((127 *h)+((_Name!= null)?_Name.hashCode(): 0));
        h = ((127 *h)+((_Comment!= null)?_Comment.hashCode(): 0));
        h = ((127 *h)+((_PrimaryKey!= null)?_PrimaryKey.hashCode(): 0));
        h = ((127 *h)+((_Fields!= null)?_Fields.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Table");
        if (_Name!= null) {
            sb.append(" name=");
            sb.append(_Name.toString());
        }
        if (_Comment!= null) {
            sb.append(" Comment=");
            sb.append(_Comment.toString());
        }
        if (_PrimaryKey!= null) {
            sb.append(" PrimaryKey=");
            sb.append(_PrimaryKey.toString());
        }
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
