package pico.erp.document.subject;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface DocumentSubjectEntityRepository extends
  CrudRepository<DocumentSubjectEntity, DocumentSubjectId> {

}

@Repository
@Transactional
public class DocumentSubjectRepositoryJpa implements DocumentSubjectRepository {

  @Autowired
  private DocumentSubjectEntityRepository repository;

  @Autowired
  private DocumentSubjectMapper mapper;

  @Override
  public DocumentSubject create(DocumentSubject documentSubject) {
    val entity = mapper.jpa(documentSubject);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(DocumentSubjectId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(DocumentSubjectId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<DocumentSubject> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
      .map(mapper::jpa);
  }

  @Override
  public Optional<DocumentSubject> findBy(DocumentSubjectId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(DocumentSubject documentSubject) {
    val entity = repository.findOne(documentSubject.getId());
    mapper.pass(mapper.jpa(documentSubject), entity);
    repository.save(entity);
  }

}
