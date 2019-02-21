package pico.erp.document.subject;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper
public abstract class DocumentSubjectMapper {

  public DocumentSubject jpa(DocumentSubjectEntity entity) {
    return DocumentSubject.builder()
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
  public abstract DocumentSubjectEntity jpa(DocumentSubject documentSubject);

  public abstract DocumentSubjectData map(DocumentSubject documentSubject);

  public abstract DocumentSubjectMessages.Update.Request map(
    DocumentSubjectRequests.UpdateRequest request);

  public abstract void pass(DocumentSubjectEntity from, @MappingTarget DocumentSubjectEntity to);

}
