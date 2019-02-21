package pico.erp.document.template;

import com.github.mustachejava.Mustache;
import java.io.StringWriter;
import lombok.SneakyThrows;

public class DocumentTemplateMustache implements DocumentTemplate {

  private final Mustache markdown;

  private final Object context;

  private String result;

  public DocumentTemplateMustache(Mustache markdown, Object context) {
    this.markdown = markdown;
    this.context = context;
  }

  @SneakyThrows
  @Override
  public String asString() {
    if (result != null) {
      return result;
    }
    StringWriter writer = new StringWriter();
    markdown.execute(writer, context).flush();
    result = writer.toString();
    return result;
  }
}
