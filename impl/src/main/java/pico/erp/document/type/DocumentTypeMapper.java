package pico.erp.document.type;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper
public abstract class DocumentTypeMapper {

  public DocumentType jpa(DocumentTypeEntity entity) {
    return DocumentType.builder()
      .id(entity.getId())
      .name(entity.getName())
      .template(entity.getTemplate())
      .build();
  }

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract DocumentTypeEntity jpa(DocumentType documentType);

  public abstract DocumentTypeData map(DocumentType documentType);

  public abstract DocumentTypeMessages.Update.Request map(
    DocumentTypeRequests.UpdateRequest request);

  public abstract void pass(DocumentTypeEntity from, @MappingTarget DocumentTypeEntity to);

}
