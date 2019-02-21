package pico.erp.document;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.document.type.DocumentTypeId;
import pico.erp.user.UserId;

@Data
public class DocumentData {

  DocumentId id;

  DocumentTypeId typeId;

  String name;

  UserId creatorId;

  OffsetDateTime createdDate;

}
