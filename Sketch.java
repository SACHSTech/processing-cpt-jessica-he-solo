import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {


    // Loading in image variables 
    PImage imgBackground;
    PImage imgVanillaIceCream;
    PImage imgChocolateIceCream;
    PImage imgStrawberryIceCream;
    PImage imgKitchen;
    PImage imgFrontDesk;
    PImage imgTopping;
    PImage imgScoop;
    PImage imgCherry;
    PImage imgOreo;
    PImage imgSprinkles;
    PImage imgAngry;
    PImage imgHappy;

    // Setting locations to false 
    boolean inKitchen = false;
    boolean inFrontDesk = false;
    boolean customOrderGenerated = false;
    boolean inStoreFront = false;
    boolean inTopping = false;
    boolean level2Unlocked = false;
    enum PlaySequence {
        STORE,
        FRONT_DESK,
        KITCHEN,
        TOPPINGS,
        END_RESULT
    }
    PlaySequence showScreen = PlaySequence.STORE;

    String customerOrder = "";
    String[] possibleFlavors = {"Vanilla", "Chocolate", "Strawberry"};
    String[] possibleToppings = {"Cherry", "Sprinkles", "Oreo Crumbs"};
    
    // Location of icecream flavor 
    PImage selectedFlavorImage;
    int scoopX = 0;  // X-coordinate for the flavor image
    int scoopY = 0;  // Y-coordinate for the flavor image

    ArrayList<PImage> selectedToppings = new ArrayList<>();

    // Position of toppings
    int ellipse1X = 588;
    int ellipseY = 170;
    int ellipse2X = 690;
    int ellipse3X = 790;

    // Colors for ellipses
    int ellipse1Color;  // Color for first ellipse (Oreo)
    int ellipse2Color;  // Color for second ellipse (Sprinkles)
    int ellipse3Color;  // Color for third ellipse (Cherry)

    // Boolean to control displaying ice cream with toppings to false
    boolean displayIceCreamWithToppings = false;

    // Restarts icecream
    int numRestarts;

    public void settings() {
        size(1400, 700);
    }

    public void setup() {
        imgBackground = loadImage("store.jpeg");
        imgVanillaIceCream = loadImage("vanilla.png");
        imgChocolateIceCream = loadImage("chocolate.png");
        imgStrawberryIceCream = loadImage("strawberry.png");
        imgFrontDesk = loadImage("background.jpeg");
        imgKitchen = loadImage("Kitchen.png");
        imgTopping = loadImage("ToppingStation.png");
        imgScoop = loadImage("scoop.png");
        imgCherry = loadImage("Cherry.png");
        imgOreo = loadImage("Oreo.png");
        imgSprinkles = loadImage("Sprinkles.png");
        imgAngry = loadImage("angry.png");
        imgHappy = loadImage("happy.png");
        inStoreFront = true;

        // Set colors for ellipses
        ellipse1Color = color(102, 51, 0);  // oreo
        ellipse2Color = color(128, 56, 186);   // sprinkles
        ellipse3Color = color(255, 0, 0);   // cherry
    }

    public void draw() {
        if (showScreen == PlaySequence.STORE) {
            image(imgBackground, 0, 0, width, height);
            fill(0);
            textSize(24);
            text("Press 0 to enter store", 50, 50);
            text("Game Guide: ", 60, 75);
            text("0 = enter store - generate order", 60,100);
            text("e = enter Kitchen (select icecream flavour)", 60,125);
            text("g = place your icecream selected into topping station)", 60,150);
            text("t = enter Topping station (select toppings)", 60,175);
            text("p = hand to customer, recieve feedback", 60, 200);
            text("b = dispose icecream, restart", 60,225);

        } else if (showScreen == PlaySequence.FRONT_DESK) {
            image(imgFrontDesk, 0, 0, width, height);
            displayFrontDesk();
        } else if (showScreen == PlaySequence.KITCHEN) {
            image(imgKitchen, 0, 0, width, height);
            displayKitchen();
            // Draw selected flavor image
            if (selectedFlavorImage != null) {
                image(selectedFlavorImage, scoopX, scoopY, 150, 150);  // Adjust width and height as needed
            }
        } else if (showScreen == PlaySequence.TOPPINGS) {
            image(imgTopping, 0, 0, width, height);
            displayTopping();  // Display toppings station
            // Draw all selected topping images
            for (PImage topping : selectedToppings) {
                image(topping, 600, 350, 170, 170); 
            }
        } else if (showScreen == PlaySequence.END_RESULT) {
            displayEndResult();  // Display end result screen
        }
        
        // Display ice cream with toppings if true
        if (displayIceCreamWithToppings) {
            drawIceCreamWithToppings();
        }
    }

    public void displayFrontDesk() {
        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        if (!customOrderGenerated) {
            text("Press '0' to generate customer order", width / 2, height / 2);
        } else {
            text("Customer Order: " + customerOrder, width / 2, height / 2);
            text("Press 'e' to go to the kitchen and make the ice cream accordingly", width / 2, height / 2 + 50);
        }
    }

    public void displayKitchen() {
        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        text("Select ice cream flavor and toppings:", 300, 550);
        text("Flavors: v (Vanilla), c (Chocolate), s (Strawberry)", 300, 600);
        text("Press 't' to move onto toppings station", 300, 650 );
        selectedIcecream();
    }

    public void displayTopping() {
        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        text("Press 'g' to reveal the icecream flavour selected", width / 2, 650);
        text("Select the toppings by clicking the circles above", width / 2, 675);
        text("After completed press 'p' to recieve feed back!", 1150, height / 2);

        // Draw ellipse for Oreo 
        fill(ellipse1Color);
        ellipse(ellipse1X, ellipseY, 60, 60);  // Oreo

        // Draw ellipse for Sprinkles
        fill(ellipse2Color);
        ellipse(ellipse2X, ellipseY, 60, 60);  // Sprinkles

        // Draw ellipse for Cherry 
        fill(ellipse3Color);
        ellipse(ellipse3X, ellipseY, 60, 60);  // Cherry

        selectedIcecream();
    }

    public void mousePressed() {
        // Check if mouse is within the area of the first ellipse1 (Oreo)
        if (dist(mouseX, mouseY, ellipse1X, ellipseY) < 30) {  // Adjust radius according to ellipse size
            selectedToppings.add(imgOreo);
        }
        // Check if mouse is within the area of the second ellipse2 (Sprinkles)
        else if (dist(mouseX, mouseY, ellipse2X, ellipseY) < 30) {  // Adjust radius according to ellipse size
            selectedToppings.add(imgSprinkles);
        }
        // Check if mouse is within the area of the third ellipse3 (Cherry)
        else if (dist(mouseX, mouseY, ellipse3X, ellipseY) < 30) {  // Adjust radius according to ellipse size
            selectedToppings.add(imgCherry);
        }
    }
    public void keyPressed() {
        if (key == '0') {
            generateOrder();
            showScreen = PlaySequence.FRONT_DESK;
            inFrontDesk = true;
        } else if (key == 'e') {
            showScreen = PlaySequence.KITCHEN;
            inFrontDesk = false;
            inKitchen = true;
        } else if (key == 't') {
            showScreen = PlaySequence.TOPPINGS;
            inKitchen = false;
            inTopping = true;
        } else if (key == 'g') {
            displayIceCreamWithToppings = true;  
        } else if (key == 'p') {
            if (checkOrderMatch()) {
                showScreen = PlaySequence.END_RESULT;
            } else {
                showScreen = PlaySequence.END_RESULT;
            }
        } else if (key == 'b') {
            restartPreparation();
        }
    }
    public void generateOrder() {
        // Randomly select ice cream flavor
        String[] flavors = {"Vanilla", "Chocolate", "Strawberry"};
        int flavorIndex = (int) random(flavors.length);
        String flavor = flavors[flavorIndex];
    
        // Randomly select toppings without duplicates
        String[] toppings = {"Oreo crumbs", "Sprinkles", "Cherry"};
        ArrayList<String> selectedToppings = new ArrayList<>();
        int numToppings = (int) random(4); // Random number of toppings (0-3)
        StringBuilder toppingsList = new StringBuilder();
        for (int i = 0; i < numToppings; i++) {
            String topping;
            do {
                int toppingIndex = (int) random(toppings.length);
                topping = toppings[toppingIndex];
            } while (selectedToppings.contains(topping)); // stop duplicates
            selectedToppings.add(topping);
            toppingsList.append(topping);
            if (i < numToppings - 1) { 
                toppingsList.append(", ");
            }
        }
        // Set customer order
        if (numToppings > 0) {
            customerOrder = flavor + " with " + toppingsList.toString();
        } else {
            customerOrder = flavor;
        }
    
        customOrderGenerated = true;

    }
    
    public void selectedIcecream() {
        if (key == 'v') {
            selectedFlavorImage = imgVanillaIceCream;
            scoopX = 640;
            scoopY = 400;
        } else if (key == 'c') {
            selectedFlavorImage = imgChocolateIceCream;
            scoopX = 640;
            scoopY = 400;
        } else if (key == 's') {
            selectedFlavorImage = imgStrawberryIceCream;
            scoopX = 640;
            scoopY = 400;
        }
    }
    
    public void drawIceCreamWithToppings() {
        // Draw ice cream flavor
        if (selectedFlavorImage != null) {
            image(selectedFlavorImage, 640, 400, 100, 170);  // Adjust position and size as needed
        }
    
        // Draw all selected topping images
        for (PImage topping : selectedToppings) {
            image(topping, 600, 350, 170, 170);  // Adjust position and size as needed
        }
    }
    
    public void displayEndResult() {
        background(255);
        // Check if the prepared ice cream matches the customer order
        if (checkOrderMatch()) {
            image(imgHappy, 0, 0, width, height);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Congratulations! You made the right order.", width / 2, 300);
            text("Press '0' to start your new order", width / 2, height / 2);
        } else {
            image(imgAngry, 0, 0, width, height);
            textSize(32);
            textAlign(CENTER, CENTER);
            text("The customer is angry. You made the wrong order.", width / 2, 300);
            text("Press '0' to start your new order", width / 2, height / 2);
        }
    }
    
    public boolean checkOrderMatch() {
        // Extract flavor and toppings from customer order
        String[] parts = customerOrder.split(" with ");
        String orderFlavor = parts[0];
        String orderToppings = (parts.length > 1) ? parts[1] : "";
    
        // Check if selected flavor matches customer order
        String selectedFlavor = "";
        if (selectedFlavorImage == imgVanillaIceCream) {
            selectedFlavor = "Vanilla";
        } else if (selectedFlavorImage == imgChocolateIceCream) {
            selectedFlavor = "Chocolate";
        } else if (selectedFlavorImage == imgStrawberryIceCream) {
            selectedFlavor = "Strawberry";
        }
    
        // Check if selected toppings match customer order
        HashSet<String> selectedToppingsSet = new HashSet<>();
        for (PImage topping : selectedToppings) {
            String toppingName = getToppingName(topping);
            selectedToppingsSet.add(toppingName);
        }
    
        // Convert selected toppings to string
        StringBuilder selectedToppingsStr = new StringBuilder();
        for (String topping : selectedToppingsSet) {
            selectedToppingsStr.append(topping).append(", ");
        }
        if (selectedToppingsStr.length() > 2) {
            selectedToppingsStr.setLength(selectedToppingsStr.length() - 2);
        }
    
        // Check if both flavor and toppings match customer order
        return orderFlavor.equals(selectedFlavor) && orderToppings.equals(selectedToppingsStr.toString());
    }
    
    // Helper method to get the name of the topping
    private String getToppingName(PImage topping) {
        if (topping == imgOreo) {
            return "Oreo crumbs";
        } else if (topping == imgSprinkles) {
            return "Sprinkles";
        } else if (topping == imgCherry) {
            return "Cherry";
        }
        return "";  // Default case, shouldn't happen if images are correctly handled
    }
    
    public void restartPreparation() {
        selectedFlavorImage = null;
        selectedToppings.clear();
        displayIceCreamWithToppings = false;
        numRestarts++;
    }
    
    public static void main(String[] args) {
        PApplet.main("Sketch");
    }
}
    