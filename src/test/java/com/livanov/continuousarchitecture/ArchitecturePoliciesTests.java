package com.livanov.continuousarchitecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.PackageMatchers;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static org.assertj.core.api.Assertions.assertThat;

class ArchitecturePoliciesTests {

    private final JavaClasses allClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackagesOf(ContinuousArchitectureApplication.class);

    @Nested
    class JavaCode {

        @Test
        void no_field_injection() {
            NO_CLASSES_SHOULD_USE_FIELD_INJECTION
                    .check(allClasses);
        }

        @Test
        void no_java_util_logging() {
            NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING
                    .check(allClasses);
        }

        @Test
        void app_should_have_three_layered_architecture() {
            layeredArchitecture()
                    .consideringOnlyDependenciesInLayers()
                    .layer("Web").definedBy("..web..")
                    .layer("Domain").definedBy("..domain..")
                    .layer("Persistence").definedBy("..persistence..")

                    .whereLayer("Web").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Web").mayOnlyAccessLayers("Domain")
                    .whereLayer("Persistence").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Persistence").mayOnlyAccessLayers("Domain")
                    .whereLayer("Domain").mayNotAccessAnyLayer()
                    .check(allClasses);
        }

        @Test
        void controllers_should_be_called_controllers_and_not_be_public_and_reside_in_web_package_and_call_secure_methods() {
            classes()
                    .that().areAnnotatedWith(RestController.class)
                    .should().notBePublic()
                    .andShould().resideInAPackage("..web..")
                    .andShould().haveSimpleNameEndingWith("Controller")
                    .andShould().onlyCallMethodsThat(new DescribedPredicate<>("are own methods, annotated with @Secured") {
                        @Override
                        public boolean test(JavaMethod javaMethod) {
                            boolean residesInAppPackage = PackageMatchers.of("com.livanov..").test(javaMethod.getOwner().getPackage().getName());

                            return !residesInAppPackage || javaMethod.isAnnotatedWith(Secured.class);
                        }
                    })
                    .check(allClasses);
        }

        @Test
        void repositories_should_not_be_public_and_be_called_repositories() {
            classes()
                    .that().areAssignableTo(Repository.class)
                    .should().notBePublic()
                    .andShould().haveSimpleNameEndingWith("Repository")
                    .check(allClasses);
        }

        @Test
        void dtos_should_go_in_dto_packages() {
            classes()
                    .that().haveSimpleNameEndingWith("Dto")
                    .should().resideInAPackage("..dto")
                    .check(allClasses);
        }

        @Test
        void builder_classes_should_be_called_builder() {
            classes()
                    .that().containAnyMethodsThat(new DescribedPredicate<>("called \"build\"") {
                        @Override
                        public boolean test(JavaMethod javaMethod) {
                            return "build".equals(javaMethod.getName());
                        }
                    })
                    .should().haveSimpleNameEndingWith("Builder")
                    .check(allClasses);
        }
    }

    @Nested
    @SpringBootTest
    @Import(TestContinuousArchitectureApplication.class)
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    class Database {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Test
        void should_not_allow_non_text_primary_keys() {

            // WHEN
            val allNonTextPrimaryKeys = jdbcTemplate.queryForList("""
                        SELECT c.table_name, c.column_name, c.is_identity, c.identity_generation, c.data_type
                        FROM information_schema.table_constraints tc
                                JOIN information_schema.constraint_column_usage AS ccu USING (constraint_schema, constraint_name)
                                JOIN information_schema.columns AS c ON c.table_schema = tc.constraint_schema
                                 AND tc.table_name = c.table_name AND ccu.column_name = c.column_name
                            WHERE constraint_type = 'PRIMARY KEY'
                              AND c.table_schema = (SELECT current_schema())
                              AND c.data_type <> 'text'
                    """);

            // THEN
            assertThat(allNonTextPrimaryKeys).isEmpty();
        }
    }
}