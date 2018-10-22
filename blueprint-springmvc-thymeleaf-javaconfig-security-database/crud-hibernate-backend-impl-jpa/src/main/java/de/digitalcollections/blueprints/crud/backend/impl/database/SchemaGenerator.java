package de.digitalcollections.blueprints.crud.backend.impl.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.TargetType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

public class SchemaGenerator {

  /**
   * Example: java SchemaGenerator de.digitalcollections.blueprints.crud.backend.impl.jpa.entity src/main/resources/sql/generated/
   *
   * @param args first argument is the package to scan for entities, second argument directory to generate the dll to
   * @throws java.lang.Exception when writing to the database fails
   */
  public static void main(String[] args) throws Exception {
    final String packageName = args[0];
    List<Class> entitiesInPackage = findAnnotatedClassesInPackage(packageName, Entity.class);

    final String directory = args[1];
    if (directory != null) {
      File dir = new File(directory);
      if (!dir.exists()) {
        dir.mkdirs();
      }
    }

    generate(Dialect.POSTGRESQL, entitiesInPackage, directory);
    generate(Dialect.MYSQL, entitiesInPackage, directory);
    generate(Dialect.ORACLE, entitiesInPackage, directory);
    generate(Dialect.HSQL, entitiesInPackage, directory);
    generate(Dialect.H2, entitiesInPackage, directory);
  }

  private static List<Class> findAnnotatedClassesInPackage(String basePackage, Class annotationClass) throws IOException, ClassNotFoundException {

    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
    String resolvedBasePackage = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolvedBasePackage + "/" + "**/*.class";
    Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
    List<Class> candidates = new ArrayList<>();

    for (Resource resource : resources) {
      if (resource.isReadable()) {
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        if (isCandidate(metadataReader, annotationClass)) {
          candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
        }
      }
    }
    return candidates;
  }

  /**
   * Method that actually creates the file.
   *
   * @param dialect to use
   */
  private static void generate(Dialect dialect, List<Class> entitiesInPackage, String directory) {
    Map<String, String> settings = new HashMap<>();
    settings.put("hibernate.hbm2ddl.auto", "create");
    settings.put("hibernate.dialect", dialect.getDialectClass());

    MetadataSources metadata = new MetadataSources(new StandardServiceRegistryBuilder().applySettings(settings).build());
    for (Class clazz : entitiesInPackage) {
      metadata.addAnnotatedClass(clazz);
    }
    Metadata md = (MetadataImplementor) metadata.buildMetadata();

    SchemaExport export = new SchemaExport();
    export.setDelimiter(";");
    export.setOutputFile(directory + "ddl_" + dialect.name().toLowerCase() + ".sql");
    export.setFormat(true);
    EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
    export.execute(targetTypes, Action.CREATE, md);
  }

  /**
   * Holds the classnames of hibernate dialects for easy reference.
   */
  private static enum Dialect {

    POSTGRESQL("org.hibernate.dialect.PostgreSQLDialect"),
    ORACLE("org.hibernate.dialect.Oracle10gDialect"),
    MYSQL("org.hibernate.dialect.MySQLDialect"),
    HSQL("org.hibernate.dialect.HSQLDialect"),
    H2("org.hibernate.dialect.H2Dialect");

    private final String dialectClass;

    private Dialect(String dialectClass) {
      this.dialectClass = dialectClass;
    }

    public String getDialectClass() {
      return dialectClass;
    }
  }

  private static boolean isCandidate(MetadataReader metadataReader, Class annotationClass) throws ClassNotFoundException {
    try {
      Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
      if (c.getAnnotation(annotationClass) != null) {
        return true;
      }
    } catch (Throwable e) {
    }
    return false;
  }
}
