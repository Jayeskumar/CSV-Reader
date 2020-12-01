package com.example.mycsvappfullcode;

//***THIS FILE WILL SHOW ERRORS UNTIL YOU HAVE COMPLETED THE TASKS YOU ARE REQUIRED TO DO.  WHEN YOU HAVE
//SUCCESSFULLY FINISHED THE TASK THE ERRORS SHOULD ALL BE GONE.***

//androidx.appcompat.app.AppCompatActivity and android.os.Bundle are put in by default when basic
//activity selected when new project is created in Android Studio. All of the other imports where
//put in manually later during the making of the project.
//START
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//END

public class ReadCSV extends AppCompatActivity implements View.OnClickListener{

    //Create objects which will be used to store the name of the files in the Asset Folder, the name
    //of the file that is in the EditText (the input box) when the user presses the Read CSV button,
    //an ArrayList of Strings for the data from the csv file, and a String to store the data from the
    //csv file after the data has been cleaned (put in a way that makes is look more readable when
    //you display it).
    //START
    private String[] theAssetFiles;
    private String fileName;
    private ArrayList<String> theData;
    private String cleanData;
    //END

    //Creating Java equivalent objects for the widgets in our activity which we created
    //in xml that we want to interact with (change) or give values to in some way.
    //START
    private TextView txtFilesHeading;
    private TextView txtAssetFiles;
    private EditText edtFileName;
    private Button btnReadCSV;
    private Button btnShowData;
    private Button btnExitPage;
    private TextView txtFileData;
    //END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_c_s_v);

        //Connect our java objects to the appropriate widget in our user interface
        //START
        txtFilesHeading = findViewById(R.id.txtFilesHeading);
        txtAssetFiles = findViewById(R.id.txtAssetFiles);
        edtFileName = findViewById(R.id.edtFileName);
        btnReadCSV = findViewById(R.id.btnReadCSV);
        btnShowData = findViewById(R.id.btnShowData);
        btnExitPage = findViewById(R.id.btnExitPage);
        txtFileData = findViewById(R.id.txtFileData);
        //END

        //We call the method getAssetList and then set what we receive back as the value of the
        //String[] (String Array) called theAssetFiles.
        //START
        theAssetFiles = getAssetList();
        //END
        //We use a StringBuilder to take each of the file names that are inside in our String[] theAssetFiles
        //(in our case there is only one file name) and put them all in one String. We then set that String
        //as the text that is shown by our TextView called txtAssetFiles.
        //START
        StringBuilder files = new StringBuilder();
        for (int i = 0; i < theAssetFiles.length; i++){
            files.append(theAssetFiles[i]);
            files.append("\n");
        }
        txtAssetFiles.setText(files);
        //END

        //This is setting the instructions of what to do when the button "Read CSV File" is pressed. When
        //we put "this" inside the brackets it is telling the system to use the main onClick method
        //for the onClickListener for this button. You only have one onClick that is not nested ie
        //the onClick is inside the setOnClickListener.
        btnReadCSV.setOnClickListener(this);
        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We use a StringBuilder to take what is in the String cleanData and put in into a StringBulider
                //called list, we then set list as the text that is shown by our TextView called txtAssetFiles.
                //START
                StringBuilder list = new StringBuilder();
                    list.append(cleanData);

                //From API 26 you could use the below line - String.join - to replace what is above this.  We have set
                //a minimum of API 16 for our app so we must use the longer methodology.
                //txtFileData.setText(String.join(" ", theAssetFiles));

                txtFileData.setText(list);
                //END
                //We are making changes to what widgets are visible on our screen.
                //START
                txtFileData.setVisibility(View.VISIBLE);
                btnShowData.setVisibility(View.GONE);
                //END
            }
        });
        //Set on onClickListener to our "Back" button which will exit (and close) this activity and go back to the
        //launcher activity
        //START
        btnExitPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //END

        //We making two widgets that are used when the user presses the button to show the data in the
        //file not visible when the activity is first launched.  We make them available again when the
        //user presses the Show Data button.
        //START
        btnShowData.setVisibility(View.GONE);
        txtFileData.setVisibility(View.GONE);
        //END
    }

    //When the user clicks on the "Read CSV File" button this onClick is triggered.
    public void onClick(View v){
        //We check what is inside the EditText editFileName (the input box), we change it to a String
        //and then we set that as the value of the String fileName.
        fileName = edtFileName.getText().toString();
        //We call the method called readCSV which returns an ArrayList of STrings.  Whe the set that
        //to be the value of our ArrayList called theData.
        theData = readCSV();
        //We call the method called getCleanData and we send it the ArrayList of Strings called theData.
        //This method returned a String and this String is set as the value of our String cleanData.
        cleanData = getCleanData(theData);
        //We make the button "Show File Data" visible so that the user can choose to look at the data
        //from the file if they want.
        btnShowData.setVisibility(View.VISIBLE);
    }

    //This method uses the Asset Manager class in Android to get a list of all of the files inside
    //the folder called "Files" which is inside the Asset Folder.
    //For more information on Asset Manager go to https://developer.android.com/reference/android/content/res/AssetManager
    private String[] getAssetList(){
        Log.d("ASSETS", "getAssetList()");
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("files");
            for(String str : files) { Log.d("ASSETS", str); }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }
        return files;
    }

    //This method reads all of the information from the file with the name that is saved in our String
    //fileName and then returns an ArrayList of Strings with the data. It also puts a Toast (pop up message)
    //on the screen of the app when the information has been read successfully.  Since a CSV file is
    //basically a type of text file this is mostly the same general method that we have used in other
    //projects to get information from text files.
    private ArrayList<String> readCSV(){
            Log.d("DATA", "readCSV");
            ArrayList<String> data = new ArrayList<>();
            AssetManager assetManager = getAssets();
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            String info = null;
            try {
                is = assetManager.open(("files/" + fileName));
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                while ( (info = br.readLine()) != null ) {
                    data.add(info);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            Toast.makeText(getBaseContext(), "Read File Successful",
                Toast.LENGTH_LONG).show();
            for(String str : data) { Log.d("DATA", str); }
            return data;
        }

        //This method is sent an ArrayList of Strings, it uses a toString method to change this ArrayList
        //to a String. it then uses replace and replaceAll methods to make the String look the way we
        //want, that String is then set as the value of a String called dataCleaned.  This String is then
        //sent back to where the method was called.
        private String getCleanData(ArrayList<String> data){
            String dataRaw = data.toString();
            String dataCleaned = dataRaw.replaceAll(" ","\n")
                                        .replaceAll(",","    ")
                                        .replace("[","")
                                        .replace("]","");
            return dataCleaned;
        }

}