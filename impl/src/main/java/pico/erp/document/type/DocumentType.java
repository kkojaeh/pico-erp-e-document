package pico.erp.document.type;

import java.io.Serializable;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentType implements Serializable {

  private static final long serialVersionUID = 1L;

  DocumentTypeId id;

  String name;

  String template;

  public DocumentTypeMessages.Create.Response apply(
    DocumentTypeMessages.Create.Request request) {
    this.id = request.getId();
    this.name = request.getName();
    return new DocumentTypeMessages.Create.Response(
      Arrays.asList(new DocumentTypeEvents.CreatedEvent(id))
    );
  }

  public DocumentTypeMessages.Update.Response apply(
    DocumentTypeMessages.Update.Request request) {
    this.name = request.getName();
    this.template = request.getTemplate();
    return new DocumentTypeMessages.Update.Response(
      Arrays.asList(new DocumentTypeEvents.UpdatedEvent(id))
    );
  }

}
