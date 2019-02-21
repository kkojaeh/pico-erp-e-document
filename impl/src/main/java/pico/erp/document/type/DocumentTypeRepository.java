package pico.erp.document.type;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface DocumentTypeRepository {

  DocumentType create(DocumentType documentType);

  void deleteBy(DocumentTypeId id);

  boolean exists(DocumentTypeId id);

  Stream<DocumentType> findAll();

  Optional<DocumentType> findBy(@NotNull DocumentTypeId id);

  void update(@NotNull DocumentType documentType);

}
