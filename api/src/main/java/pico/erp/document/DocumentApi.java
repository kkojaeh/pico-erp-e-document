package pico.erp.document;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

public final class DocumentApi {

  @RequiredArgsConstructor
  public enum Roles implements Role {

    DOCUMENT_ACCESSOR,

    DOCUMENT_MANAGER;

    @Id
    @Getter
    private final String id = name();

  }

}
