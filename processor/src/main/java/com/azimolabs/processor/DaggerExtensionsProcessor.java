package com.azimolabs.processor;

import com.azimolabs.daggerextensions.api.GenerateModule;
import com.azimolabs.daggerextensions.api.NotSpecified;
import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class DaggerExtensionsProcessor extends AbstractProcessor {

    public static final String MODULE_STRING = "Module";
    public static final String BIND_STRING = "bind";
    public static final String PARAMETER_STRING = "parameter";
    public static final String INCLUDES_STRING = "includes";

    private static final Class<GenerateModule> GENERATE_MODULE = GenerateModule.class;
    private static final ClassName BINDS = ClassName.get("dagger", "Binds");
    private static final ClassName MODULE = ClassName.get("dagger", "Module");

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(GenerateModule.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean processed = false;
        for (TypeElement annotation : annotations) {
            if (annotation.getQualifiedName().toString().equals(GENERATE_MODULE.getName())) {
                for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(GENERATE_MODULE)) {
                    try {
                        generateModuleForAnnotatedElement(annotatedElement);
                        processed = true;
                    } catch (IOException e) {
                        messager.printMessage(Diagnostic.Kind.NOTE,
                                e.getMessage() + "Error creating file");
                    }
                }
            }
        }
        return processed;
    }

    private void generateModuleForAnnotatedElement(Element annotatedElement) throws IOException {
        List<MethodSpec> methodsInModule = new ArrayList<>();

        MethodSpec.Builder bindSuperClassBuilder = MethodSpec
                .methodBuilder(BIND_STRING + getSimpleNameOfClass(((TypeElement) annotatedElement).getSuperclass()))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(BINDS)
                .returns(ClassName.get(((TypeElement) annotatedElement).getSuperclass()))
                .addParameter(ParameterSpec.builder(
                        ClassName.get((TypeElement) annotatedElement), PARAMETER_STRING)
                        .build());
        methodsInModule.add(bindSuperClassBuilder.build());

        ((TypeElement) annotatedElement).getInterfaces().forEach(elementInterface -> {
                    MethodSpec.Builder bindInterfaceBuilder = MethodSpec
                            .methodBuilder(BIND_STRING + getSimpleNameOfClass(elementInterface))
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .addAnnotation(BINDS)
                            .returns(ClassName.get(elementInterface))
                            .addParameter(ParameterSpec.builder(
                                    ClassName.get((TypeElement) annotatedElement), PARAMETER_STRING)
                                    .build());
                    methodsInModule.add(bindInterfaceBuilder.build());
                }
        );

        if (!methodsInModule.isEmpty()) {
            TypeSpec.Builder moduleBuilder = TypeSpec.classBuilder(
                    annotatedElement.getSimpleName() + MODULE_STRING)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addMethods(methodsInModule);

            moduleBuilder.addAnnotation(getModuleAnnotationForElement(annotatedElement));

            String packageName = processingEnv.getElementUtils()
                    .getPackageOf(annotatedElement).getQualifiedName().toString();

            JavaFile.builder(packageName, moduleBuilder.build())
                    .build()
                    .writeTo(filer);
        }
    }

    @NotNull
    private AnnotationSpec getModuleAnnotationForElement(Element annotatedElement) {
        TypeMirror typeMirror = getIncluded(annotatedElement);
        AnnotationSpec.Builder annotationSpecBuilder = AnnotationSpec.builder(MODULE);

        if (typeMirror != null) {
            annotationSpecBuilder.addMember(INCLUDES_STRING, "{$T.class}", ClassName.get(typeMirror));
        }
        return annotationSpecBuilder.build();
    }

    /**
     * There the trick is to actually use getAnnotation() and catch the MirroredTypeException.
     * Surprisingly the exception then provides the TypeMirror of the required class.
     * If TypeMirror object's name is NotSpecified, we don't want to generate class like this.
     *
     * @param element to process
     * @return class which should be included
     */
    private TypeMirror getIncluded(Element element) {
        TypeMirror included = null;

        try {
            element.getAnnotation(GENERATE_MODULE).includes();
        } catch (MirroredTypeException mte) {
            String name = (mte.getTypeMirror().toString());
            if (name != null && name.equals(NotSpecified.class.getName())) {
                return null;
            }
            included = mte.getTypeMirror();
        }
        return included;
    }

    /**
     * Extracting simple class name from TypeMirror element
     *
     * @param elementInterface as TypeMirror
     * @return simple class name
     */
    private String getSimpleNameOfClass(TypeMirror elementInterface) {
        String[] fullNameOfClass = elementInterface.toString().split("\\.");
        return fullNameOfClass[fullNameOfClass.length - 1];
    }
}