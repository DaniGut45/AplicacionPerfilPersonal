package com.example.aplicacionperfilpersonal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.aplicacionperfilpersonal.R.id.textViewNivelSatisfaccion
import com.example.aplicacionperfilpersonal.R.id.textViewResultado
import com.example.aplicacionperfilpersonal.R.id.textViewSeekBar


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //Inicio lo que voy a usar
    private lateinit var spinner: Spinner
    private lateinit var editTextNombre: EditText
    private lateinit var editTextApellido: EditText
    private lateinit var editTextCorreoElectronico: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var botonEnviar: Button
    private lateinit var textViewResultado: TextView
    private lateinit var checkBoxMusica: CheckBox
    private lateinit var checkBoxLectura: CheckBox
    private lateinit var checkBoxDeporte: CheckBox
    private lateinit var checkBoxArte: CheckBox
    private lateinit var seekBar: SeekBar
    private lateinit var textViewNivelSatisfaccion: TextView
    private lateinit var switchNotification: Switch
    private val KEY_TEXT_VIEW = "key_text_view"

    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar las vistas
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextApellido = findViewById(R.id.editTextApellido)
        editTextCorreoElectronico = findViewById(R.id.editTextCorreoElectronico)
        radioGroup = findViewById(R.id.radioGroup)
        spinner = findViewById(R.id.spinnerPais)
        textViewResultado = findViewById(R.id.textViewResultado)
        botonEnviar = findViewById(R.id.buttonEnviar)
        checkBoxMusica = findViewById(R.id.checkBoxMusica)
        checkBoxLectura = findViewById(R.id.checkBoxLectura)
        checkBoxDeporte = findViewById(R.id.checkBoxDeporte)
        checkBoxArte = findViewById(R.id.checkBoxArte)
        seekBar = findViewById(R.id.seekBar)
        textViewNivelSatisfaccion = findViewById(R.id.textViewNivelSatisfaccion)
        switchNotification = findViewById(R.id.Suscripcion)

        // Configurar el Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.paises_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        // Hacer foco en el EditText de nombre
        editTextNombre.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)


        // Configurar el SeekBar
        seekBar.max = 10
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textViewNivelSatisfaccion.text = "Valor: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        //Vamos a guardar el textViewResultado para que si el movil da la vuelta siga apareciendo
        // Verificamos si existe un estado guardado
        if (savedInstanceState != null) {
            // Recuperamos y establecemos el texto guardado del TextView
            val savedText = savedInstanceState.getString(KEY_TEXT_VIEW) // Asegúrate de usar la clave correcta
            textViewResultado.text = savedText // Usar la propiedad 'text'
        }


        // Configurar el Listener del botón
        botonEnviar.setOnClickListener {
            //Obtengo el valor de los editText
            val nombreTexto = editTextNombre.text.toString()
            val apellidoTexto = editTextApellido.text.toString()
            val correoTexto = editTextCorreoElectronico.text.toString()

            // Verifico si algún campo esta vacio y si no lanzo un mensaje
            if (nombreTexto.isEmpty() || apellidoTexto.isEmpty() || correoTexto.isEmpty()) {
                // Muestro un mensaje de error si hay campos vacíos
                Toast.makeText(this, "Completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifico que el nombre y el apellido solo contengan letras
            if (!nombreTexto.all { it.isLetter() } || !apellidoTexto.all { it.isLetter() }) {
                Toast.makeText(this, "El nombre y el apellido deben contener solo letras.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifico si el correo tiene un formato válido y si no lanzo un mensaje
            val correoValido = correoTexto.contains("@") && correoTexto.contains(".") &&
                    correoTexto.indexOf("@") < correoTexto.lastIndexOf(".")

            if (!correoValido) {
                Toast.makeText(this, "Ingresa un correo válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Obtenego el género seleccionado
            val selectedId = radioGroup.checkedRadioButtonId
            val genero = findViewById<RadioButton>(selectedId).text.toString()

            // Inicio una cadena y voy añadiendo y esta checkeado el checkBox
            var interesesTexto = ""
            if (checkBoxMusica.isChecked) {
                interesesTexto += "Música "
            }
            if (checkBoxLectura.isChecked) {
                interesesTexto += "Lectura "
            }
            if (checkBoxDeporte.isChecked) {
                interesesTexto += "Deporte "
            }
            if (checkBoxArte.isChecked) {
                interesesTexto += "Arte"
            }
            // Si no hay intereses seleccionados, establecer un valor predeterminado
            if (interesesTexto.isEmpty()) {
                interesesTexto = "Ninguno"
            }

            //Obtengo el pais
            val pais = spinner.selectedItem.toString()

            // Obtengo el estado del Switch
            val suscripcion = if (switchNotification.isChecked) {
                "Está Suscrito"
            } else {
                "No está suscrito"
            }

            textViewResultado.text = """
                Nombre: $nombreTexto
                Apellido: $apellidoTexto
                Correo Electrónico: $correoTexto
                Género: $genero
                Nacionalidad: $pais
                Intereses: $interesesTexto
                Nivel de Satisfacción: ${seekBar.progress}
                Suscripción: $suscripcion
            """
            }
        }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    // Método llamado antes de que la actividad sea destruida, por ejemplo, al rotar la pantalla
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guardamos el texto actual del TextView en el Bundle
        outState.putString(KEY_TEXT_VIEW, textViewResultado.text.toString()) // Usar la propiedad 'text'
    }
}






