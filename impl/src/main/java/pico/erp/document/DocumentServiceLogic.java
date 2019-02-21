package pico.erp.document;

import java.util.LinkedList;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.document.DocumentExceptions.NotFoundException;
import pico.erp.document.DocumentRequests.CreateRequest;
import pico.erp.document.DocumentRequests.DeleteRequest;
import pico.erp.document.storage.DocumentStorageStrategy;
import pico.erp.document.type.DocumentTypeRequests;
import pico.erp.document.type.DocumentTypeService;
import pico.erp.shared.Public;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

@Service
@Public
@Transactional
@Validated
public class DocumentServiceLogic implements DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  @Lazy
  @Autowired
  private DocumentStorageStrategy documentStorageStrategy;

  @Lazy
  @Autowired
  private DocumentTypeService documentTypeService;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private DocumentMapper mapper;

  @Override
  public DocumentData create(CreateRequest request) {
    val document = new Document();
    val events = new LinkedList<Event>();
    val response = document.apply(mapper.map(request));
    if (documentRepository.exists(document.getId())) {
      throw new DocumentExceptions.AlreadyExistsException();
    }
    val created = documentRepository.create(document);
    events.addAll(response.getEvents());

    val contentInputStream = documentTypeService.make(
      DocumentTypeRequests.MakeRequest.builder()
        .id(document.getTypeId())
        .key(request.getKey())
        .name(document.getName())
        .build()
    );
    val response2 = created.apply(
      DocumentMessages.Store.Request.builder()
        .contentInputStream(contentInputStream)
        .storageStrategy(documentStorageStrategy)
        .build()
    );
    documentRepository.update(created);
    events.addAll(response2.getEvents());
    eventPublisher.publishEvents(events);
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val document = documentRepository.findBy(request.getId())
      .orElseThrow(DocumentExceptions.NotFoundException::new);
    val response = document.apply(mapper.map(request));
    documentRepository.deleteBy(document.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public DocumentData get(DocumentId id) {
    return documentRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public ContentInputStream load(DocumentId id) {
    val document = documentRepository.findBy(id)
      .orElseThrow(DocumentExceptions.NotFoundException::new);
    return ContentInputStream.builder()
      .contentLength(document.getContentLength())
      .contentType(document.getContentType())
      .inputStream(documentStorageStrategy.load(document.getStorageKey()))
      .name(document.getContentName())
      .build();
  }

}
