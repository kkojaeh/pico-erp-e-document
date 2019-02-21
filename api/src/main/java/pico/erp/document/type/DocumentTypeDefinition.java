package pico.erp.document.type;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DocumentTypeDefinition<K, C> {

  C createContext(K key);

  K createKey(String key);

  DocumentTypeId getId();

  String getName();

  @Getter
  @Builder
  @AllArgsConstructor
  class Impl<K, C> implements DocumentTypeDefinition<K, C> {

    DocumentTypeId id;

    String name;

    Function<K, C> contextCreator;

    Function<String, K> keyCreator;

    @Override
    public C createContext(K key) {
      return contextCreator.apply(key);
    }

    @Override
    public K createKey(String key) {
      return keyCreator.apply(key);
    }

  }

}
