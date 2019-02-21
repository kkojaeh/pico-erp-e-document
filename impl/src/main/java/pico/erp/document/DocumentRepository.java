package pico.erp.document;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface DocumentRepository {

  Document create(@NotNull Document document);

  void deleteBy(@NotNull DocumentId id);

  boolean exists(@NotNull DocumentId id);

  Optional<Document> findBy(@NotNull DocumentId id);

  void update(@NotNull Document document);


}
