# mooncraft-services

This repository contains a backend application and a plugin providing network information.

## Building

This project uses Gradle to handle dependencies and building.

**Requirements:**

- Java 16 JDK
- Git

**Compiling from source:**

```sh
git clone https://github.com/SafemoonMC/mooncraft-services.git
cd mooncraft-services/
./gradlew buildAll
```

You can find the output artifacts in the `/COMPILED_JARS` directory.

**Other Gradle custom tasks:**

- **buildBase**, it gets just the jar without any dependency in;
- **buildSources**, it gets just the jar with project files in;
- **buildJavadoc**, it gets just the final JavaDoc;
- **buildShadowjar**, it gets just the final jar with all necessary dependencies;

## Contributing

This project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
Generally, you can import the style from the `java-google-style.xml` file you can find at the root of
the project.
