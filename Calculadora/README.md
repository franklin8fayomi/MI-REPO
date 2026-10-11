# Calculadora (Android)

Calculadora científica sencilla para Android, hecha en Java con Android Studio.

## Funciones
- Suma (+), resta (−), multiplicación (×) y división (÷)
- Raíz cuadrada (√)
- Seno (sin), coseno (cos), tangente (tan) y arcotangente (atan), en grados
- Paréntesis para agrupar operaciones
- Botón C para borrar

## Estructura
```
app/src/main/
├── java/upc/edu/calculadora/
│   ├── MainActivity.java   → gestiona los botones y la pantalla
│   └── Evaluador.java      → calcula la expresión (respeta el orden de operaciones)
└── res/layout/
    └── activity_main.xml   → diseño: pantalla + teclado de 6 filas × 4 botones
```

## Cómo ejecutarla
1. Abrir la carpeta `Calculadora` con Android Studio (**File → Open**).
2. Esperar a que termine la sincronización de Gradle.
3. Pulsar **▶ Run** con un emulador o un móvil conectado.
