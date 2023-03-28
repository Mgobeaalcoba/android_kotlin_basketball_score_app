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

-----------------------------------

## **Encapsulamiento de los ViewModels**

Problema: Desde MainActivity seguimos pudiendo editar los valores de nuestras variables localScoreInt y visitantScoreInt dado que no están encapsuladas. El encapsulamiento implica que determinados atributos, variables y metodos de una clase solo puedan ser accedidos desde la clase unicamente y no desde otro archivo del proyecto. 

Por eso vamos a adaptar el codigo para encapsular como corresponde: 

1- En MainViewModel vamos a declarar a nuestras **"var" como "private var"**
2- Vamos a **sumarles delante un "_" (guion bajo)** a todas las "var" que queremos encapsular. Esto es una convención: los MutableLiveData privados deben empezar con "_"

*Truco: En Android Studio e Intellij IDEO con "Shift+F6" selecciono todas las veces que aparece una variable (o cualquier otro termino) dentro del documento. Luego de la edición "Enter"*

3- Creo **"val"´s adicionales del tipo "LiveData**" (las normales, solo legibles, pero no editables) para que estan sean accedidas por los demas documentos. Las mismas solo podrán leerle pero no editarse por tratarse de "val"´s y de ser no mutables por default. 

**¡¡¡Listo!!!**

Con esto ya no se podrá acceder a los LiveData editables desde otro archivo y solo podrán trabajarse desde MainViewModel respetando las reglas del encapsulamiento. 

----------------------------------

## **Usando el ViewModel dentro del Data Binding**

Dentro la sección "data" del "layout" (el que sea que hayamos escogido), si recuerdan bien, se pueden agregar variables. 

Vamos entonces a agregar el ViewModel como una variable dentro del layout para consumir la información desde el mismo. 

1- seteamos la variable en "activity_main.xml": 

```xml
    <data>
        <variable
            name="mainViewModel"
            type="com.mgobeaalcoba.basketballscore.MainViewModel" />
    </data>
```
Con el viewModel acá podemos pasar bastante codigo de MainActivity...

**Vamos a pasar los "onClickListener" desde el MainActivity hacia el layout "activity_main.xml"**

*¿Como va a suceder esto?* Mediante la ejecución de una función lambda que pondremos en el .xml y que se va a ejecutar cuando el usuario haga click en cualquiera de nuestros botones.

Con esto eliminamos de nuestro main toda la **"private fun setupButtons() {}"**

Y agredamos en todos los views que sean botones:

**android:onClick="@{() -> {ViewModel}.{Metodo del ViewModel}()}"**

Fundamental para que todo esto funcione: 

**Debemos asignar nuestra instancia de viewModel a binding.mainViewModel para que sepa que objeto de la clase mainViewModel es el que tiene que leer.**

**¿Como queda entonces nuestro .xml?**

*Notas: El boton de resultsButton lo voy a seguir manejando desde el MainActivity dado que es el que conecta con la otra Activity y no lo tengo en el MainViewModel.* 

*Por otro lado, el botón de minusOne tambien lo voy a dejar en el main. Dado que con esa función en el MainViewModel retorno un booleano que me permité entender si debo o no mostrar un toast al usuario y un log al developer y este retorno no lo podría aprovechar desde el activity_main.xml*

asi es que: 

**¿Como queda el activity_main.xml?**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="mainViewModel"
            type="com.mgobeaalcoba.basketballscore.MainViewModel" />
    </data>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp"
        tools:context=".MainActivity">
        
        <TextView
            android:id="@+id/local_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:layout_marginTop="12dp"
            android:gravity="center"
            android:text="@string/local"
            android:textColor="@color/black"
            android:textSize="40sp"
            android:textStyle="bold"/>

        <LinearLayout
            android:id="@+id/upper_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/local_text"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            android:layout_marginTop="16dp"
            android:gravity="center"
            android:orientation="horizontal">

            <Button
                android:id="@+id/local_minus_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textColor="@color/black"
                android:text="@string/minus_one"
                android:textSize="24sp" />

            <TextView
                android:id="@+id/local_score_text"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:text="@string/points_local"
                android:textColor="@color/black"
                android:textSize="72sp"
                tools:text="62" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <Button
                    android:id="@+id/local_plus_button"
                    android:onClick="@{() -> mainViewModel.plusOne(true)}"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="@color/black"
                    android:text="@string/plus_one"
                    android:textSize="24sp" />

                <Button
                    android:id="@+id/local_two_points_button"
                    android:onClick="@{() -> mainViewModel.plusTwo(true)}"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="@color/black"
                    android:text="@string/plus_two"
                    android:textSize="24sp" />
            </LinearLayout>
        </LinearLayout>

        <LinearLayout
            android:id="@+id/center_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:gravity="center_vertical"
            android:orientation="horizontal"
            android:layout_below="@id/upper_layout"
            android:layout_marginTop="32dp"
            android:padding="16dp">

            <ImageButton
                android:id="@+id/restart_button"
                android:onClick="@{() -> mainViewModel.resetScore()}"
                android:layout_width="72dp"
                android:layout_height="72dp"
                android:padding="16dp"
                android:textColor="@color/black"
                android:text="Prueba"
                android:src="@drawable/ic_restore_black" />

            <ImageView
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_weight="1"
                android:gravity="center"
                android:src="@drawable/basket_ball"
                android:textColor="@color/black"
                android:textSize="60sp"
                tools:text="62" />

            <ImageButton
                android:id="@+id/results_button"
                android:layout_width="72dp"
                android:layout_height="72dp"
                android:padding="16dp"
                android:src="@drawable/ic_arrow_right_black" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/down_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/center_layout"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            android:layout_marginTop="32dp"
            android:gravity="center"
            android:orientation="horizontal">

            <Button
                android:id="@+id/visitant_minus_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textColor="@color/black"
                android:text="@string/minus_one"
                android:textSize="24sp" />

            <TextView
                android:id="@+id/visitant_score_text"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:text="@string/points_visitant"
                android:textColor="@color/black"
                android:textSize="72sp"
                tools:text="62" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <Button
                    android:id="@+id/visitant_plus_button"
                    android:onClick="@{() -> mainViewModel.plusOne(false)}"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="@color/black"
                    android:text="@string/plus_one"
                    android:textSize="24sp" />

                <Button
                    android:id="@+id/visitant_two_points_button"
                    android:onClick="@{() -> mainViewModel.plusTwo(false)}"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textColor="@color/black"
                    android:text="@string/plus_two"
                    android:textSize="24sp" />
            </LinearLayout>
        </LinearLayout>

        <TextView
            android:id="@+id/visitant_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:layout_below="@+id/down_layout"
            android:layout_marginTop="16dp"
            android:gravity="center"
            android:text="Visitante"
            android:textColor="@color/black"
            android:textSize="40sp"
            android:textStyle="bold"/>

    </RelativeLayout>

