package pico.erp.document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.document.storage.DocumentStorageKey;
import pico.erp.document.subject.DocumentSubjectId;
import pico.erp.user.UserId;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString
public class Document implements Serializable, DocumentInfo {

  private static final long serialVersionUID = 1L;

  @Id
  DocumentId id;

  DocumentSubjectId subjectId;

  String name;

  UserId creatorId;

  LocalDateTime createdDate;

  DocumentStorageKey storageKey;

  String contentType;

  long contentLength;

  String contentName;

  public DocumentMessages.Create.Response apply(DocumentMessages.Create.Request request) {
    id = request.getId();
    name = request.getName();
    subjectId = request.getSubjectId();
    creatorId = request.getCreatorId();
    createdDate = LocalDateTime.now();
    return new DocumentMessages.Create.Response(
      Arrays.asList(new DocumentEvents.CreatedEvent(id))
    );
  }

  public DocumentMessages.Store.Response apply(DocumentMessages.Store.Request request) {
    val content = request.getContentInputStream();
    contentLength = content.getContentLength();
    contentType = content.getContentType();
    contentName = content.getName();
    storageKey = request.getStorageStrategy().save(this, request.getContentInputStream());
    return new DocumentMessages.Store.Response(
      Arrays.asList(new DocumentEvents.StoredEvent(id))
    );
  }

  public DocumentMessages.Delete.Response apply(DocumentMessages.Delete.Request request) {
    request.getStorageStrategy().remove(storageKey);
    return new DocumentMessages.Delete.Response(
      Arrays.asList(new DocumentEvents.DeletedEvent(this.id))
    );
  }

}
