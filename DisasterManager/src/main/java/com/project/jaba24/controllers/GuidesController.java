package com.project.jaba24.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class GuidesController {
    @FXML
    private ListView<String> disasterListView;

    @FXML
    private ListView<String> instructionListView;

    @FXML
    private TextArea aboutTextArea;

    @FXML
    private TextArea emergencyContactTextArea;

    // Sample data
    private final ObservableList<String> disasters = FXCollections.observableArrayList(
            "Flood", "Drought", "Cyclone", "Tornado", "Volcano", "Wildfire", "Earthquake"
    );

    // Instructions for each disaster
    private final ObservableList<String> earthquakeInstructions = FXCollections.observableArrayList(
            "Drop, Cover, and Hold On",
            "Stay indoors until the shaking stops",
            "Stay away from windows",
            "If outdoors, move to a clear area away from buildings",
            "Check for injuries and provide assistance if needed",
            "Be prepared for aftershocks"
    );
    private final ObservableList<String> floodInstractions = FXCollections.observableArrayList(
            "Move to Higher Ground Immediately",
            "Avoid Walking or Driving Through Flood Waters",
            "Listen to Local Alerts and Warnings",
            "Turn Off Utilities",
            "Be Aware of Potential Electrical Hazards",
            "Move Important Items to Higher Levels",
            "Use Sandbags to Prevent Flooding"
    );
    private ObservableList<String> DroughtInstractions= FXCollections.observableArrayList(
            "Conserve Water",
            "Use Water Wisely",
            "Follow Local Water Restrictions",
            "Plan for Crop Management",
            "Prepare an Emergency Water Supply",
            "Stay Informed"
    );
    private ObservableList<String> CycloneInstractions = FXCollections.observableArrayList(
            "Secure Your Home",
            "Evacuate if Advised",
            "Avoid Flood Waters",
            "Protect Important Documents",
            "Stay Indoors During the Cyclone"
    );
    private ObservableList<String> TornadoInstractions = FXCollections.observableArrayList(
            "Find Shelter Immediately",
            "Avoid Windows",
            "Use Protective Coverings",
            "Stay in Your Shelter Until the Tornado Passes",
            "Be Aware of Secondary Hazards",
            "Provide Assistance if Needed"
    );
    private ObservableList<String> VolcanoInstractions = FXCollections.observableArrayList(
            "Follow Evacuation Orders",
            "Stay Indoors",
            "Avoid Areas Downwind",
            "Protect Against Ash",
            "Listen to Emergency Broadcasts"
    );

    private ObservableList<String> WildfireInstractions = FXCollections.observableArrayList(
            "Create a Defensible Space",
            "Plan Multiple Evacuation Routes",
            "eep Your Vehicle Ready",
            "Protect Your Home",
            "Avoid Smoke Inhalation"
    );

    @FXML
    public void initialize() {
        disasterListView.setItems(disasters);
    }

    @FXML
    public void handleDisasterSelection(MouseEvent event) {
        String selectedDisaster = disasterListView.getSelectionModel().getSelectedItem();
        if (selectedDisaster != null) {
            switch (selectedDisaster) {
                case "Earthquake":
                    aboutTextArea.setText("Earthquakes are sudden movements of the Earth's crust caused by the release of built-up stress along geological faults. They can cause severe damage to buildings, infrastructure, and lead to secondary disasters like tsunamis and landslides.");
                    instructionListView.setItems(earthquakeInstructions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                case "Flood":
                    aboutTextArea.setText("Floods occur when an overflow of water submerges land that is usually dry. Causes include heavy rainfall, river overflow, or dam breakage. Floods can lead to significant property damage and loss of life.");
                    instructionListView.setItems(floodInstractions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                case "Drought":
                    aboutTextArea.setText("Droughts are prolonged periods of abnormally low rainfall, leading to water shortages. They can cause severe agricultural, economic, and environmental impacts. Droughts are often slow-onset disasters, making them harder to detect early.");
                    instructionListView.setItems(DroughtInstractions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                case "Cyclone":
                    aboutTextArea.setText("Cyclones are powerful, rotating storm systems characterized by strong winds, heavy rains, and thunder. They can cause widespread damage due to high winds, heavy rainfall, and storm surges.");
                    instructionListView.setItems(CycloneInstractions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                case "Tornado":
                    aboutTextArea.setText("Tornadoes are rapidly rotating columns of air that extend from a thunderstorm to the ground. They are capable of causing extreme destruction in their path due to high wind speeds and pressure differences.");
                    instructionListView.setItems(TornadoInstractions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                case "Volcano":
                    aboutTextArea.setText("Volcanic eruptions occur when magma from beneath the Earth's crust breaks through the surface. Eruptions can release lava, ash, gases, and pyroclastic flows, causing extensive damage to the environment and posing significant risks to human life.");
                    instructionListView.setItems(VolcanoInstractions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                case "Wildfire":
                    aboutTextArea.setText("Wildfires are uncontrolled fires that spread rapidly through vegetation and forested areas. They are often caused by natural factors like lightning or human activities. Wildfires can destroy homes, wildlife habitats, and agricultural land.");
                    instructionListView.setItems(WildfireInstractions);
                    emergencyContactTextArea.setText("Call Emergency Services: Dial the local emergency number (e.g., 911 in the US) for immediate assistance. Listen to Emergency Broadcasts: Tune in to local radio or TV for updates and instructions.");
                    break;
                default:
                    aboutTextArea.setText("");
                    instructionListView.setItems(FXCollections.observableArrayList());
                    emergencyContactTextArea.setText("");
            }
        }
    }
}
