
package org.openmrs.module.patientdatatransfer.web.model;

/**
 * Created by Muthoni on 03/06/15.
 *
 */

import java.util.Date;
import org.openmrs.module.patientdatatransfer.web.Util;

public class PBaseData {
    public Integer id = null;

    public String uuid = null;

    public Integer creatorId = null;

    public Date dateCreated = null;

    public Integer changedById = null;

    public Date dateChanged = null;

    public Boolean voided  = null;

    public Date dateVoided= null;

    public Integer voidedById = null;

    public String voidReason =null;

    protected String formatDate(Date date) {
        return Util.formatDate(date);
    }

    protected Date parseDate(String date) {
        return Util.parseDate(date);
    }

    protected boolean isEmpty(String cell) {
        return Util.isEmpty(cell);
    }

    protected boolean isNotEmpty(String remainingExpression) {
        return Util.isNotEmpty(remainingExpression);
    }

}
