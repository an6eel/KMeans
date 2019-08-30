# Algoritmo K-Means

Implementaciones del algoritmo de Clustering K-Means para la realización de mi proyecto fin de grado:

## Desarrollo de algoritmos de agrupamiento con paradigma de programación funcional

Las implementaciones realizadas son las siguientes:

+ Implementación imperativa en Java
+ Implementacion funcional en Java
+ Implementación funcional en Scala
+ Implementación funcional con paralelización en Scala

Además se han añadido ficheros de pruebas para medir el rendimiento de cada implementación con el
framework de microBenchmarking [ScalaMeter](https://scalameter.github.io/)

### Resumen

En la actualidad el paradigma de programación funcional está adquiriendo gran importancia.
Algunos lenguajes propios de este paradigma, como Scala, han pasado a ser los lenguajes habituales 
en determinados ámbitos. Incluso se están incorporando elementos de este paradigma a lenguajes imperativos, 
como C++ o Java. En este contexto en el proyecto se plantea estudiar qué posibles ventajas puede ofrecer el 
uso de este paradigma, mediante Scala (lenguaje nativo de programación funcional) y Java (lenguaje de programación
orientada a objetos con características agregadas posteriormente de programación funcional), a la hora de implementar
algoritmos de aprendizaje automático, en concreto de agrupamiento.Esta tarea tiene un elevado coste computacional
y queremos investigar el aprovechamiento del paralelismo de bajo nivel, haciendo uso de todos los núcleos disponibles
en el procesador. Para esto realizaremos diferentes implementaciones del algoritmo K-Means usando los paradigmas
imperativo y funcional, con los lenguajes de programación Java y Scala, respectivamente.
Además, realizaremos una prueba sobre la capacidad de paralelización aportada por las  colecciones de Scala
para comprobar su efectividad. Una vez tengamos estas implementaciones, se ejecutan sobre conjuntos de datos reales
para tener una idea del coste computacional asociado y las posibles ventajas obtenidas

__Palabras clave__ : Paradigma Imperativo, Paradigma Funcional, Agrupamiento, K-Means, Scala, Java, ScalaMeter

### Contenido

- `data/`: Conjuntos de datos sobre los que se han realizado los tests de rendimiento
- `src/main`: Implementaciones del algoritmo
    - `java/`: Implentaciones en Java
        - `fp`: Implementacion con paradigma funcional
        - `sec`: Implementacion con paradigma imperativo
    - `scala/kmeans` : Implementaciones en Scala
        - `sec`: Implentación secuencial
        - `par`: Implementación con paralelización
- `src/test`: Archivos de prueba
    - `java`: Archivo de prueba para las implementaciones en Java
    - `scala`: Archivo de prueba para las implementaciones en Scala
