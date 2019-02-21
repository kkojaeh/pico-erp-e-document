package pico.erp.document;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.document.subject.DocumentSubjectId;
import pico.erp.user.UserId;

@Data
public class DocumentData {

  DocumentId id;

  DocumentSubjectId subjectId;

  String name;

  UserId creatorId;

  OffsetDateTime createdDate;

}
