package pico.erp.document;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class DocumentApi {

  public static ApplicationId ID = ApplicationId.from("document");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    DOCUMENT_ACCESSOR,
    DOCUMENT_MANAGER;

    @Id
    @Getter
    private final String id = name();

  }

}
