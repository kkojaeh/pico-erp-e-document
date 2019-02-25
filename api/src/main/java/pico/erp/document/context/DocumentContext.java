package pico.erp.document.context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.ContentInputStream;

@RequiredArgsConstructor
@Builder
public class DocumentContext {

  @Getter
  public final Map data = new HashMap<>();

  @Getter
  @NonNull
  private final Function<String, String> dateFormatter;

  @Getter
  @NonNull
  private final Function<String, String> dateTimeFormatter;

  @Getter
  @NonNull
  private final Function<String, String> phoneNumberFormatter;

  @Getter
  @NonNull
  private final Function<ContentInputStream, String> contentEncoder;

}
