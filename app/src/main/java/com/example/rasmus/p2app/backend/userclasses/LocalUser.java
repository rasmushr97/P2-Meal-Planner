package com.example.rasmus.p2app.backend.userclasses;

import com.example.rasmus.p2app.cloud.DBHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
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
    private int age;
    private LocalDate birthday;
    private int height;
    private double weight;
    private double goalWeight;
    private int calorieDeficit;
    private int caloriesPerDay;
    private double exerciseLvl = 1.375;
    private boolean isMale;
    private Goal goal = new Goal();
    private Preferences preferences;

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
        return this.weight / ((this.height / 100) * (this.height / 100));
    }

    public void calcAge() {
       this.age = (int) YEARS.between(this.birthday, LocalDate.now());
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public int getCalorieDeficit() {
        return calorieDeficit;
    }

    public void setCalorieDeficit(int calorieDeficit) {
        this.calorieDeficit = calorieDeficit;
    }

    public boolean isMale() {
        return this.isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public double getExerciseLvl() {
        return exerciseLvl;
    }

    public void setExerciseLvl(double exerciseLvl) {
        this.exerciseLvl = exerciseLvl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public static void main(String argv[]) {
        LocalUser localUser = new LocalUser();
        DBHandler.createCon();
        /*localUser.setBirthday(LocalDate.now());
        localUser.setHeight(200);
        localUser.setWeight(70);
        localUser.updateXML(localUser); */
        localUser = localUser.initialize(1);
        System.out.println(localUser.toString());
        System.out.println(Goal.getUserWeight().values());
        DBHandler.closeCon();
    }

    public LocalUser initialize(int ID){
        LocalUser localUser = new LocalUser();
        try {
            File userXML = new File("app\\src\\main\\res\\xml\\localuser_data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(userXML);

            /* Makes the format clean */
            doc.getDocumentElement().normalize();

            /* Chooses localuser, and 0 is the first localuser */
            NodeList nList = doc.getElementsByTagName("localuser");
            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                /* Gets all data from localuser_data.xml */
                localUser.setHeight(Integer.parseInt(eElement.getElementsByTagName("height").item(0).getTextContent()));
                localUser.setWeight(Double.parseDouble(eElement.getElementsByTagName("weight").item(0).getTextContent()));
                localUser.setBirthday(LocalDate.parse(eElement.getElementsByTagName("birthday").item(0).getTextContent()));
                localUser.calcAge();
                //localUser.setGoalWeight(Double.parseDouble(eElement.getElementsByTagName("goalweight").item(0).getTextContent()));
                int isMale = Integer.parseInt(eElement.getElementsByTagName("isMale").item(0).getTextContent());
                switch (isMale){
                    case 0: localUser.setMale(false); break;
                    case 1: localUser.setMale(true); break;
                }

                /* Calculates the users daily calorie intake */
                localUser.getGoal().calcCaloriesPerDay(localUser);

                /* Gets previous weight measurements from database */
                DBHandler.getLocalUser(ID, localUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localUser;
    }


    public void updateXML(LocalUser localUser){
        try {
            String filepath = "app\\src\\main\\res\\xml\\localuser_data.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

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

            /* Writes everything to the XML file */
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            System.out.println("Done");

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
