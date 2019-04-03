package pico.erp.document.subject;

import com.github.mustachejava.MustacheFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.ComponentAutowired;
import kkojaeh.spring.boot.component.ComponentBean;
import kkojaeh.spring.boot.component.SpringBootComponentReadyEvent;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.document.maker.DocumentMakerDefinition;
import pico.erp.document.subject.DocumentSubjectRequests.MakeRequest;
import pico.erp.document.subject.DocumentSubjectRequests.TestRequest;
import pico.erp.document.template.DocumentTemplateMustache;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class DocumentSubjectServiceLogic implements DocumentSubjectService,
  ApplicationListener<SpringBootComponentReadyEvent> {

  private final Map<DocumentSubjectId, DocumentSubjectDefinition> mapping = new HashMap<>();

  @Autowired
  private DocumentSubjectRepository documentSubjectRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private DocumentSubjectMapper mapper;

  @ComponentAutowired(required = false)
  private List<DocumentSubjectDefinition> definitions;

  @Autowired
  private MustacheFactory mustacheFactory;

  @ComponentAutowired(required = false)
  private DocumentMakerDefinition makerDefinition;

  @Override
  public boolean exists(DocumentSubjectId id) {
    return documentSubjectRepository.exists(id);
  }

  @Override
  public DocumentSubjectData get(DocumentSubjectId id) {
    return documentSubjectRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(DocumentSubjectExceptions.NotFoundException::new);
  }

  @Override
  public ContentInputStream make(MakeRequest request) {
    val documentType = documentSubjectRepository.findBy(request.getId())
      .orElseThrow(DocumentSubjectExceptions.NotFoundException::new);
    val template = Optional.ofNullable(documentType.getTemplate())
      .orElse("");
    val context = mapping.get(request.getId()).getContext(request.getKey());
    val compiledMustache = mustacheFactory
      .compile(new StringReader(template), documentType.getId().getValue());
    val documentTemplate = new DocumentTemplateMustache(compiledMustache, context);
    return makerDefinition.make(request.getName(), documentTemplate);
  }

  @Override
  public void onApplicationEvent(SpringBootComponentReadyEvent event) {
    val targets = definitions.stream().collect(Collectors.toMap(d -> d.getId(), d -> d));
    mapping.putAll(
      definitions.stream()
        .collect(Collectors.toMap(d -> d.getId(), d -> d))
    );
    documentSubjectRepository.findAll()
      .forEach(documentType -> targets.remove(documentType.getId()));
    targets.values().forEach(definition -> {
      val documentType = new DocumentSubject();
      val request = DocumentSubjectMessages.Create.Request.builder()
        .id(definition.getId())
        .name(definition.getName())
        .build();
      val response = documentType.apply(request);
      documentSubjectRepository.create(documentType);
      eventPublisher.publishEvents(response.getEvents());
    });
  }

  @Override
  public ContentInputStream test(TestRequest request) {
    val documentType = documentSubjectRepository.findBy(request.getId())
      .orElseThrow(DocumentSubjectExceptions.NotFoundException::new);
    val template = Optional.ofNullable(request.getTemplate())
      .orElse("");
    val definition = mapping.get(request.getId());
    val key = definition.getKey(request.getKey());
    val context = definition.getContext(key);
    val compiledMustache = mustacheFactory
      .compile(new StringReader(template), documentType.getId().getValue());
    val documentTemplate = new DocumentTemplateMustache(compiledMustache, context);
    return makerDefinition.make("test", documentTemplate);
  }

  @Override
  public void update(DocumentSubjectRequests.UpdateRequest request) {
    val documentType = documentSubjectRepository.findBy(request.getId())
      .orElseThrow(DocumentSubjectExceptions.NotFoundException::new);
    val response = documentType.apply(mapper.map(request));
    documentSubjectRepository.update(documentType);
    eventPublisher.publishEvents(response.getEvents());
  }
}
