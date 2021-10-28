package br.edu.ifsp.scl.ads.dados

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.ads.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var geradorRandomico: Random

    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>
    private var range: IntRange = 1..6
    private var numeroDados: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        geradorRandomico = Random(System.currentTimeMillis())

        activityMainBinding.jogarDadoBt.setOnClickListener {
            val resultado1: Int = geradorRandomico.nextInt(range)
            val resultado2: Int = geradorRandomico.nextInt(range)
            val nomeImagem1: String = "dice_${resultado1}"
            val nomeImagem2: String = "dice_${resultado2}"

            settingValueDices(nomeImagem1, nomeImagem2, numeroDados)
            showOrNotDiceTwo(numeroDados)
            showOrNotFaces(range)
            showTextMessageResult(numeroDados, resultado1, resultado2)
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Modificações na minha View
                if (result.data != null) {
                    val configuracao: Configuracao? = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)
                    if (configuracao != null){
                        range = IntRange(1, configuracao.numeroFaces)
                        numeroDados = configuracao.numeroDados
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settingsMi) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }

    private fun settingValueDices(nomeImagem1: String, nomeImagem2: String, numeroDados: Int) {
        if (numeroDados == 2) {
            activityMainBinding.resultadoIv2.setImageResource(
                resources.getIdentifier(nomeImagem2, "mipmap", packageName)
            )
        }
        activityMainBinding.resultadoIv.setImageResource(
            resources.getIdentifier(nomeImagem1, "mipmap", packageName)
        )
    }

    private fun showOrNotDiceTwo(numeroDados: Int) {
        if (numeroDados == 2) {
            activityMainBinding.resultadoIv2.visibility = View.VISIBLE
        } else {
            activityMainBinding.resultadoIv2.visibility = View.GONE
        }
        activityMainBinding.resultadoIv.visibility = View.VISIBLE
    }

    private fun showOrNotFaces(range: IntRange) {
        if (range.last > 6) {
            activityMainBinding.resultadoIv.visibility = View.GONE
            activityMainBinding.resultadoIv2.visibility = View.GONE
        }
    }

    private fun showTextMessageResult(numeroDados: Int, resultado1: Int, resultado2: Int) {
        if (numeroDados == 2) {
            activityMainBinding.resultadoTv.text = "As faces sorteadas foram ${resultado1} e ${resultado2}"
        } else {
            "A face sorteado foi ${resultado1}".also { activityMainBinding.resultadoTv.text = it }
        }
    }
}