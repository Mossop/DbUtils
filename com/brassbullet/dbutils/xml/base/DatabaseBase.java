
package com.brassbullet.dbutils.xml.base;

import com.brassbullet.dbutils.xml.base.TableBase;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.MarshallableRootElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.RootElement;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class DatabaseBase
    extends MarshallableRootElement
    implements RootElement
{

    private String _Name;
    private String _Type;
    private String _Url;
    private String _Author;
    private String _Version;
    private String _Date;
    private String _Comment;
    private List _Tables = PredicatedLists.createInvalidating(this, new TablesPredicate(), new ArrayList());
    private PredicatedLists.Predicate pred_Tables = new TablesPredicate();

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
        if (_Name == null) {
            invalidate();
        }
    }

    public String getType() {
        return _Type;
    }

    public void setType(String _Type) {
        this._Type = _Type;
        if (_Type == null) {
            invalidate();
        }
    }

    public String getUrl() {
        return _Url;
    }

    public void setUrl(String _Url) {
        this._Url = _Url;
        if (_Url == null) {
            invalidate();
        }
    }

    public String getAuthor() {
        return _Author;
    }

    public void setAuthor(String _Author) {
        this._Author = _Author;
        if (_Author == null) {
            invalidate();
        }
    }

    public String getVersion() {
        return _Version;
    }

    public void setVersion(String _Version) {
        this._Version = _Version;
        if (_Version == null) {
            invalidate();
        }
    }

    public String getDate() {
        return _Date;
    }

    public void setDate(String _Date) {
        this._Date = _Date;
        if (_Date == null) {
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

    public List getTables() {
        return _Tables;
    }

    public void deleteTables() {
        _Tables = null;
        invalidate();
    }

    public void emptyTables() {
        _Tables = PredicatedLists.createInvalidating(this, pred_Tables, new ArrayList());
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Name == null) {
            throw new MissingAttributeException("name");
        }
        if (_Type == null) {
            throw new MissingAttributeException("type");
        }
        if (_Url == null) {
            throw new MissingAttributeException("url");
        }
        if (_Tables == null) {
            throw new MissingContentException("Table");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        for (Iterator i = _Tables.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Database");
        w.attribute("name", _Name.toString());
        w.attribute("type", _Type.toString());
        w.attribute("url", _Url.toString());
        if (_Author!= null) {
            w.attribute("author", _Author.toString());
        }
        if (_Version!= null) {
            w.attribute("version", _Version.toString());
        }
        if (_Date!= null) {
            w.attribute("date", _Date.toString());
        }
        if (_Comment!= null) {
            w.leaf("Comment", _Comment.toString());
        }
        for (Iterator i = _Tables.iterator(); i.hasNext(); ) {
            m.marshal(((MarshallableObject) i.next()));
        }
        w.end("Database");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Database");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("name")) {
                if (_Name!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Name = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("type")) {
                if (_Type!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Type = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("url")) {
                if (_Url!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Url = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("author")) {
                if (_Author!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Author = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("version")) {
                if (_Version!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Version = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("date")) {
                if (_Date!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Date = xs.takeAttributeValue();
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
        {
            List l = PredicatedLists.create(this, pred_Tables, new ArrayList());
            while (xs.atStart("Table")) {
                l.add(((TableBase) u.unmarshal()));
            }
            _Tables = PredicatedLists.createInvalidating(this, pred_Tables, l);
        }
        xs.takeEnd("Database");
    }

    public static DatabaseBase unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static DatabaseBase unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static DatabaseBase unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((DatabaseBase) d.unmarshal(xs, (DatabaseBase.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof DatabaseBase)) {
            return false;
        }
        DatabaseBase tob = ((DatabaseBase) ob);
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
        if (_Type!= null) {
            if (tob._Type == null) {
                return false;
            }
            if (!_Type.equals(tob._Type)) {
                return false;
            }
        } else {
            if (tob._Type!= null) {
                return false;
            }
        }
        if (_Url!= null) {
            if (tob._Url == null) {
                return false;
            }
            if (!_Url.equals(tob._Url)) {
                return false;
            }
        } else {
            if (tob._Url!= null) {
                return false;
            }
        }
        if (_Author!= null) {
            if (tob._Author == null) {
                return false;
            }
            if (!_Author.equals(tob._Author)) {
                return false;
            }
        } else {
            if (tob._Author!= null) {
                return false;
            }
        }
        if (_Version!= null) {
            if (tob._Version == null) {
                return false;
            }
            if (!_Version.equals(tob._Version)) {
                return false;
            }
        } else {
            if (tob._Version!= null) {
                return false;
            }
        }
        if (_Date!= null) {
            if (tob._Date == null) {
                return false;
            }
            if (!_Date.equals(tob._Date)) {
                return false;
            }
        } else {
            if (tob._Date!= null) {
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
        if (_Tables!= null) {
            if (tob._Tables == null) {
                return false;
            }
            if (!_Tables.equals(tob._Tables)) {
                return false;
            }
        } else {
            if (tob._Tables!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Name!= null)?_Name.hashCode(): 0));
        h = ((127 *h)+((_Type!= null)?_Type.hashCode(): 0));
        h = ((127 *h)+((_Url!= null)?_Url.hashCode(): 0));
        h = ((127 *h)+((_Author!= null)?_Author.hashCode(): 0));
        h = ((127 *h)+((_Version!= null)?_Version.hashCode(): 0));
        h = ((127 *h)+((_Date!= null)?_Date.hashCode(): 0));
        h = ((127 *h)+((_Comment!= null)?_Comment.hashCode(): 0));
        h = ((127 *h)+((_Tables!= null)?_Tables.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Database");
        if (_Name!= null) {
            sb.append(" name=");
            sb.append(_Name.toString());
        }
        if (_Type!= null) {
            sb.append(" type=");
            sb.append(_Type.toString());
        }
        if (_Url!= null) {
            sb.append(" url=");
            sb.append(_Url.toString());
        }
        if (_Author!= null) {
            sb.append(" author=");
            sb.append(_Author.toString());
        }
        if (_Version!= null) {
            sb.append(" version=");
            sb.append(_Version.toString());
        }
        if (_Date!= null) {
            sb.append(" date=");
            sb.append(_Date.toString());
        }
        if (_Comment!= null) {
            sb.append(" Comment=");
            sb.append(_Comment.toString());
        }
        if (_Tables!= null) {
            sb.append(" Table=");
            sb.append(_Tables.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        Dispatcher d = new Dispatcher();
        d.register("Database", (DatabaseBase.class));
        d.register("Field", (FieldBase.class));
        d.register("PrimaryKey", (PrimaryKeyBase.class));
        d.register("Reference", (ReferenceBase.class));
        d.register("Table", (TableBase.class));
        d.freezeElementNameMap();
        return d;
    }


    private static class TablesPredicate
        implements PredicatedLists.Predicate
    {


        public void check(Object ob) {
            if (!(ob instanceof TableBase)) {
                throw new InvalidContentObjectException(ob, (TableBase.class));
            }
        }

    }

}
