# Unidad 4: Post Basketball Score App (Proyecto que resolví solo):

## **Activity Lifecycle**

Como su nombre lo indica es el ciclo de vida de una Activity. Es un poco mas complejo que el ciclo de vida humano (nacer, crecer, reproducirse y morir) pero al igual que el ultimo sigue una estructura con etapas obligadas (nacer, crecer y morir) y otras optavivas (reproducirse). Es un poco más complejo porque tiene más pasos, incluso algunos se regresan.

![Captura 1](screenshots/lifecycle_1.png)

Todos los metodos se van llamando automaticamente. Nosotros no tenemos que hacer absolutamente nada.

¿Cual es la importancia de comprender el Lifecycle de una app?

En ocasiones, cuando desde una app "x" (ejemplo "Flex App") mandamos abrir otra aplicación, por ej Google Maps, la actividad puedo pasar a distintos momentos de su lifecycle. Por ejemplo:

1- Llega a "on pause": tiene tres caminos posibles en función de lo que ocurra {
A- Otras aplicaciones con mayor prioridad necesitan memoría. Por lo que Android va a matar a esta activity pausada.
B- La actividad está un largo tiempo sin uso. Pasa al metodo "on stop"
C- El usuario retorna a la activity. Pasa a "on resume"
}
2- Llega a "on stop: tiene tres caminos nuevamente {
A- Otras aplicaciones con mayor prioridad necesitan memoría. Por lo que Android va a matar a esta activity pausada.
B- La actividad es finalizada o es destruida por el sistema. Pasa a "on destroy"
C- El usuario navega hacia la activity nuevamente. Pasa a "on restart"
}

Es importante acá detenerse y entender que en función de cualquiera de los caminos que tenga la activity en su ciclo de vida debemos preveer distintas acciones para manejar nuestros datos. En situaciones deberemos recurrir a una base de datos, en otras deberemos registrar esos datos en nuestra aplicación solamente y con ello será suficiente.

De aquí la importancía de controlar la aplicación durante todo el ciclo de vida.

Por ultimo. Cuando paso la aplicación al modo horizontal se producen los siguiente ciclos de la activity en orden:
stopped -> destroyed -> created -> started -> resumed
Al volver al modo vertical va a pasar nuevamente lo mismo.

Este ciclo de vida al rotar va a hacer que por ejemplo, el marcador de nuestra app de basketball se pierda frente a la rotación. Por lo que tendremos que aprender a manejar esta situación...

Para efectos de programación en Android, **una Activity es un View**, pero **no hay que confundirlo con los tipos de views que hemos estado viendo**, que son los botones TextView. Sino más bien es una clase de tipo View, porque es la que se dedica a mostrar la información al usuario.
Pero **las Activities tienen un problema y es que nosotros no tenemos control sobre ellas**. Como te mencioné en las lecciones pasadas, Android puede destruirlas cuando se le dé la gana.
Ya lo vimos que puede suceder tanto cuando abres otra aplicación que necesita recursos como cuando por ejemplo, giras la pantalla, perdemos todos los datos y no hay mucho control o no hay mucho que hacer con eso.

Cuando las apps se vuelven muy grandes, las activities deben dejar de ocuparse del procesamiento y la lógica de nuestra app y deben solo concentrarse en mostrarle la información al usuario y en recibir las interacciones que el mismo haga.

Cuando eso sucede debemos recurrir a otra clase, auxiliar a las activities, para tales fines que se llama "ViewModel"

Entonces, en resumen, todo lo que sea de **mostrar datos y reaccionar a la pantalla se va a hacer con la Activity** y todas las variables, todo el procesamiento, **todo lo demás se va a hacer con el ViewModel**.

---

## **ViewModel**

El ViewModel es una clase que mientras la Activity realiza todo su ciclo de vida (se crea, se pausa, detiene, restablece, muere, etc) sigue vivo en todo el proceso.

![Captura 2](screenshots/viewmodel_1.png)

