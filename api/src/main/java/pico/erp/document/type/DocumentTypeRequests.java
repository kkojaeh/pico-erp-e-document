package pico.erp.document.type;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;

public interface DocumentTypeRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    DocumentTypeId id;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    DocumentTypeId id;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @NotNull
    @Size(max = TypeDefinitions.CLOB_LENGTH)
    String template;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class MakeRequest<K> {

    @Valid
    @NotNull
    DocumentTypeId id;

    @Valid
    @NotNull
    String name;

    @Valid
    @NotNull
    K key;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class TestRequest {

    @Valid
    @NotNull
    DocumentTypeId id;

    @Valid
    @NotNull
    String key;

    @Size(max = TypeDefinitions.CLOB_LENGTH)
    String template;

  }

}
