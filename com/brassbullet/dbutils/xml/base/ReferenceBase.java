
package com.brassbullet.dbutils.xml.base;

import com.brassbullet.dbutils.xml.base.IntegrityActions;
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


public class ReferenceBase
    extends MarshallableObject
    implements Element
{

    private boolean _MustExist;
    private boolean isDefaulted_MustExist = true;
    private final static boolean DEFAULT_MUSTEXIST = true;
    private IntegrityActions _OnModify;
    private boolean isDefaulted_OnModify = true;
    private final static IntegrityActions DEFAULT_ONMODIFY = IntegrityActions.parse("cascade");
    private IntegrityActions _OnDelete;
    private boolean isDefaulted_OnDelete = true;
    private final static IntegrityActions DEFAULT_ONDELETE = IntegrityActions.parse("rollback");
    private String _Table;
    private String _Field;

    public boolean defaultedMustExist() {
        return isDefaulted_MustExist;
    }

    public boolean getMustExist() {
        if (!isDefaulted_MustExist) {
            return _MustExist;
        }
        return DEFAULT_MUSTEXIST;
    }

    public void setMustExist(boolean _MustExist) {
        this._MustExist = _MustExist;
        isDefaulted_MustExist = false;
        invalidate();
    }

    public boolean hasMustExist() {
        return true;
    }

    public void deleteMustExist() {
        isDefaulted_MustExist = true;
        invalidate();
    }

    public boolean defaultedOnModify() {
        return (_OnModify!= null);
    }

    public IntegrityActions getOnModify() {
        if (_OnModify == null) {
            return DEFAULT_ONMODIFY;
        }
        return _OnModify;
    }

    public void setOnModify(IntegrityActions _OnModify) {
        this._OnModify = _OnModify;
        if (_OnModify == null) {
            invalidate();
        }
    }

    public boolean defaultedOnDelete() {
        return (_OnDelete!= null);
    }

    public IntegrityActions getOnDelete() {
        if (_OnDelete == null) {
            return DEFAULT_ONDELETE;
        }
        return _OnDelete;
    }

    public void setOnDelete(IntegrityActions _OnDelete) {
        this._OnDelete = _OnDelete;
        if (_OnDelete == null) {
            invalidate();
        }
    }

    public String getTable() {
        return _Table;
    }

    public void setTable(String _Table) {
        this._Table = _Table;
        if (_Table == null) {
            invalidate();
        }
    }

    public String getField() {
        return _Field;
    }

    public void setField(String _Field) {
        this._Field = _Field;
        if (_Field == null) {
            invalidate();
        }
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Table == null) {
            throw new MissingAttributeException("table");
        }
        if (_Field == null) {
            throw new MissingAttributeException("field");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Reference");
        if (!isDefaulted_MustExist) {
            w.attribute("mustExist", printBoolean(_MustExist));
        }
        if (_OnModify!= null) {
            w.attribute("onModify", _OnModify.toString());
        }
        if (_OnDelete!= null) {
            w.attribute("onDelete", _OnDelete.toString());
        }
        w.attribute("table", _Table.toString());
        w.attribute("field", _Field.toString());
        w.end("Reference");
    }

    private static String printBoolean(boolean f) {
        return (f?"true":"false");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Reference");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("mustExist")) {
                if (!isDefaulted_MustExist) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _MustExist = readBoolean(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                isDefaulted_MustExist = false;
                continue;
            }
            if (an.equals("onModify")) {
                if (_OnModify!= null) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _OnModify = IntegrityActions.parse(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                continue;
            }
            if (an.equals("onDelete")) {
                if (_OnDelete!= null) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _OnDelete = IntegrityActions.parse(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                continue;
            }
            if (an.equals("table")) {
                if (_Table!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Table = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("field")) {
                if (_Field!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Field = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("Reference");
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

    public static ReferenceBase unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static ReferenceBase unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static ReferenceBase unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((ReferenceBase) d.unmarshal(xs, (ReferenceBase.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof ReferenceBase)) {
            return false;
        }
        ReferenceBase tob = ((ReferenceBase) ob);
        if (!isDefaulted_MustExist) {
            if (tob.isDefaulted_MustExist) {
                return false;
            }
            if (_MustExist!= tob._MustExist) {
                return false;
            }
        } else {
            if (!tob.isDefaulted_MustExist) {
                return false;
            }
        }
        if (_OnModify!= null) {
            if (tob._OnModify == null) {
                return false;
            }
            if (!_OnModify.equals(tob._OnModify)) {
                return false;
            }
        } else {
            if (tob._OnModify!= null) {
                return false;
            }
        }
        if (_OnDelete!= null) {
            if (tob._OnDelete == null) {
                return false;
            }
            if (!_OnDelete.equals(tob._OnDelete)) {
                return false;
            }
        } else {
            if (tob._OnDelete!= null) {
                return false;
            }
        }
        if (_Table!= null) {
            if (tob._Table == null) {
                return false;
            }
            if (!_Table.equals(tob._Table)) {
                return false;
            }
        } else {
            if (tob._Table!= null) {
                return false;
            }
        }
        if (_Field!= null) {
            if (tob._Field == null) {
                return false;
            }
            if (!_Field.equals(tob._Field)) {
                return false;
            }
        } else {
            if (tob._Field!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((31 *h)+(_MustExist? 137 : 139));
        h = ((67 *h)+(isDefaulted_MustExist? 59 : 61));
        h = ((127 *h)+((_OnModify!= null)?_OnModify.hashCode(): 0));
        h = ((127 *h)+((_OnDelete!= null)?_OnDelete.hashCode(): 0));
        h = ((127 *h)+((_Table!= null)?_Table.hashCode(): 0));
        h = ((127 *h)+((_Field!= null)?_Field.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Reference");
        sb.append(" mustExist=");
        sb.append(printBoolean(getMustExist()));
        sb.append(" onModify=");
        sb.append(getOnModify().toString());
        sb.append(" onDelete=");
        sb.append(getOnDelete().toString());
        if (_Table!= null) {
            sb.append(" table=");
            sb.append(_Table.toString());
        }
        if (_Field!= null) {
            sb.append(" field=");
            sb.append(_Field.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return DatabaseBase.newDispatcher();
    }

}
