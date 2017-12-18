package pdm_1718i.yamda.ui.fragments

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.services.DBSyncJob

/*
    A aplicação	 inclui	 também	 um	 ecrã	 onde	 o	 utilizador	 especifica

    - circunstâncias em que a réplica local dos dados pode ser actualizada (
        -->periodicidade --> Abrir janela nova, com NumberPicker
        -->tipo de conectividade a usar (e.g. apenas Wi-Fi ou qualquer) X

    - se pretende ou não receber notificações de estreias de filmes seguidos.

    - O utilizador especifica também o limiar de percentagem de bateria a partir do qual não são realizadas comunicações
        automáticas (modo de poupança de energia). Estas preferências são armazenadas de forma
        persistente	através	da framework SharedPreferences. (!!!So apartir do android v26 (Oreo) )
 */

class SettingsFragment : PreferenceFragment() {

    override fun onCreate (savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        val syncData : Preference = findPreference(R.string.preference_sync_data_key.toString())

        syncData.setOnPreferenceClickListener {
            DBSyncJob.schedule()
            true
        }
    }
}