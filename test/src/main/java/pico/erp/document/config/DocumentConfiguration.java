package pico.erp.document.config;

import java.io.ByteArrayInputStream;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pico.erp.document.context.DocumentContextFactory;
import pico.erp.document.context.DocumentContextFactoryImpl;
import pico.erp.document.maker.DocumentMakerDefinition;
import pico.erp.document.storage.DocumentStorageStrategy;
import pico.erp.document.storage.FileSystemDocumentStorageStrategy;
import pico.erp.document.template.DocumentTemplate;
import pico.erp.shared.Public;
import pico.erp.shared.data.ContentInputStream;

@Configuration
public class DocumentConfiguration {

  @Public
  @Bean
  @ConditionalOnMissingBean(DocumentContextFactory.class)
  public DocumentContextFactory noOpDocumentContextFactory() {
    return new DocumentContextFactoryImpl();
  }

  @Public
  @Bean
  @ConditionalOnMissingBean(DocumentMakerDefinition.class)
  public DocumentMakerDefinition textDocumentMakerDefinition() {
    return new DocumentMakerDefinition() {

      @Override
      public ContentInputStream make(String name, DocumentTemplate template) {
        val value = template.asString();
        return ContentInputStream.builder()
          .name(name + ".txt")
          .contentLength(value.length())
          .contentType("text/plain")
          .inputStream(new ByteArrayInputStream(value.getBytes()))
          .build();
      }

    };
  }

  @Public
  @Bean
  @ConditionalOnMissingBean(DocumentStorageStrategy.class)
  public DocumentStorageStrategy noOpDocumentStorageStrategy() {
    return new FileSystemDocumentStorageStrategy();
  }




}
