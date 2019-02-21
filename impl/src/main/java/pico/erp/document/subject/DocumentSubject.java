package pico.erp.document.subject;

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
public class DocumentSubject implements Serializable {

  private static final long serialVersionUID = 1L;

  DocumentSubjectId id;

  String name;

  String template;

  public DocumentSubjectMessages.Create.Response apply(
    DocumentSubjectMessages.Create.Request request) {
    this.id = request.getId();
    this.name = request.getName();
    return new DocumentSubjectMessages.Create.Response(
      Arrays.asList(new DocumentSubjectEvents.CreatedEvent(id))
    );
  }

  public DocumentSubjectMessages.Update.Response apply(
    DocumentSubjectMessages.Update.Request request) {
    this.name = request.getName();
    this.template = request.getTemplate();
    return new DocumentSubjectMessages.Update.Response(
      Arrays.asList(new DocumentSubjectEvents.UpdatedEvent(id))
    );
  }

}
