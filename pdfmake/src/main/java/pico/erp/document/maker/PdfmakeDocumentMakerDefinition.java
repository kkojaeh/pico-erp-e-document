package pico.erp.document.maker;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.net.URI;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import pico.erp.document.template.DocumentTemplate;
import pico.erp.shared.data.ContentInputStream;

public class PdfmakeDocumentMakerDefinition implements DocumentMakerDefinition {

  private final File workspace;

  private static final String WORKSPACE_PATTERN =
    ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/pdfmake-workspace/**/*";

  @SneakyThrows
  public PdfmakeDocumentMakerDefinition(Config config) {
    workspace = config.getWorkspace();
    if (!workspace.exists()) {
      workspace.mkdirs();
    }

    val resolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
    val resources = resolver.getResources(WORKSPACE_PATTERN);
    val root = new ClassPathResource("pdfmake-workspace").getURI();
    for (val resource : resources) {
      URI uri = resource.getURI();
      if (resource.contentLength() > 0) {
        val relative = uri.toString().substring(root.toString().length());
        FileUtils.copyInputStreamToFile(resource.getInputStream(),
          new File(workspace.getAbsolutePath() + relative));
      }
    }
    run("npm", "install");
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Config {

    File workspace;

  }

  @SneakyThrows
  @Override
  public ContentInputStream make(String name, DocumentTemplate template) {
    val uuid = UUID.randomUUID().toString();
    val input = uuid + ".js";
    val output = uuid + ".pdf";
    val inputFile = new File(workspace, input);
    val outputFile = new File(workspace, output);
    try {
      // file 이동
      FileUtils.writeStringToFile(inputFile, template.asString(), "UTF-8");
      run("node", "index.js", input, output);
      byte[] bytes = FileUtils.readFileToByteArray(outputFile);
      val result = ContentInputStream.builder()
        .name(name + ".pdf")
        .inputStream(new ByteArrayInputStream(bytes))
        .contentType("application/pdf")
        .contentLength(bytes.length)
        .build();
      return result;
    } finally {
      if (inputFile.exists()) {
        inputFile.delete();
      }
      if (outputFile.exists()) {
        outputFile.delete();
      }
    }
  }

  @SneakyThrows
  private void run(String... commands) {
    ProcessBuilder builder = new ProcessBuilder(commands);
    builder.directory(workspace);
    builder.redirectOutput(Redirect.INHERIT);
    builder.redirectError(Redirect.INHERIT);
    val process = builder.start();
    process.waitFor();
  }
}
