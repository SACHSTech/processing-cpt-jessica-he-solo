import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {

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

    // Boolean to control displaying ice cream with toppings
    boolean displayIceCreamWithToppings = false;

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
        inStoreFront = true;

        // Set colors for ellipses
        ellipse1Color = color(102, 51, 0);  // Brown color (RGB)
        ellipse2Color = color(128, 56, 186);   // Purple color (RGB)
        ellipse3Color = color(255, 0, 0);   // Red color (RGB)
    }

    public void draw() {
        if (showScreen == PlaySequence.STORE) {
            image(imgBackground, 0, 0, width, height);
            fill(0);
            textSize(24);
            text("Press 0 to enter store", 50, 200);
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
                image(topping, 600, 350, 170, 170);  // Adjust position and size as needed
            }
        } else if (showScreen == PlaySequence.END_RESULT) {
            displayEndResult();  // Display end result screen
        }
        
        // Display ice cream with toppings if flag is true
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
        text("Select ice cream flavor and toppings:", width / 2, 550);
        text("Flavors: v (Vanilla), c (Chocolate), s (Strawberry)", width / 2, 600);
        text("Press 't' to move onto toppings station", width / 2, 650 );
        selectedIcecream();
    }

    public void displayTopping() {
        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        text("After completed click 'g' to serve out the icecream", width / 2, 675);

        // Draw ellipse for Oreo (ellipse1)
        fill(ellipse1Color);
        ellipse(ellipse1X, ellipseY, 60, 60);  // Oreo

        // Draw ellipse for Sprinkles (ellipse2)
        fill(ellipse2Color);
        ellipse(ellipse2X, ellipseY, 60, 60);  // Sprinkles

        // Draw ellipse for Cherry (ellipse3)
        fill(ellipse3Color);
        ellipse(ellipse3X, ellipseY, 60, 60);  // Cherry

        selectedIcecream();
    }

    public void mousePressed() {
        // Check if mouse is within the area of the first ellipse (Oreo)
        if (dist(mouseX, mouseY, ellipse1X, ellipseY) < 30) {  // Adjust radius according to ellipse size
            selectedToppings.add(imgOreo);
        }
        // Check if mouse is within the area of the second ellipse (Sprinkles)
        else if (dist(mouseX, mouseY, ellipse2X, ellipseY) < 30) {  // Adjust radius according to ellipse size
            selectedToppings.add(imgSprinkles);
        }
        // Check if mouse is within the area of the third ellipse (Cherry)
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
            displayIceCreamWithToppings = true;  // Toggle flag to display ice cream with toppings
        } else if (key == 'p') {
            if (checkOrderMatch()) {
                showScreen = PlaySequence.END_RESULT;
            } else {
                showScreen = PlaySequence.END_RESULT;
            }
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
            } while (selectedToppings.contains(topping)); // Avoid duplicates
            selectedToppings.add(topping);
            toppingsList.append(topping);
            if (i < numToppings - 1) { // Add comma if not the last topping
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
    
        // Check if level 2 should be unlocked
        if (!level2Unlocked) {
            level2Unlocked = true;
            text("Congratulations! You may now continue to level 2 where you need to prepare 2 ice creams.", width / 2, height / 2 + 100);
        }
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
        background(255);  // White background for end result
    
        // Check if the prepared ice cream matches the customer order
        if (checkOrderMatch()) {
            fill(0, 255, 0);  // Green color for success message
            textSize(32);
            textAlign(CENTER, CENTER);
            text("Congratulations! You made the right order.", width / 2, height / 2);
        } else {
            fill(255, 0, 0);  // Red color for failure message
            textSize(32);
            textAlign(CENTER, CENTER);
            text("The customer is angry. You get a 0 star.", width / 2, height / 2);
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
            if (topping == imgOreo) {
                selectedToppingsSet.add("Oreo crumbs");
            } else if (topping == imgSprinkles) {
                selectedToppingsSet.add("Sprinkles");
            } else if (topping == imgCherry) {
                selectedToppingsSet.add("Cherry");
            }
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
    
    public static void main(String[] args) {
        PApplet.main("Sketch");
    }
}
    