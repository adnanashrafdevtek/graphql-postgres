package com.xcordia.document;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.xcordia.document");

        noClasses()
            .that()
                .resideInAnyPackage("com.xcordia.document.service..")
            .or()
                .resideInAnyPackage("com.xcordia.document.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.xcordia.document.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
