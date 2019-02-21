package pico.erp.document.maker;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import pico.erp.document.template.DocumentTemplate;
import pico.erp.shared.data.ContentInputStream;

public class PdfmakeAwsLambdaDocumentMakerDefinition implements DocumentMakerDefinition {

  private final AWSLambda lambda;

  private final String lambdaFunctionName;

  public PdfmakeAwsLambdaDocumentMakerDefinition(
    PdfmakeAwsLambdaDocumentMakerDefinitionConfig config) {
    lambda = config.getLambda();
    lambdaFunctionName = config.getLambdaFunctionName();
  }

  @Override
  public ContentInputStream make(String name, DocumentTemplate template) {
    InvokeRequest request = new InvokeRequest().withFunctionName(lambdaFunctionName)
      .withPayload(ByteBuffer.wrap(template.asString().getBytes()));

    InvokeResult result = lambda.invoke(request);
    byte[] base64bytes = result.getPayload().array();
    // json 형태의 시작/마지막 (") 를 제거
    byte[] withoutQuotes = Arrays.copyOfRange(base64bytes, 1, base64bytes.length - 1);
    byte[] bytes = Base64.getDecoder().decode(withoutQuotes);
    return ContentInputStream.builder()
      .name(name + ".pdf")
      .inputStream(new ByteArrayInputStream(bytes))
      .contentType("application/pdf")
      .contentLength(bytes.length)
      .build();
  }
}
