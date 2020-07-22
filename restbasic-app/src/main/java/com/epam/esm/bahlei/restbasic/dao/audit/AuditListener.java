package com.epam.esm.bahlei.restbasic.dao.audit;

import com.epam.esm.bahlei.restbasic.model.audit.Audit;
import com.epam.esm.bahlei.restbasic.model.audit.Auditable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditListener {
  @PrePersist
  public void setCreatedAt(Auditable auditable) {
    Audit audit = auditable.getAudit();

    if (audit == null) {
      audit = new Audit();
      auditable.setAudit(audit);
    }

    audit.setCreatedAt(Instant.now());
    audit.setUpdatedAt(Instant.now());
  }

  @PreUpdate
  public void setUpdatedOn(Auditable auditable) {
    Audit audit = auditable.getAudit();

    audit.setUpdatedAt(Instant.now());
  }
}
