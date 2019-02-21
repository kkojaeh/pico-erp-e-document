package pico.erp.document.type;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface DocumentTypeEntityRepository extends CrudRepository<DocumentTypeEntity, DocumentTypeId> {

}

@Repository
@Transactional
public class DocumentTypeRepositoryJpa implements DocumentTypeRepository {

  @Autowired
  private DocumentTypeEntityRepository repository;

  @Autowired
  private DocumentTypeMapper mapper;

  @Override
  public DocumentType create(DocumentType documentType) {
    val entity = mapper.jpa(documentType);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(DocumentTypeId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(DocumentTypeId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<DocumentType> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
      .map(mapper::jpa);
  }

  @Override
  public Optional<DocumentType> findBy(DocumentTypeId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(DocumentType documentType) {
    val entity = repository.findOne(documentType.getId());
    mapper.pass(mapper.jpa(documentType), entity);
    repository.save(entity);
  }

}
