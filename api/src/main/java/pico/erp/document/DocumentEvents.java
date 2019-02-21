package pico.erp.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import pico.erp.shared.event.Event;

public interface DocumentEvents {


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.e-document.deleted";

    private DocumentId id;

    public String channel() {
      return CHANNEL;
    }

  }


  @ToString
  @Value
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.e-document.created";

    private DocumentId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @ToString
  @Value
  class StoredEvent implements Event {

    public final static String CHANNEL = "event.e-document.stored";

    private DocumentId id;

    public String channel() {
      return CHANNEL;
    }

  }


}
