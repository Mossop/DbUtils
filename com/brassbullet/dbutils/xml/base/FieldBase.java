
package com.brassbullet.dbutils.xml.base;

import com.brassbullet.dbutils.xml.base.ReferenceBase;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class FieldBase
    extends MarshallableObject
    implements Element
{

    private boolean _AllowNull;
    private boolean isDefaulted_AllowNull = true;
    private final static boolean DEFAULT_ALLOWNULL = true;
    private String _Name;
    private String _Type;
    private String _Comment;
    private ReferenceBase _Reference;

    public boolean defaultedAllowNull() {
        return isDefaulted_AllowNull;
    }

    public boolean getAllowNull() {
        if (!isDefaulted_AllowNull) {
            return _AllowNull;
        }
        return DEFAULT_ALLOWNULL;
    }

    public void setAllowNull(boolean _AllowNull) {
        this._AllowNull = _AllowNull;
        isDefaulted_AllowNull = false;
        invalidate();
    }

    public boolean hasAllowNull() {
        return true;
    }

    public void deleteAllowNull() {
        isDefaulted_AllowNull = true;
        invalidate();
    }

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

    public String getComment() {
        return _Comment;
    }

    public void setComment(String _Comment) {
        this._Comment = _Comment;
        if (_Comment == null) {
            invalidate();
        }
    }

    public ReferenceBase getReference() {
        return _Reference;
    }

    public void setReference(ReferenceBase _Reference) {
        this._Reference = _Reference;
        if (_Reference == null) {
            invalidate();
        }
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
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        v.validate(_Reference);
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Field");
        if (!isDefaulted_AllowNull) {
            w.attribute("allowNull", printBoolean(_AllowNull));
        }
        w.attribute("name", _Name.toString());
        w.attribute("type", _Type.toString());
        if (_Comment!= null) {
            w.leaf("Comment", _Comment.toString());
        }
        if (_Reference!= null) {
            m.marshal(_Reference);
        }
        w.end("Field");
    }

    private static String printBoolean(boolean f) {
        return (f?"true":"false");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Field");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("allowNull")) {
                if (!isDefaulted_AllowNull) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _AllowNull = readBoolean(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                isDefaulted_AllowNull = false;
                continue;
            }
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
        if (xs.atStart("Reference")) {
            _Reference = ((ReferenceBase) u.unmarshal());
        }
        xs.takeEnd("Field");
    }

    private static boolean readBoolean(String s)
        throws ConversionException
    {
        if (s.equals("true")) {
            return true;
        }
        if (s.equals("false")) {
            return false;
        }
        throw new ConversionException(s);
    }

    public static FieldBase unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static FieldBase unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static FieldBase unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((FieldBase) d.unmarshal(xs, (FieldBase.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof FieldBase)) {
            return false;
        }
        FieldBase tob = ((FieldBase) ob);
        if (!isDefaulted_AllowNull) {
            if (tob.isDefaulted_AllowNull) {
                return false;
            }
            if (_AllowNull!= tob._AllowNull) {
                return false;
            }
        } else {
            if (!tob.isDefaulted_AllowNull) {
                return false;
            }
        }
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
        if (_Reference!= null) {
            if (tob._Reference == null) {
                return false;
            }
            if (!_Reference.equals(tob._Reference)) {
                return false;
            }
        } else {
            if (tob._Reference!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((31 *h)+(_AllowNull? 137 : 139));
        h = ((67 *h)+(isDefaulted_AllowNull? 59 : 61));
        h = ((127 *h)+((_Name!= null)?_Name.hashCode(): 0));
        h = ((127 *h)+((_Type!= null)?_Type.hashCode(): 0));
        h = ((127 *h)+((_Comment!= null)?_Comment.hashCode(): 0));
        h = ((127 *h)+((_Reference!= null)?_Reference.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Field");
        sb.append(" allowNull=");
        sb.append(printBoolean(getAllowNull()));
        if (_Name!= null) {
            sb.append(" name=");
            sb.append(_Name.toString());
        }
        if (_Type!= null) {
            sb.append(" type=");
            sb.append(_Type.toString());
        }
        if (_Comment!= null) {
            sb.append(" Comment=");
            sb.append(_Comment.toString());
        }
        if (_Reference!= null) {
            sb.append(" Reference=");
            sb.append(_Reference.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return DatabaseBase.newDispatcher();
    }

}
