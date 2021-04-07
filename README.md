# dagger-extensions

Dagger-extensions is an android plugin that auto generates Dagger’s module classes and makes it much easier to inject dependencies into your Android Project.

## Why do I need it?
Managing dependencies in a big project may be challenging. It’s easy to fall into a trap of many unreadable and complicated relations between components. 
It’s why the dependency injection design pattern was created. In short it is based on creating a service which is responsible for injecting needed dependencies (more about dependency injection you can read [here](https://en.wikipedia.org/wiki/Dependency_injection)).
One of the most popular implementations of that approach in the Android ecosystem is [Dagger](https://dagger.dev/). It’s super useful but unfortunately it leads to a lot of duplication in your code. Dagger-extensions plugin reduces that issue - it’s enough to add special annotation to your class and dagger’s module class will be generated automatically. Thanks to that your code is cleaner, shorter and easier to understand. 

## How to use it?
- Add dagger-extensions to your gradle file:
```groovy
repositories { 
  maven { url 'https://dl.bintray.com/azimolabs/maven' } 
}
dependencies {
  implementation "com.azimolabs.daggerextensions:api:1.0.3"
  annotationProcessor 'com.azimolabs.daggerextensions:processor:1.0.3'
}
```
- Add **@GenerateModule** annotation to class for which you want to have dagger’s module
```java
@GenerateModule
public class ExampleActivity extends Activity implements ExampleInterface {}
```
- *Optional:* If your class should also include another module, add that info in params to annotation (like in the example below):
```java
@GenerateModule(includes = BaseDaggerActivityModule.class)
public class ExampleActivity extends Activity {
    
    @Override
    public void sampleMethod() {}; 
}
```
- Compile your code - dagger-extensions will take care of the rest!

## What exactly dagger-extensions will do?

- Annotation processor will find classes with annotation **@GenerateModule**
- Will create a new abstract class based on a given one. Will have the same name as base class, but with “Module” suffix (example: module generated from ExampleActivity class will be named ExampleActivityModule)
- For every class or interface that your class annotated with GenerateModule inherits from, corresponding @Binds method will be generated 

## So what is a difference?

Code without daggers-extensions:

ExampleActivity.kt
```java
public class ExampleActivity extends Activity implements ExampleInterface {
    
    @Override
    public void sampleMethod() {}; 
}
```

ExampleActivityModule.kt
```java
@Module 
public abstract class ExampleActivityModule { 
    
    @Binds 
    public abstract Activity bindActivity(ExampleActivity parameter); 
    
    @Binds 
    public abstract ExampleInterface bindExampleInterface(ExampleActivity parameter);
}
```

Code with daggers-extensions

ExampleActivity.kt 
```java
@GenerateModule(includes = BaseDaggerActivityModule.class)
public class ExampleActivity extends Activity {
    
    @Override
    public void sampleMethod() {}; 
}
```


# Towards financial services available to all
We’re working throughout the company to create faster, cheaper, and more available financial services all over the world, and here are some of the techniques that we’re utilizing. There’s still a long way ahead of us, and if you’d like to be part of that journey, check out our [careers page](https://bit.ly/3vajnu6).
