package com.example.serenate_20;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles> musicFiles;
    static boolean shuffleBtnBoolean = false;
    static int repeatBtnBoolean = 0;
    static ArrayList<MusicFiles> albums_list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

       permission();

    }

    private void permission() {
       
         if((ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
         != PackageManager.PERMISSION_GRANTED))
             {
               ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
               REQUEST_CODE);
              }
            
         else{
                
                musicFiles = getAllAudio(this); 

               initial_viewPager();
             }

       }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         
          if(requestCode == REQUEST_CODE)
            {
              if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                
                  musicFiles = getAllAudio(this); 
                  initial_viewPager();
                }

              else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
                  }
            }
         }

    private void initial_viewPager() {
      
      ViewPager viewPager1 =findViewById(R.id.view_tracklists);
      TabLayout tabLayout1 =findViewById(R.id.option_tab);

      ViewPagerAdapter viewPagerAdapter1 = new ViewPagerAdapter(getSupportFragmentManager());
      viewPagerAdapter1.addFragments(new TrackFragment(), "Tracks");
      viewPagerAdapter1.addFragments(new AlbumFragment(), "Albums");
      viewPager1.setAdapter(viewPagerAdapter1);
      tabLayout1.setupWithViewPager(viewPager1);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment>fragments;
    private final ArrayList<String>titles;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
       super(fm);
       
       this.fragments= new ArrayList<>();
       this.titles= new ArrayList<>();
     }

     void addFragments(Fragment fragment,String title){
       
       fragments.add(fragment);
       titles.add(title);

     }

    @NonNull
    @Override
    public Fragment getItem(int position){
      return fragments.get(position);
     }

    @Override
    public int getCount(){
      return fragments.size();
     }
 
    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
      return titles.get(position);
     }
        
   }
  
  public static ArrayList<MusicFiles>getAllAudio(Context context){
  
            ArrayList<String> album_duplicates = new ArrayList<>();
            ArrayList<MusicFiles> temporaryAudioList = new ArrayList<>();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {

                      MediaStore.Audio.Media.ALBUM,
                      MediaStore.Audio.Media.TITLE,
                      MediaStore.Audio.Media.DURATION,
                      MediaStore.Audio.Media.DATA,//for path
                      MediaStore.Audio.Media.ARTIST,
                      MediaStore.Audio.Media._ID
                    };

          Cursor cursor = context.getContentResolver().query(uri,projection,
                       null, null, null);

              if(cursor != null){
                    
                    while(cursor.moveToNext()) {
                        
                       String Album = cursor.getString(0);
                       String Title = cursor.getString(1);
                       String Duration = cursor.getString(2);
                       String Path = cursor.getString(3);
                       String Artist = cursor.getString(4);
                       String Id = cursor.getString(5);

                       MusicFiles musicFiles = new MusicFiles(Path,Title,Artist,Album,Duration,Id);

                       Log.e("Path : " + Path, "Album : "+ Album);

                       temporaryAudioList.add(musicFiles);

                       if(!album_duplicates.contains(Album)){

                                  albums_list2.add(musicFiles);
                                  album_duplicates.add(Album);
                                }

                       }
                       cursor.close();
                  }
                   return temporaryAudioList;

      }


 @Override
 public boolean onCreateOptionsMenu(Menu menu3){

         getMenuInflater().inflate(R.menu.search_option_btn, menu3);

         MenuItem menuItem3 = menu3.findItem(R.id.search_btn);
         SearchView searchView3 = (SearchView) menuItem3.getActionView();
         searchView3.setOnQueryTextListener(this);
         return super.onCreateOptionsMenu(menu3);
 }
 @Override
 public boolean onQueryTextSubmit(String query){

        return false;
 }
 @Override
 public boolean onQueryTextChange(String newText3){

        String userInput3 = newText3.toLowerCase(); 
        ArrayList<MusicFiles> mySongs3 = new ArrayList<>();

        for(MusicFiles song3 : musicFiles){

           if(song3.getTitle().toLowerCase().contains(userInput3)){
        
                mySongs3.add(song3);
               }
           }

         TrackFragment.musicAdapter3.listUpdate(mySongs3);
         return true;
 }
 


 
}