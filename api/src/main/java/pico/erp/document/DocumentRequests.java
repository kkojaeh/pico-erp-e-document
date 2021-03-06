package pico.erp.document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.document.subject.DocumentSubjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.user.UserId;

public interface DocumentRequests {


  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CreateRequest<K> {

    @Valid
    @NotNull
    DocumentId id;

    @Valid
    @NotNull
    DocumentSubjectId subjectId;

    @NotNull
    @Size(max = TypeDefinitions.NAME_X2_LENGTH)
    String name;

    @Valid
    @NotNull
    K key;

    @Valid
    @NotNull
    UserId creatorId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    DocumentId id;

  }


}
