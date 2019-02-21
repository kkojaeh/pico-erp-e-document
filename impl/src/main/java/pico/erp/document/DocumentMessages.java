package pico.erp.document;

import java.io.InputStream;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.document.storage.DocumentStorageKey;
import pico.erp.document.storage.DocumentStorageStrategy;
import pico.erp.document.type.DocumentTypeId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.shared.event.Event;
import pico.erp.user.UserId;

public interface DocumentMessages {


  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      DocumentId id;

      @Valid
      @NotNull
      DocumentTypeId typeId;

      @NotNull
      @Size(max = TypeDefinitions.NAME_X2_LENGTH)
      String name;

      @Valid
      @NotNull
      UserId creatorId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Store {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    class Request {

      @Valid
      @NotNull
      ContentInputStream contentInputStream;

      @NotNull
      DocumentStorageStrategy storageStrategy;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface Delete {

    @Data
    class Request {

      @NotNull
      DocumentStorageStrategy storageStrategy;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

}
