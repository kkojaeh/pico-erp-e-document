package pico.erp.document.subject;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface DocumentSubjectRepository {

  DocumentSubject create(DocumentSubject documentSubject);

  void deleteBy(DocumentSubjectId id);

  boolean exists(DocumentSubjectId id);

  Stream<DocumentSubject> findAll();

  Optional<DocumentSubject> findBy(@NotNull DocumentSubjectId id);

  void update(@NotNull DocumentSubject documentSubject);

}