</layout>
```

**¿Como queda entonces el MainActivity?**

```kotlin
package com.mgobeaalcoba.basketballscore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mgobeaalcoba.basketballscore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargamos nuestro data binding:
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instanciamos en "onCreate" nuestro viewModel:
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Asigno nuestra instanciación del MainViewModel, que es viewModel como atributo mainViewModel del binding:
        // mainViewModel es la variable que creamos en el layout!!!
        binding.mainViewModel = viewModel

        // Generamos los Observadores de las variables del MainViewModel:
        viewModel.localScoreInt.observe(this, Observer {
                localScoreValue: Int ->
            binding.localScoreText.text = localScoreValue.toString()
        })

        viewModel.visitantScoreInt.observe(this, Observer {
                visitantScoreValue: Int ->
            binding.visitantScoreText.text = visitantScoreValue.toString()
        })

        setupButtons()
    }

    private fun setupButtons() {

        // Decisión: Sigo manejando los metodos de MainViewModel.minusOne y .createMatch por acá
        // dado que minusOne me retorna un booleano que lo uso para lanzar un toast al user y un log
        // al developer. En el caso del .createMatch es una función que está acá y no en el MainViewModel

        // Equipo Local:
        binding.localMinusButton.setOnClickListener {
            val exeToast = viewModel.minusOne(true)
            if (exeToast) {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        // Equipo visitante:
        binding.visitantMinusButton.setOnClickListener {
            val exeToast = viewModel.minusOne(false)
            if (exeToast) {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.resultsButton.setOnClickListener {
            openDetailActivity(viewModel.createMatch())
        }

    }

    private fun openDetailActivity(matchScore : MatchScore) {
        // Creamos un intent activity:
        val intent = Intent(this, ScoreActivity::class.java) // Explicit intent

        // Solo voy a pasar como putExtra el objeto matchScore:
        intent.putExtra(ScoreActivity.MATCH_SCORE_KEY, matchScore)

        // Enviamos el objeto matchScore a la siguiente activity:
        startActivity(intent)
    }

}
```

**¿Como queda el MainViewModel?**

```kotlin
package com.mgobeaalcoba.basketballscore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _localScoreInt: MutableLiveData<Int> = MutableLiveData()
    private var _visitantScoreInt: MutableLiveData<Int> = MutableLiveData()

    val localScoreInt: LiveData<Int>
        get() = _localScoreInt

    val visitantScoreInt: LiveData<Int>
        get() = _visitantScoreInt

    // Creamos una función init que se ejecuta apenas se instancia un MainViewModel
    init {
        resetScore() // Nos aseguramos que los valores van a arrancar en cero
    }

    // Reset:
    fun resetScore() {
        _localScoreInt.value = 0
        _visitantScoreInt.value = 0
    }

    fun minusOne(local: Boolean) : Boolean {
        var needToast = false
        if (local) {
            // Solo reduzco si es distinto de cero.
            if (_localScoreInt.value != 0) {
                // Ejecuto el minus solo si el value de localScoreInt no es nulo (safe call)
                _localScoreInt.value = _localScoreInt.value?.minus(1)
            } else {
                needToast = true
            }
        } else {
            // Solo reduzco si es distinto de cero.
            if (_visitantScoreInt.value != 0) {
                // Ejecuto el minus solo si el value de localScoreInt no es nulo (safe call)
                _visitantScoreInt.value = _visitantScoreInt.value?.minus(1)
            } else {
                needToast = true
            }
        }
        return needToast
    }

   fun plusOne(local: Boolean) {
        if (local) {
            _localScoreInt.value = _localScoreInt.value?.plus(1)
        } else {
            _visitantScoreInt.value = _visitantScoreInt.value?.plus(1)
        }
    }

    fun plusTwo(local: Boolean) {
        if (local) {
            _localScoreInt.value = _localScoreInt.value?.plus(2)
        } else {
            _visitantScoreInt.value = _visitantScoreInt.value?.plus(2)
        }
    }

    fun createMatch() :MatchScore {
        return MatchScore(_localScoreInt.value!!, _visitantScoreInt.value!!)
    }

}
```













