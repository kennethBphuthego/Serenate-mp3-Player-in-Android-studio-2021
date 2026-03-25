package com.example.serenate_20;

public class MusicFiles{

  private String Path;
  private String Title;
  private String Artist;
  private String Album;
  private String Duration;
  private String Id;
   

   public MusicFiles(String Path,String Title,String Artist,String Album,String Duration,String Id){

         this.Path = Path;
         this.Title = Title;
         this.Artist = Artist;
         this.Album = Album;
         this.Duration = Duration;
         this.Id = Id;
     }

   public MusicFiles()
       {
       }

   public String getPath(){
       return Path;
       }

   public void setPath(String Path){
       this.Path = Path;
      }

   public String getTitle(){
       return Title;
       }

   public void setTitle(String Title){
       this.Title = Title;
      }

   public String getArtist(){
       return Artist;
       }

   public void setArtist(String Artist){
       this.Artist = Artist;
      }

   public String getAlbum(){
       return Album;
       }

   public void setAlbum(String Album){
       this.Album = Album;
      }

   public String getDuration(){
       return Duration;
       }

   public void setDuration(String Duration){
       this.Duration = Duration;
      }

   public String getId(){
       return Id;
       }

   public void setId(String Id) {
       this.Id = Id;
   }

}