package pico.erp.document;

import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface DocumentEntityRepository extends CrudRepository<DocumentEntity, DocumentId> {

}

@Repository
@Transactional
public class DocumentRepositoryJpa implements DocumentRepository {

  @Autowired
  private DocumentEntityRepository repository;

  @Autowired
  private DocumentMapper mapper;

  @Override
  public Document create(Document document) {
    val entity = mapper.entity(document);
    val created = repository.save(entity);
    return mapper.domain(created);
  }

  @Override
  public void deleteBy(DocumentId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(DocumentId id) {
    return repository.existsById(id);
  }

  @Override
  public Optional<Document> findBy(DocumentId id) {
    return repository.findById(id)
      .map(mapper::domain);
  }

  @Override
  public void update(Document document) {
    val entity = repository.findById(document.getId()).get();
    mapper.pass(mapper.entity(document), entity);
    repository.save(entity);
  }
}
