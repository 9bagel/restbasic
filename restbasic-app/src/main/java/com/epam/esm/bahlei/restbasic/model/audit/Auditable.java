package com.epam.esm.bahlei.restbasic.model.audit;

public interface Auditable {

    Audit getAudit();

    void setAudit(Audit audit);
}