Entonces, como vimos en la lección pasada del Basquetball Score, cuando rotamos la Activity, si teníamos los datos almacenados en la misma Activity, estos se perdían.
Pero si los tenemos ahora almcenados en el ViewModel vemos que como el modelo sobrevive a la rotación de la Activity. Esos datos ya no se van a perder.
Y otra gran ventaja que yo veo es que cuando hacemos llamados a Internet, nosotros no sabemos en qué momento va a contestar el servidor. Esto depende mucho de la velocidad del Internet, de cómo esté programado el backend.
Entonces, como no sabemos en qué momento va a llegar... un problema muy recurrente que teníamos antes, cuando no existía esto del ViewModel, es que si esos datos volvían después del "on pause" o del "on stop", la app se rompía porque no tenía forma de mostrarlo, porque la Activity ya estaba destruida.
Con el ViewModel si esta operación se está haciendo y la Activity manda a llamar Destroy, el modelo automáticamente va a destruir el proceso y no va a tratar de mostrar nada y entonces te ahorras bastantes problemas que teníamos antes como programadores de Android. Y este mismo problema aplicaba tanto para cuando traíamos datos de Internet como cuando los traíamos desde la base de datos.

Pasos para trabajar con ViewModel:

1- Agregar una dependencia extra, dado que ViewModel no viene con Android Studio por defecto. ¿Como?
En "dependencies" de build.gradle (:app) agregamos:

- "**implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'**" -> Sync now
  2- En MainActivity creamos una variable global (es decir, dentro de la class MainActivity pero fuera del metodo "onCreate" del lyfecycle):
- **private lateinit var viewModel: MainViewModel**
  3- Dentro del metodo "onCreate" vamos a instanciar a "viewModel:
- **viewModel = ViewModelProvider(this).get(MainViewModel::class.java)**
  Los ViewModel los creamos de esta manera porque tienen algo especial. Como dijimos arriba un ViewModel, cuando destruimos una actividad al girar el teléfono, por ejemplo, el ViewModel no se destruye, sino que sigue vivo hasta que la actividad se destruye por completo.
  Entonces, al crear el ViewModel de esta manera, lo que hace es que si giramos el teléfono, la actividad vuelve a llamar a este metodo "on create" pero al hacer esto ya va a detectar que el ViewModel está creado y no va a crear uno nuevo. Esta última parte es el efecto del ".get(Main...)" que está arriba.
  4- Vamos a ir a la carpeta de nuestro paquete ("com.{empresa}.{proyecto}") y hacemos: Botón derecho -> New -> Kotlin Class/File
  5- Le ponemos el nombre que ya hacemos usado dentro del ".get(Main...)":
- **MainViewModel**, seleccionamos clase y Enter.
  6- Esta clase va a **heredar** de un ViewModel. ¿Como? En kotlin así:

```kotlin
class MainViewModel: ViewModel() {
    // Class MainViewModel que hereda atributos y metodos de la class ViewModel (importada)
}
```

### A partir de acá comienza a regir la regla de oro: SEPARACION DE PREOCUPACIONES:

**La regla de oro nos dice que el _MainViewModel_ haga todo lo que tenga que ver con lógica, y el _MainActivity_ solamente se dedique a pintar los datos para el usuario y a responder a las acciones del usuario.**

Migramos todas las variables lógicas que creamos en MainActivity para manejar el funcionamiento de los botones de la Activity hacia MainViewModel.

Luego migramos todas las funciones logicas que modificaban las variables descritas arriba ⬆️.

Pero con esto no nos alcanza para que luego de un stop -> destroyed como el que genera el giro de pantalla en la activity se conserve la data. Para ello tenemos que usar **LiveData**

---

## **LiveData**

_¿Que son los LiveData?_

