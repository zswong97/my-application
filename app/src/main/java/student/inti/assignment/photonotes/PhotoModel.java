package student.inti.assignment.photonotes;

public class PhotoModel {
   public String title;
   public String image;
   public String description;
   public String user;
    public String date;

    public  PhotoModel(){
    }


    public PhotoModel(String mPostTitle, String mPostDescription, String s,String mUser,String mDate) {
        this.title=mPostTitle;
        this.description=mPostDescription;
        this.image=s;
        this.user=mUser;
        this.date=mDate;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }
}

