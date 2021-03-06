package pico.erp.document;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import kkojaeh.spring.boot.component.ComponentBean;
import kkojaeh.spring.boot.component.SpringBootComponent;
import kkojaeh.spring.boot.component.SpringBootComponentBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pico.erp.ComponentDefinition;
import pico.erp.document.DocumentApi.Roles;
import pico.erp.shared.SharedConfiguration;
import pico.erp.shared.data.Role;

@Slf4j
@SpringBootComponent("e-document")
@EntityScan
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
@Import(value = {
  SharedConfiguration.class
})
public class DocumentApplication implements ComponentDefinition {

  public static void main(String[] args) {
    new SpringBootComponentBuilder()
      .component(DocumentApplication.class)
      .run(args);
  }

  @Bean
  @ComponentBean(host = false)
  public Role documentAccessor() {
    return Roles.DOCUMENT_ACCESSOR;
  }

  @Bean
  public MustacheFactory mustacheFactory() {
    val factory = new DefaultMustacheFactory();
    return factory;
  }

  @Bean
  @ComponentBean(host = false)
  public Role documentManager() {
    return Roles.DOCUMENT_MANAGER;
  }

  @Override
  public Class<?> getComponentClass() {
    return DocumentApplication.class;
  }

}
