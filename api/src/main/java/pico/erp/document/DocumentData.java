package pico.erp.document;

import java.time.LocalDateTime;
import lombok.Data;
import pico.erp.document.subject.DocumentSubjectId;
import pico.erp.user.UserId;

@Data
public class DocumentData {

  DocumentId id;

  DocumentSubjectId subjectId;

  String name;

  UserId creatorId;

  LocalDateTime createdDate;

}
