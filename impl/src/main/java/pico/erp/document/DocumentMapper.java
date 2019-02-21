package pico.erp.document;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.document.storage.DocumentStorageStrategy;

@Mapper
public abstract class DocumentMapper {

  @Lazy
  @Autowired
  protected DocumentStorageStrategy documentStorageStrategy;

  @Lazy
  @Autowired
  protected DocumentRepository documentRepository;


  public Document domain(DocumentEntity entity) {
    return Document.builder()
      .id(entity.getId())
      .name(entity.getName())
      .typeId(entity.getTypeId())
      .creatorId(entity.getCreatorId())
      .createdDate(entity.getCreatedDate())
      .contentLength(entity.getContentLength())
      .contentType(entity.getContentType())
      .contentName(entity.getContentName())
      .storageKey(entity.getStorageKey())
      .build();
  }

  public abstract DocumentMessages.Create.Request map(DocumentRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "storageStrategy", expression = "java(documentStorageStrategy)")
  })
  public abstract DocumentMessages.Delete.Request map(DocumentRequests.DeleteRequest request);

  public abstract DocumentData map(Document document);

  public abstract DocumentEntity entity(Document document);

  public abstract void pass(DocumentEntity from, @MappingTarget DocumentEntity to);


}
