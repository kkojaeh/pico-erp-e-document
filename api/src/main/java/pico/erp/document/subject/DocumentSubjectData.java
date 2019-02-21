package pico.erp.document.subject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentSubjectData {

  DocumentSubjectId id;

  String name;

  String template;

}
