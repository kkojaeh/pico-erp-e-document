package pico.erp.document.type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentTypeData {

  DocumentTypeId id;

  String name;

  String template;

}
