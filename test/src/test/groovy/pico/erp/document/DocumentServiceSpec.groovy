package pico.erp.document

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.document.maker.DocumentMakerDefinition
import pico.erp.document.subject.DocumentSubjectDefinition
import pico.erp.document.subject.DocumentSubjectId
import pico.erp.document.subject.DocumentSubjectRequests
import pico.erp.document.subject.DocumentSubjectService
import pico.erp.document.template.DocumentTemplate
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.shared.data.ContentInputStream
import pico.erp.user.UserId
import spock.lang.Specification

@SpringBootTest(classes = [DocumentApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@Configuration
@ActiveProfiles("test")
class DocumentServiceSpec extends Specification {

  @Bean
  DocumentMakerDefinition testDocumentMakerDefinition() {
    return new DocumentMakerDefinition() {

      @Override
      ContentInputStream make(String name, DocumentTemplate template) {
        def value = template.asString()
        return ContentInputStream.builder()
          .name(name + ".txt")
          .contentLength(value.length())
          .contentType("text/plain")
          .inputStream(new ByteArrayInputStream(value.getBytes()))
          .build()
      }
    }
  }

  @Bean
  DocumentSubjectDefinition testDocumentTypeDefinition() {
    return DocumentSubjectDefinition.Impl.builder()
      .id(subjectId)
      .name("테스트")
      .keyGetter({ k -> k })
      .contextGetter({ k -> [name: "테스트", key: k] })
      .build()
  }

  @Autowired
  DocumentService documentService

  @Autowired
  DocumentSubjectService documentTypeService


  static def template = """{{name}} Hello {{key}}"""

  static def subjectId = DocumentSubjectId.from("TEST")

  def id = DocumentId.from("test")

  def creatorId = UserId.from("kjh")


  def setup() {
    documentTypeService.update(
      new DocumentSubjectRequests.UpdateRequest(
        id: subjectId,
        name: "테스트",
        template: template
      )
    )
  }

  def create() {
    documentService.create(
      new DocumentRequests.CreateRequest(
        id: id,
        subjectId: subjectId,
        name: "테스트 문서",
        key: "key1",
        creatorId: creatorId
      )
    )
  }

  def load() {
    return documentService.load(id)
  }

  def "문서 생성"() {
    when:
    create()
    def content = IOUtils.toString(load(), "UTF-8")

    then:
    content == "테스트 Hello key1"
  }

}
