package pico.erp.document;


import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.document.storage.DocumentStorageKey;
import pico.erp.document.subject.DocumentSubjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.user.UserId;

@Entity(name = "Document")
@Table(name = "EDC_DOCUMENT")
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  DocumentId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "SUBJECT_ID", length = TypeDefinitions.ID_LENGTH))
  })
  DocumentSubjectId subjectId;

  @Column(length = TypeDefinitions.NAME_X2_LENGTH)
  String name;

  @Column(length = TypeDefinitions.CONTENT_TYPE_LENGTH)
  String contentType;

  long contentLength;

  @Column(length = TypeDefinitions.NAME_X2_LENGTH)
  String contentName;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "STORAGE_KEY", length = TypeDefinitions.ID_LENGTH))
  })
  DocumentStorageKey storageKey;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CREATOR_ID", length = TypeDefinitions.ID_LENGTH))
  })
  UserId creatorId;

  @Column(updatable = false)
  LocalDateTime createdDate;

}
