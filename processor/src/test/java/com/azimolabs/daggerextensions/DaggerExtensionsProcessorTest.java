package com.azimolabs.daggerextensions;

import com.azimolabs.processor.DaggerExtensionsProcessor;
import com.google.common.collect.ImmutableList;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.azimolabs.processor.DaggerExtensionsProcessor.BIND_STRING;
import static com.azimolabs.processor.DaggerExtensionsProcessor.INCLUDES_STRING;
import static com.azimolabs.processor.DaggerExtensionsProcessor.MODULE_STRING;
import static com.azimolabs.processor.DaggerExtensionsProcessor.PARAMETER_STRING;
import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DaggerExtensionsProcessorTest {

    //region example classes
    private final JavaFileObject EXAMPLE_CLASS_1 = JavaFileObjects
            .forSourceString("ExampleClass_1",
                    "package com.azimolabs.daggerextensions;\n" +

                            "public class ExampleClass_1 {\n" +
                            "    void sampleMethod() {\n" +
                            "    }\n" +
                            "}");

    private final JavaFileObject EXAMPLE_INTERFACE_1 = JavaFileObjects
            .forSourceString("ExampleInterface_1",
                    "package com.azimolabs.daggerextensions;\n" +

                            "public interface ExampleInterface_1 {\n" +
                            "    void sampleMethod();\n" +
                            "}");

    private final JavaFileObject EXAMPLE_INTERFACE_2 = JavaFileObjects
            .forSourceString("ExampleInterface_2",
                    "package com.azimolabs.daggerextensions;\n" +

                            "public interface ExampleInterface_2 {\n" +
                            "    void sampleMethod();\n" +
                            "}");
    //endregion

    //region activities
    private final JavaFileObject ACTIVITY_EXTENDS_IMPLEMENTS_GENERATE_MODULE_WITH_INCLUDED = JavaFileObjects
            .forSourceString("ExampleActivity",
                    "package com.azimolabs.daggerextensions.example.activity;\n" +

                            "import android.app.Activity;\n" +
                            "import com.azimolabs.daggerextensions.ExampleClass_1;\n" +
                            "import com.azimolabs.daggerextensions.ExampleInterface_1;\n" +
                            "import com.azimolabs.daggerextensions.ExampleInterface_2;\n" +
                            "import com.azimolabs.daggerextensions.api.GenerateModule;\n" +

                            "@GenerateModule(" + INCLUDES_STRING + "= ExampleClass_1.class)\n" +
                            "public class ExampleActivity extends Activity implements ExampleInterface_1, ExampleInterface_2 {\n" +
                            "    @Override\n" +
                            "    public void sampleMethod() {};\n" +
                            "}");

    private final JavaFileObject ACTIVITY_EXTENDS_GENERATE_MODULE_WITH_INCLUDED = JavaFileObjects
            .forSourceString("ExampleActivity",
                    "package com.azimolabs.daggerextensions.example.activity;\n" +

                            "import android.app.Activity;\n" +
                            "import com.azimolabs.daggerextensions.ExampleClass_1;\n" +
                            "import com.azimolabs.daggerextensions.api.GenerateModule;\n" +

                            "@GenerateModule(" + INCLUDES_STRING + "= ExampleClass_1.class)\n" +
                            "public class ExampleActivity extends ExampleClass_1 {\n" +
                            "}");

    private final JavaFileObject ACTIVITY_EXTENDS_IMPLEMENTS_GENERATE_MODULE = JavaFileObjects
            .forSourceString("ExampleActivity",
                    "package com.azimolabs.daggerextensions.example.activity;\n" +

                            "import android.app.Activity;\n" +
                            "import com.azimolabs.daggerextensions.ExampleClass_1;\n" +
                            "import com.azimolabs.daggerextensions.ExampleInterface_1;\n" +
                            "import com.azimolabs.daggerextensions.ExampleInterface_2;\n" +
                            "import com.azimolabs.daggerextensions.api.GenerateModule;\n" +

                            "@GenerateModule\n" +
                            "public class ExampleActivity extends Activity implements ExampleInterface_1, ExampleInterface_2 {\n" +
                            "    @Override\n" +
                            "    public void sampleMethod() {};\n" +
                            "}");
    //endregion

    //region tests
    @Test
    public void test_activityExtendsImplements_generatedModule_whichIncludes() {
        assertAbout(javaSources())
                .that(ImmutableList.of(ACTIVITY_EXTENDS_IMPLEMENTS_GENERATE_MODULE_WITH_INCLUDED, EXAMPLE_CLASS_1, EXAMPLE_INTERFACE_1, EXAMPLE_INTERFACE_2))
                .processedWith(new DaggerExtensionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.azimolabs.daggerextensions.example.activity.ExampleActivity" + MODULE_STRING,
                        "package com.azimolabs.daggerextensions.example.activity;\n" +
                                "import android.app.Activity;\n" +
                                "import com.azimolabs.daggerextensions.ExampleClass_1;\n" +
                                "import com.azimolabs.daggerextensions.ExampleInterface_1;\n" +
                                "import com.azimolabs.daggerextensions.ExampleInterface_2;\n" +
                                "import dagger.Binds;\n" +
                                "import dagger.Module;\n" +

                                "@Module(" + INCLUDES_STRING + "= {ExampleClass_1.class})\n" +
                                "public abstract class ExampleActivity" + MODULE_STRING + " {\n" +

                                "  @Binds\n" +
                                "  public abstract Activity " + BIND_STRING + "Activity(ExampleActivity " + PARAMETER_STRING + ");\n" +

                                "  @Binds\n" +
                                "  public abstract ExampleInterface_1 " + BIND_STRING + "ExampleInterface_1(ExampleActivity " + PARAMETER_STRING + ");\n" +

                                "  @Binds\n" +
                                "  public abstract ExampleInterface_2 " + BIND_STRING + "ExampleInterface_2(ExampleActivity " + PARAMETER_STRING + ");\n" +
                                "}"
                        )
                )
                .withWarningContaining("No processor claimed any of these annotations: dagger.Binds,dagger.Module");
    }

    @Test
    public void test_activityExtends_generatedModule_whichIncludes() {
        assertAbout(javaSources())
                .that(ImmutableList.of(ACTIVITY_EXTENDS_GENERATE_MODULE_WITH_INCLUDED, EXAMPLE_CLASS_1))
                .processedWith(new DaggerExtensionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.azimolabs.daggerextensions.example.activity.ExampleActivity" + MODULE_STRING,
                        "package com.azimolabs.daggerextensions.example.activity;\n" +
                                "import com.azimolabs.daggerextensions.ExampleClass_1;\n" +
                                "import dagger.Binds;\n" +
                                "import dagger.Module;\n" +

                                "@Module(" + INCLUDES_STRING + "= {ExampleClass_1.class})\n" +
                                "public abstract class ExampleActivity" + MODULE_STRING + "{\n" +

                                "  @Binds\n" +
                                "  public abstract ExampleClass_1 " + BIND_STRING + "ExampleClass_1(ExampleActivity " + PARAMETER_STRING + ");\n" +
                                "}"
                        )
                )
                .withWarningContaining("No processor claimed any of these annotations: dagger.Binds,dagger.Module");
    }

    @Test
    public void test_activityExtendsImplements_generatedModule() {
        assertAbout(javaSources())
                .that(ImmutableList.of(ACTIVITY_EXTENDS_IMPLEMENTS_GENERATE_MODULE, EXAMPLE_CLASS_1, EXAMPLE_INTERFACE_1, EXAMPLE_INTERFACE_2))
                .processedWith(new DaggerExtensionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.azimolabs.daggerextensions.example.activity.ExampleActivity" + MODULE_STRING,
                        "package com.azimolabs.daggerextensions.example.activity;\n" +
                                "import android.app.Activity;\n" +
                                "import com.azimolabs.daggerextensions.ExampleInterface_1;\n" +
                                "import com.azimolabs.daggerextensions.ExampleInterface_2;\n" +
                                "import dagger.Binds;\n" +
                                "import dagger.Module;\n" +

                                "@Module\n" +
                                "public abstract class ExampleActivity" + MODULE_STRING + " {\n" +

                                "  @Binds\n" +
                                "  public abstract Activity " + BIND_STRING + "Activity(ExampleActivity " + PARAMETER_STRING + ");\n" +

                                "  @Binds\n" +
                                "  public abstract ExampleInterface_1 " + BIND_STRING + "ExampleInterface_1(ExampleActivity " + PARAMETER_STRING + ");\n" +

                                "  @Binds\n" +
                                "  public abstract ExampleInterface_2 " + BIND_STRING + "ExampleInterface_2(ExampleActivity " + PARAMETER_STRING + ");\n" +
                                "}"
                        )
                )
                .withWarningContaining("No processor claimed any of these annotations: dagger.Binds,dagger.Module");
    }
    //endregion
}
