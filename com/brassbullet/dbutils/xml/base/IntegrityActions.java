
package com.brassbullet.dbutils.xml.base;

import javax.xml.bind.IllegalEnumerationValueException;


public final class IntegrityActions {

    private String _IntegrityActions;
    public final static IntegrityActions CASCADE = new IntegrityActions("cascade");
    public final static IntegrityActions ROLLBACK = new IntegrityActions("rollback");
    public final static IntegrityActions NOTHING = new IntegrityActions("nothing");
    public final static IntegrityActions NULL = new IntegrityActions("null");

    private IntegrityActions(String s) {
        this._IntegrityActions = s;
    }

    public static IntegrityActions parse(String s) {
        if (s.equals("cascade")) {
            return CASCADE;
        }
        if (s.equals("nothing")) {
            return NOTHING;
        }
        if (s.equals("null")) {
            return NULL;
        }
        if (s.equals("rollback")) {
            return ROLLBACK;
        }
        throw new IllegalEnumerationValueException(s);
    }

    public String toString() {
        return _IntegrityActions;
    }

}
