package pdm_1718i.yamda

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesTest {

    @Test
    @Throws(Exception::class)
    fun usingSharedPreferences() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        val TEST_NAME_KEY = "TEST_NAME"
        val TEST_NAME = "Nome_de_teste"

        val TEST_EMAIL_KEY = "TEST_EMAIL"
        val TEST_EMAIL = "EMAIL_DE_TESTE@TESTE.COM"


        val prefs : SharedPreferences = appContext.getSharedPreferences("TEST_SHARED_PREFERENCES", 0)
        val editor : SharedPreferences.Editor = prefs.edit()

        editor.putString(TEST_NAME_KEY, TEST_NAME)
        editor.putString(TEST_EMAIL_KEY, TEST_NAME)
        editor.apply()

        val ret_name : String = prefs.getString(TEST_NAME_KEY, "")
        val ret_email : String = prefs.getString(TEST_EMAIL_KEY, "")

        assert(ret_name == TEST_NAME && ret_email == TEST_EMAIL)
    }

    @Test
    @Throws(Exception::class)
    fun gettingBrokenData(){
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        val TEST_NAME_KEY = "TEST_NAME"
        val TEST_NAME = "Nome_de_teste"

        val TEST_EMAIL_KEY = "TEST_EMAIL"
        val prefs : SharedPreferences = appContext.getSharedPreferences("TEST_SHARED_PREFERENCES", 0)
        val editor : SharedPreferences.Editor = prefs.edit()

        editor.putString(TEST_NAME_KEY, TEST_NAME)
        editor.putString(TEST_EMAIL_KEY, TEST_NAME)
        editor.apply()

        val ret_name : String = prefs.getString(TEST_EMAIL_KEY, "")

        assert(ret_name == "")
    }
}