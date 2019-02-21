package pico.erp.document;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pico.erp.shared.ApplicationInitializer;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class DocumentInitializer implements ApplicationInitializer {

  public interface DocumentInitializable {

    void initialize();

  }

  @Lazy
  @Autowired
  private List<DocumentInitializable> initializables;

  @Override
  public void initialize() {
    initializables.stream().forEach(DocumentInitializable::initialize);
  }
}