Los LiveData son **variables que insertamos en ViewModel y que a su vez contienen otras variables.** Por ejemplo, podemos tener un LiveData de enteros, un LiveData de strings o de cualquier otro tipo de variable que queramos, y los LiveData funcionan como el típico hermanito buchón que va llevando "chismes" de un familiar a otro (en este caso sus familiares serían la Activity y el ViewModel).
Supongamos que tenemos un LiveData de un entero llamado localScore, como en la aplicación de Basketball Score y esa variable localScore cambia.
Entonces, lo que va a hacer el hermanito "buchon" es que va a ir y le va a avisar a mamá "Activity" que la variable cambio en lo de su hermano mayor "ViewModel".
Allí mamá **"Activity"** sabrá que hacer con él si retarlo (lease **pintarlo para el usuario**) o hacer otra cosa.
Y también al revés, una vez que la **Activity LiveData cambió**, va a ir y le va a avisar al **ViewModel** que cambió **para que el Model pueda detener su proceso.**

_Tipos de LiveData:_

1- Normal: No podemos asignar valores. Solo podemos leerlos
2- Mutable: Podemos asignar y leer valores. Vamos a usar de estos dos tanto para las variables trasladadas a MainViewModel "localScoreInt" como "visitanScoreInt":

Pasamos entonces de:

```kotlin
class MainViewModel: ViewModel() {
    var localScoreInt: 0
    var visitantScoreInt = 0
    ...
}
```

a:

```kotlin
class MainViewModel: ViewModel() {
    var localScoreInt: MutableLiveData<Int> = MutableLiveData()
    var visitantScoreInt: MutableLiveData<Int> = MutableLiveData()
    ...
}
```

Una vez definidas las variables de nuestro MainViewModel como LiveData debemos asignar sus valores. Pero esto ya no se realiza como hasta ahora sino que ha cambiado (importante: no vamos a poder usar operadores de incremento con LiveData y debemos trabajar las operaciones aritmeticas asegurando que su valor NO va a ser nulo con "!!"):

Pasamos entonces de:

```kotlin
// Reset:
fun resetScore() {
    localScoreInt.value = 0
    visitantScoreInt.value = 0
}

fun minusOne(local: Boolean) {
    if (local) {
        --localScoreInt
    } else {
        --visitantScoreInt
    }
}
```

a:

```kotlin
// Reset:
fun resetScore() {
    localScoreInt.value = 0
    visitantScoreInt.value = 0
}

fun minusOne(local: Boolean) {
    if (local) {
        localScoreInt.value = localScoreInt.value!! - 1
    } else {
        visitantScoreInt.value = visitantScoreInt.value!! - 1
    }
}
```

O en su defecto (ultima versión armada)

```kotlin
    // Reset:
    fun resetScore() {
        localScoreInt.value = 0
        visitantScoreInt.value = 0
    }

    fun minusOne(local: Boolean) {
        if (local) {
            // Ejecuto el minus solo si el value de localScoreInt no es nulo (safe call)
            localScoreInt.value = localScoreInt.value?.minus(1)
        } else {
            visitantScoreInt.value = visitantScoreInt.value?.minus(1)
        }
    }
```

Aquí terminamos de configura nuestro "MainViewModel". Ahora vamos a "ActivityMain": 

1- Insertamos dentro del **metodo "on create"**:

```kotlin
        viewModel.localScoreInt.observe(this, Observer { 
                localScoreValue: Int -> // Lambda que cuando observa que localScore cambió nos va a traer ese valor hacía "this" en la variable localScoreValue
            binding.localScoreText.text = localScoreValue.toString()
        }) // El owner debe ser "algo" que tenga un lifecycle como la activity, que justamente es donde estamos ingresando este codigo. 
```

Hacemos lo mismo con visitanScoreValue: 

```kotlin
        viewModel.visitantScoreInt.observe(this, Observer { 
                visitantScoreValue: Int -> // Lambda que cuando observa que localScore cambió nos va a traer ese valor hacía "this" en la variable localScoreValue
            binding.visitantScoreText.text = visitantScoreValue.toString()
        }) // El owner debe ser "algo" que tenga un lifecycle como la activity, que justamente es donde estamos ingresando este codigo. 
```

**Al tener los "Observer" encendidos ya no es necesario que modifiquemos los .text cuando haya un cambio dado que eso lo va a hacer solo la lambda del "viewMode.{variable}.observe{owner, Observer}**
