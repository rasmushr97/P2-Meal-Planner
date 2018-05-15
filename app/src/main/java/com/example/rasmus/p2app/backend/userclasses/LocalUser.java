package com.example.rasmus.p2app.backend.userclasses;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.cloud.DBHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static java.time.temporal.ChronoUnit.YEARS;


public class LocalUser extends User {
    private static int age;
    private static LocalDate birthday;
    private static int height;
    private static double weight;
    private static double goalWeight;
    private static int calorieDeficit;
    private static int caloriesPerDay;
    private static int wantLoseWeight;
    private static double exerciseLvl = 1.375;
    private static boolean isMale;
    private static Goal goal = new Goal();
    private static Preferences preferences;

    @Override
    public String toString() {
        return "LocalUser{" +
                "age=" + age +
                ", birthday=" + birthday +
                ", height=" + height +
                ", weight=" + weight +
                ", goalWeight=" + goalWeight +
                ", calorieDeficit=" + calorieDeficit +
                ", caloriesPerDay=" + caloriesPerDay +
                ", exerciseLvl=" + exerciseLvl +
                ", isMale=" + isMale +
                '}';
    }

    public double calcBMI() {
        double newHeight = (double) height / 100;
        return weight / (newHeight * newHeight);
    }

    public int getWantLoseWeight() {
        return wantLoseWeight;
    }

    public void setWantLoseWeight(int wantLoseWeight) {
        LocalUser.wantLoseWeight = wantLoseWeight;
    }
        //TODO RIGHT
    public void calcAge() {
       age = (int) YEARS.between(birthday, LocalDate.now());
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate newBirthday) {
        birthday = newBirthday;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int newCaloriesPerDay) {
        caloriesPerDay = newCaloriesPerDay;
    }

    public int getCalorieDeficit() {
        return calorieDeficit;
    }

    public void setCalorieDeficit(int newCalorieDeficit) {
        calorieDeficit = newCalorieDeficit;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public double getExerciseLvl() {
        return exerciseLvl;
    }

    public void setExerciseLvl(double newExerciseLvl) {
        exerciseLvl = newExerciseLvl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int newAge) {
        age = newAge;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        weight = newWeight;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(double newGoalWeight) {
        goalWeight = newGoalWeight;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal newGoal) {
        goal = newGoal;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences newPreferences) {
        preferences = newPreferences;
    }


    public void initialize(int ID, AppCompatActivity activity){
        try {
            //InputStream is = activity.getAssets().open("localuser_data.xml");
            File is = activity.getFileStreamPath("localuser_data.xml");


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            /* Makes the format clean */
            doc.getDocumentElement().normalize();

            /* Chooses localuser, and 0 is the first localuser */
            NodeList nList = doc.getElementsByTagName("localuser");
            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                /* Gets all data from localuser_data.xml */
                InRAM.user.setHeight(Integer.parseInt(eElement.getElementsByTagName("height").item(0).getTextContent()));
                InRAM.user.setWeight(Double.parseDouble(eElement.getElementsByTagName("weight").item(0).getTextContent()));
                InRAM.user.setBirthday(LocalDate.parse(eElement.getElementsByTagName("birthday").item(0).getTextContent()));
                InRAM.user.calcAge();
                InRAM.user.setWantLoseWeight(Integer.parseInt(eElement.getElementsByTagName("loseweight").item(0).getTextContent()));
                int isMale = Integer.parseInt(eElement.getElementsByTagName("isMale").item(0).getTextContent());
                switch (isMale){
                    case 0: InRAM.user.setMale(false); break;
                    case 1: InRAM.user.setMale(true); break;
                }

                /* Calculates the users daily calorie intake */
                InRAM.user.getGoal().calcCaloriesPerDay(InRAM.user);

                /* Gets previous weight measurements from database */
                DBHandler.getLocalUser(ID);
                //is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateXML(LocalUser localUser, AppCompatActivity activity){
        try {
            File file = activity.getFileStreamPath("localuser_data.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            /* Get the userNode element by tag name directly */
            Node userNode = doc.getElementsByTagName("localuser").item(0);
            NodeList list = userNode.getChildNodes(); //List of all childnodes

            /* Iterates through the list */
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                /* Updates the node currently selected */
                if ("height".equals(node.getNodeName())) {
                    node.setTextContent("" + localUser.getHeight());
                } else if ("weight".equals(node.getNodeName())) {
                    node.setTextContent("" + localUser.getWeight());
                } else if ("isMale".equals(node.getNodeName())) {
                    if(localUser.isMale()){
                        node.setTextContent("1");
                    } else node.setTextContent("0");
                } else if ("birthday".equals(node.getNodeName())) {
                    node.setTextContent(localUser.getBirthday().toString());
                }
            }
            localUser.getGoal().calcCaloriesPerDay(localUser);

            /* Writes everything to the XML file */
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file.getPath());
            transformer.transform(source, result);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

    }

}
