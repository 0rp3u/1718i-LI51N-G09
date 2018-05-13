package pdm_1718i.yamda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.PopupMenu;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import pdm_1718i.yamda.data.db.MovieContract;
import pdm_1718i.yamda.model.Movie;
import pdm_1718i.yamda.ui.App;

import static org.junit.Assert.assertEquals;
import static pdm_1718i.yamda.extensions.CalendarExtensionsKt.getDateFromCalendar;

@RunWith(AndroidJUnit4.class)
public class COntentProviderInsertTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
/*
        Movie popular = App.Companion.getMoviesProvider().popularMovies(1).get(0);
        ContentValues cont = new ContentValues();

        cont.put(MovieContract.MovieDetails.INSTANCE.get_ID(),  popular.getId());
        cont.put(MovieContract.MovieDetails.INSTANCE.getIS_FOLLOWING(), false);
        cont.put(MovieContract.MovieDetails.INSTANCE.getPOSTER_PATH(), popular.getPoster_path());
        cont.put(MovieContract.MovieDetails.INSTANCE.getTITLE(), popular.getTitle());
        cont.put(MovieContract.MovieDetails.INSTANCE.getRELEASE_DATE(), getDateFromCalendar(popular.getRelease_date()));
        cont.put(MovieContract.MovieDetails.INSTANCE.getOVERVIEW(), "");
        cont.put(MovieContract.MovieDetails.INSTANCE.getORIGINAL_TITLE(), "title");


        int deleted = appContext.getContentResolver().delete(MovieContract.MovieDetails.INSTANCE.getCONTENT_URI(),null, null );
        Log.d("contentProviderTest0" , "deleted: "+deleted);
        Uri coun = appContext.getContentResolver().insert(MovieContract.MovieDetails.INSTANCE.getCONTENT_URI(),cont );

        Cursor cursor = appContext.getContentResolver().query(
                MovieContract.MovieDetails.INSTANCE.getCONTENT_URI(),
                MovieContract.MovieDetails.INSTANCE.getPROJECT_ALL(),
                "_id=?",
                new String[] { Integer.toString(popular.getId())},
                "");

        if(cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MovieContract.MovieDetails.INSTANCE.get_ID());
            int id = cursor.getInt(index);
            assertEquals(popular.getId(), id);
        }
        cursor.close();

        int deletedCount = appContext.getContentResolver().delete(MovieContract.MovieDetails.INSTANCE.getCONTENT_URI(), " _ID = ? ", new String[] { ""+popular.getId()+" " });

        assertEquals(deletedCount, 1);*/
    }
}
