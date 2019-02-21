package pico.erp.document.maker;

import pico.erp.document.template.DocumentTemplate;
import pico.erp.shared.data.ContentInputStream;

public interface DocumentMakerDefinition {

  ContentInputStream make(String name, DocumentTemplate template);

}
