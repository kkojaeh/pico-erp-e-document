package pico.erp.document.type;

import com.github.mustachejava.MustacheFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.document.DocumentInitializer.DocumentInitializable;
import pico.erp.document.maker.DocumentMakerDefinition;
import pico.erp.document.template.DocumentTemplateMustache;
import pico.erp.document.type.DocumentTypeRequests.MakeRequest;
import pico.erp.document.type.DocumentTypeRequests.TestRequest;
import pico.erp.shared.Public;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class DocumentTypeServiceLogic implements DocumentTypeService, DocumentInitializable {

  private final Map<DocumentTypeId, DocumentTypeDefinition> mapping = new HashMap<>();

  @Autowired
  private DocumentTypeRepository documentTypeRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private DocumentTypeMapper mapper;

  @Lazy
  @Autowired
  private List<DocumentTypeDefinition> definitions;

  @Autowired
  private MustacheFactory mustacheFactory;

  @Lazy
  @Autowired
  private DocumentMakerDefinition makerDefinition;

  @Override
  public boolean exists(DocumentTypeId id) {
    return documentTypeRepository.exists(id);
  }

  @Override
  public DocumentTypeData get(DocumentTypeId id) {
    return documentTypeRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(DocumentTypeExceptions.NotFoundException::new);
  }

  @Override
  public void initialize() {
    val targets = definitions.stream().collect(Collectors.toMap(d -> d.getId(), d -> d));
    mapping.putAll(
      definitions.stream()
        .collect(Collectors.toMap(d -> d.getId(), d -> d))
    );
    documentTypeRepository.findAll().forEach(documentType -> targets.remove(documentType.getId()));
    targets.values().forEach(definition -> {
      val documentType = new DocumentType();
      val request = DocumentTypeMessages.Create.Request.builder()
        .id(definition.getId())
        .name(definition.getName())
        .build();
      val response = documentType.apply(request);
      documentTypeRepository.create(documentType);
      eventPublisher.publishEvents(response.getEvents());
    });

  }

  @Override
  public ContentInputStream make(MakeRequest request) {
    val documentType = documentTypeRepository.findBy(request.getId())
      .orElseThrow(DocumentTypeExceptions.NotFoundException::new);
    val template = documentType.getTemplate();
    val context = mapping.get(request.getId()).createContext(request.getKey());
    val compiledMustache = mustacheFactory
      .compile(new StringReader(template), documentType.getId().getValue());
    val documentTemplate = new DocumentTemplateMustache(compiledMustache, context);
    return makerDefinition.make(request.getName(), documentTemplate);
  }

  @Override
  public ContentInputStream test(TestRequest request) {
    val documentType = documentTypeRepository.findBy(request.getId())
      .orElseThrow(DocumentTypeExceptions.NotFoundException::new);
    val template = Optional.ofNullable(request.getTemplate())
      .orElse("");
    val definition = mapping.get(request.getId());
    val key = definition.createKey(request.getKey());
    val context = definition.createContext(key);
    val compiledMustache = mustacheFactory
      .compile(new StringReader(template), documentType.getId().getValue());
    val documentTemplate = new DocumentTemplateMustache(compiledMustache, context);
    return makerDefinition.make("test", documentTemplate);
  }

  @Override
  public void update(DocumentTypeRequests.UpdateRequest request) {
    val documentType = documentTypeRepository.findBy(request.getId())
      .orElseThrow(DocumentTypeExceptions.NotFoundException::new);
    val response = documentType.apply(mapper.map(request));
    documentTypeRepository.update(documentType);
    eventPublisher.publishEvents(response.getEvents());
  }
}
