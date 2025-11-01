package com.project.jaba24.controllers;
import com.project.jaba24.DAO.ImpUser;
import com.project.jaba24.MainApplication;
import com.project.jaba24.business.Disaster;
import com.project.jaba24.business.Jobs;
import com.project.jaba24.business.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Console;
import java.io.IOException;
import com.project.jaba24.business.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class dashBoardController {
    @FXML
    public ComboBox disasterTypeFilter;
    @FXML
    public TextField countryFilter;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    @FXML
    public Button accountButton;
    @FXML
    public Button applyFilterBtn;
    @FXML
    public Button disasterTab;
    @FXML
    public Button alertTab;
    @FXML
    public Button jobTab;
    @FXML
    public VBox filteroptions;
    @FXML
    public ListView jobsListView;
    @FXML
    public VBox humanitarianJobsDetails;
    @FXML
    public Button guideTab;
    @FXML
    private WebView webView;
    @FXML
    private ListView<Disaster> disasterListView;
    private String selectedTab;
    private ObservableList<Disaster> disastersObservableList;
    private ObservableList<Jobs> jobsObservableList;
    private static final Preferences preferences = Preferences.userNodeForPackage(MainApplication.class);
    private final ImpUser userDAO = new ImpUser();


    @FXML
    public void initialize() throws IOException {
        String email = preferences.get("email", "");
        User user = userDAO.getUserByEmail(email);
        accountButton.setText(String.valueOf(user.getUsername().toUpperCase().charAt(0)));

        disasterTypeFilter.getItems().addAll("All", "Flood", "Drought", "Cyclone", "Tornado", "Volcano", "Wildfire", "Earthquake");
        disasterTypeFilter.setValue("All");
        // Initialize the observable list View
        disastersObservableList = FXCollections.observableArrayList();
        jobsObservableList = FXCollections.observableArrayList();
        // Bind the observable list to the list view
        disasterListView.setItems(disastersObservableList);
        jobsListView.setItems(jobsObservableList);
        disasterListView.setCellFactory(listView -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster disaster, boolean empty) {
                super.updateItem(disaster, empty);
                if (empty || disaster == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    hBox.getStyleClass().add("disaster-box");
                    VBox vBox = new VBox();
                    Text title = new Text(disaster.getName());
                    title.getStyleClass().add("disaster-title");
                    Text status = new Text(disaster.getStatus());
                    status.getStyleClass().add("disaster-status");
                    Text date = new Text(disaster.getDate().toString());
                    date.getStyleClass().add("disaster-date");
                    Button pinButton = new Button("Pin");
                    pinButton.getStyleClass().add("pin-button");
                    pinButton.setOnAction(event -> pinToMap(disaster));

                    vBox.getChildren().addAll(title, date, status, pinButton);
                    hBox.getChildren().addAll(vBox, new Region());  // Ensure the button stays at the right
                    setGraphic(hBox);
                }
            }
        });
        jobsListView.setCellFactory(listView -> new ListCell<Jobs>() {
            @Override
            protected void updateItem(Jobs job, boolean empty) {
                super.updateItem(job, empty);
                if (empty || job == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    hBox.getStyleClass().add("job-box");

                    VBox vBox = new VBox();
                    Text title = new Text(job.getTitle());
                    title.getStyleClass().add("job-title");

                    HBox locationBox = new HBox();
                    Text locationLabel = new Text("Location: ");
                    locationLabel.getStyleClass().add("job-variables");
                    Text locationContent = new Text(job.getCity() + ", " + job.getCountry());
                    locationContent.getStyleClass().add("job-content");
                    locationBox.getChildren().addAll(locationLabel, locationContent);

                    HBox statusBox = new HBox();
                    Text statusLabel = new Text("Status: ");
                    statusLabel.getStyleClass().add("job-variables");
                    Text statusContent = new Text(job.getStatus());
                    statusContent.getStyleClass().add("job-content");
                    statusBox.getChildren().addAll(statusLabel, statusContent);

                    HBox createdDateBox = new HBox();
                    Text createdDateLabel = new Text("Creating Date: ");
                    createdDateLabel.getStyleClass().add("job-variables");
                    Text createdDateContent = new Text(job.getCreated_at().toString());
                    createdDateContent.getStyleClass().add("job-content");
                    createdDateBox.getChildren().addAll(createdDateLabel, createdDateContent);

                    HBox closingDateBox = new HBox();
                    Text closingDateLabel = new Text("Closing Date: ");
                    closingDateLabel.getStyleClass().add("job-variables");
                    Text closingDateContent = new Text(job.getClosing_date().toString());
                    closingDateContent.getStyleClass().add("job-content");
                    closingDateBox.getChildren().addAll(closingDateLabel, closingDateContent);

                    Button pinButton = new Button("Pin");
                    pinButton.getStyleClass().add("pin-button");
                    pinButton.setOnAction(event -> pinToMap(job));

                    vBox.getChildren().addAll(title, locationBox, statusBox, createdDateBox, closingDateBox, pinButton);

                    Region region = new Region();
                    HBox.setHgrow(region, Priority.ALWAYS);

                    hBox.getChildren().addAll(vBox, region);
                    setGraphic(hBox);
                }
            }
        });

        loadDisasters();
        List<Disaster> onGoingDisasters = getDisastersByUserCountry();
        if(!onGoingDisasters.isEmpty()) {
            MainApplication.showAlertWindow();
        }

    }

    private void pinToMap(Disaster disaster) {
        String mapHtml = updateMapWithCoordinates(disaster);
        webView.getEngine().loadContent(mapHtml);
    }

    private void pinToMap(Jobs job){
        String mapHtml = updateMapWithCoordinates(job);
        webView.getEngine().loadContent(mapHtml);
    }
    private List<Disaster> getDisastersByUserCountry() {
        List<Disaster> OnGoingDisasters = new ArrayList<>();
        User user = userDAO.getUserByEmail(preferences.get("email", ""));
        if(user == null) {
            return OnGoingDisasters;
        }
        List<Disaster> disasters = fetchDisastersData(
                "All",
                user.getLocation(),
                null,
                null
        );
        OnGoingDisasters = disasters.stream()
                .filter(disaster -> "ongoing".equalsIgnoreCase(disaster.getStatus()))
                .collect(Collectors.toList());

        return OnGoingDisasters;
    }

    private void loadDisasters() {
        disastersObservableList.clear();
        List<Disaster> disasters = fetchDisastersData(
                disasterTypeFilter.getValue().toString(),
                null,
                null,
                null
        );
        disastersObservableList.addAll(disasters);
        String mapHtml = updateMapWithDisasters(disasters);
        webView.getEngine().loadContent(mapHtml);
    }

    private List<Disaster> fetchDisastersData(
            String disasterType,
            String country,
            String startDate,
            String endDate
    ) {
        List<Disaster> disasters = new ArrayList<>();
        StringBuilder response = new StringBuilder();
        StringBuilder urlString = new StringBuilder(
                "https://api.reliefweb.int/v1/disasters?appname=jaba24&limit=20&sort[]=date:desc"
        );

        try {
            if (!disasterType.equals("All")) {
                urlString.append("&query[value]=").append(disasterType);
            }
            if (country != null && !country.isEmpty()) {
                urlString.append("&filter[field]=country&filter[value]=").append(country);
            }
            if (startDate != null && endDate != null) {
                urlString
                        .append("&filter[field]=date.created&filter[value][from]=")
                        .append(startDate).append("T00:00:00%2B00:00")
                        .append("&filter[value][to]=")
                        .append(endDate)
                        .append("T00:00:00%2B00:00");
            }


            URL url = new URL(urlString.toString());
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode dataNodes = rootNode.get("data");
            for (JsonNode node : dataNodes) {
                URL disasterUrl = new URL(node.get("href").asText());
                HttpURLConnection disasterConn = (HttpURLConnection) disasterUrl.openConnection();
                disasterConn.setRequestMethod("GET");

                StringBuilder disasterResponse = new StringBuilder();
                BufferedReader disasterReader = new BufferedReader(new InputStreamReader(disasterConn.getInputStream()));
                String disasterLine;
                while ((disasterLine = disasterReader.readLine()) != null) {
                    disasterResponse.append(disasterLine);
                }
                disasterReader.close();
                JsonNode disasterNode = objectMapper.readTree(disasterResponse.toString());
                JsonNode disasterFields = disasterNode.path("data").get(0).path("fields");
                long id = disasterFields.path("id").asLong();
                String name = disasterFields.path("name").asText("No name provided");
                String description = disasterFields.path("description").asText("No description available");
                String status = disasterFields.path("status").asText("Unknown status");
                JsonNode countryArray = disasterFields.path("country");
                String countryValue = "Unknown Country";
                Location location = new Location(0.0, 0.0);
                if (countryArray.isArray() && !countryArray.isEmpty()) {
                    JsonNode primaryCountry = countryArray.get(0);
                    countryValue = primaryCountry.path("name").asText("Unknown Country");
                    JsonNode locationNode = primaryCountry.path("location");
                    double latitude = locationNode.path("lat").asDouble(0.0);
                    double longitude = locationNode.path("lon").asDouble(0.0);
                    location = new Location(latitude, longitude);
                }
                String date = disasterFields.path("date").path("event").asText("Unknown Date");
                Disaster disaster = new Disaster(id, name, description, status, countryValue, location, date);
                disasters.add(disaster);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return disasters;
    }

    private List<Jobs> fetchJobs(){
        User user = userDAO.getUserByEmail(preferences.get("email", ""));
        List<Jobs> jobs = new ArrayList<>();
        StringBuilder response = new StringBuilder();
        String urlString =
                "https://api.reliefweb.int/v1/jobs?appname=jaba24&limit=20&sort[0]=date:desc&filter[field]=country&filter[value]="
                        + user.getLocation();
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode dataNodes = rootNode.get("data");
            for (JsonNode node : dataNodes) {
                URL job_url = new URL(node.get("href").asText());
                HttpURLConnection job_connection = (HttpURLConnection) job_url.openConnection();
                job_connection.setRequestMethod("GET");
                BufferedReader job_reader = new BufferedReader(new InputStreamReader(job_connection.getInputStream()));
                StringBuilder job_response = new StringBuilder();
                String job_line;
                while ((job_line = job_reader.readLine()) != null) {
                    job_response.append(job_line);
                }
                job_reader.close();
                JsonNode job_node = objectMapper.readTree(job_response.toString());
                JsonNode job_fields = job_node.path("data").get(0).path("fields");
                long id = job_fields.path("id").asLong();
                String title = job_fields.path("title").asText("No title provided");
                String status = job_fields.path("status").asText("Unknown status");
                String body = job_fields.path("body").asText("No body available");
                String how_to_apply = job_fields.path("how_to_apply-html").asText("No application method available");
                JsonNode cityNode =
                        job_fields.path("city").isArray() && job_fields.path("city").size() > 0 ?
                                job_fields.path("city").get(0) : null;
                String city = cityNode != null ?
                        cityNode.path("name").asText("Unknown city") : "Unknown city";
                JsonNode countryNode = job_fields.path("country").get(0);
                String country = countryNode.path("name").asText("Unknown country");
                JsonNode locationNode = countryNode.path("location");
                Location location = new Location(0.0, 0.0);
                location.setLatitude(locationNode.path("lat").asDouble(0.0));
                location.setLongitude(locationNode.path("lon").asDouble(0.0));
                List<String> career_categories = new ArrayList<>();
                JsonNode careerCategoriesNode = job_fields.path("career_categories");
                if (careerCategoriesNode.isArray()) {
                    for (JsonNode category : careerCategoriesNode) {
                        career_categories.add(category.path("name").asText("Unknown category"));
                    }
                }
                JsonNode date_node = job_fields.path("date");
                String created_at = date_node.path("created").asText("Unknown date");
                String closing_date = date_node.path("closing").asText("Unknown date");
                Jobs job = new Jobs(
                        id,
                        title,
                        status,
                        body,
                        how_to_apply,
                        city,
                        country,
                        location,
                        career_categories,
                        created_at,
                        closing_date
                );
                jobs.add(job);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jobs;
    }
    private String updateMapWithDisasters(List<Disaster> disastersData) {
        StringBuilder markers = new StringBuilder();
        for (Disaster disaster : disastersData) {
            String sanitizedDescription = disaster.getDescription()
                    .replace("'", "\\'")
                    .replace("\n", "<br>");

            markers.append("var target = L.latLng(")
                    .append(disaster.getLocation().getLatitude()).append(", ")
                    .append(disaster.getLocation().getLongitude()).append(");")
                    .append("L.marker(target).addTo(map).bindPopup('<h2>")
                    .append(disaster.getName())
                    .append("</h2><br>")
                    .append(sanitizedDescription).append("');");
        }

        return """
              <html><body>
              <div id="osm-map" style="width: 100%; height: 100%;">
              </div>
              <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"></script>
              <link href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" rel="stylesheet"/>
              <script>
                // Initialize the map
                var element = document.getElementById('osm-map');
                var map = L.map(element);
                
                // Add OpenStreetMap tile layer
                L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(map);
                
                // Set map's initial view
                var target = L.latLng(0, 0); // Default to (0, 0) if no disasters
                map.setView(target, 4); // Global zoom level
                
                """ + markers + """
              </script>
            </body></html>
            """;
    }
    private String updateMapWithJobs(List<Jobs> jobs){
        StringBuilder markers = new StringBuilder();
        for (Jobs job: jobs){
            String sanitizedHowToApply = job.getHow_to_apply()
                    .replace("'", "\\'")
                    .replace("\n", "")
                    .trim();
            markers.append("var target = L.latLng(")
                    .append(job.getLocation().getLatitude()).append(", ")
                    .append(job.getLocation().getLongitude()).append(");")
                    .append("L.marker(target).addTo(map).bindPopup('<h2>")
                    .append(job.getTitle())
                    .append("</h2><br>")
                    .append(sanitizedHowToApply).append("');");
        }
        return """
              <html><body>
              <div id="osm-map" style="width: 100%; height: 100%;">
              </div>
              <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"></script>
              <link href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" rel="stylesheet"/>
              <script>
                // Initialize the map
                var element = document.getElementById('osm-map');
                var map = L.map(element);
                
                // Add OpenStreetMap tile layer
                L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(map);
                
                // Set map's initial view
                var target = L.latLng(0, 0); // Default to (0, 0) if no disasters
                map.setView(target, 4); // Global zoom level
                
                """ + markers + """
              </script>
            </body></html>
            """;
    }

    private String updateMapWithCoordinates(Disaster disaster){
        String sanitizedDescription = disaster.getDescription()
                .replace("'", "\\'")
                .replace("\n", "<br>");
        Location location = disaster.getLocation();
        String markers = "var target = L.latLng(" +
                location.getLatitude() + ", " +
                location.getLongitude() + ");" +
                "L.marker(target).addTo(map).addTo(map).bindPopup('<h2>"
                + disaster.getName() + "</h2><br>" + sanitizedDescription + "');" +
                "map.setView(target, 6);";

        return """
              <html><body>
              <div id="osm-map"style="width: 100%; height: 100%;"></div>
              <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"></script>
              <link href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" rel="stylesheet"/>
              <script>
                // Initialize the map
                var element = document.getElementById('osm-map');
                var map = L.map(element);
                
                // Add OpenStreetMap tile layer
                L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(map);
                
                // Set map's initial view
                var target = L.latLng(0, 0); // Default to (0, 0) if no disasters
                map.setView(target, 4); // Global zoom level
                
                """ + markers + """
                </script>
                </body></html>
                """;
    }
    private String updateMapWithCoordinates(Jobs job){
        String sanitizedHowToApply = job.getHow_to_apply()
                .replace("'", "\\'")
                .replace("\n", "")
                .trim();
        String marker = "var target = L.latLng(" +
                job.getLocation().getLatitude() + ", " +
                job.getLocation().getLongitude() + ");" +
                "L.marker(target).addTo(map).bindPopup('<h2>"
                + job.getTitle() + "</h2><br>" + sanitizedHowToApply + "');" +
                "map.setView(target, 6);";
        return """
              <html><body>
              <div id="osm-map"style="width: 100%; height: 100%;"></div>
              <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"></script>
              <link href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" rel="stylesheet"/>
              <script>
                // Initialize the map
                var element = document.getElementById('osm-map');
                var map = L.map(element);
                
                // Add OpenStreetMap tile layer
                L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(map);
                
                // Set map's initial view
                var target = L.latLng(0, 0); // Default to (0, 0) if no disasters
                map.setView(target, 4); // Global zoom level
                
                """ + marker + """
                </script>
                </body></html>
                """;

    }


    public void applyFilters(ActionEvent actionEvent) {
        applyFilterBtn.setText("Applying...");
        applyFilterBtn.setDisable(true);
        String selectedDisasterType = disasterTypeFilter.getValue().toString();
        String selectedCountry = countryFilter.getText();
        String selectedStartDate = startDate.getValue() != null ? startDate.getValue().toString() : null;
        String selectedEndDate = endDate.getValue() != null ? endDate.getValue().toString() : null;

        List<Disaster> filteredDisasters = fetchDisastersData(selectedDisasterType, selectedCountry, selectedStartDate, selectedEndDate);
        String mapHtml = updateMapWithDisasters(filteredDisasters);
        webView.getEngine().loadContent(mapHtml);
        disastersObservableList.clear();
        disastersObservableList.addAll(filteredDisasters);
        applyFilterBtn.setText("Apply Filter");
        applyFilterBtn.setDisable(false);
    }

    public void showDisasters(ActionEvent actionEvent) {
        selectedTab = "disaster";
        SwitchTabs(selectedTab);
        loadDisasters();
    }

    public void showJobs(ActionEvent actionEvent) {
        selectedTab = "job";
        SwitchTabs(selectedTab);
        List<Jobs> jobs = fetchJobs();
        String mapHtml = updateMapWithJobs(jobs);
        webView.getEngine().loadContent(mapHtml);
        jobsObservableList.clear();
        jobsObservableList.addAll(jobs);
    }

    public void showAlerts(ActionEvent actionEvent) {
        selectedTab = "alert";
        SwitchTabs(selectedTab);
        List<Disaster> onGoingDisasters = getDisastersByUserCountry();
        String mapHtml = updateMapWithDisasters(onGoingDisasters);
        disastersObservableList.clear();
        disastersObservableList.addAll(onGoingDisasters);
        webView.getEngine().loadContent(mapHtml);
    }

    public void SwitchTabs(String tab) {
        switch (tab){
            case "disaster":
                disasterTab.getStyleClass().setAll("selected");
                alertTab.getStyleClass().setAll("header-button");
                jobTab.getStyleClass().setAll("header-button");
                filteroptions.setVisible(true);
                filteroptions.setManaged(true);
                disasterListView.setVisible(true);
                disasterListView.setManaged(true);
                jobsListView.setVisible(false);
                jobsListView.setManaged(false);
                humanitarianJobsDetails.setVisible(false);
                humanitarianJobsDetails.setManaged(false);
                break;
            case "alert":
                disasterTab.getStyleClass().setAll("header-button");
                alertTab.getStyleClass().setAll("selected");
                jobTab.getStyleClass().setAll("header-button");
                filteroptions.setVisible(false);
                filteroptions.setManaged(false);
                disasterListView.setVisible(true);
                disasterListView.setManaged(true);
                jobsListView.setVisible(false);
                jobsListView.setManaged(false);
                humanitarianJobsDetails.setVisible(false);
                humanitarianJobsDetails.setManaged(false);
                break;
            case "job":
                disasterTab.getStyleClass().setAll("header-button");
                alertTab.getStyleClass().setAll("header-button");
                jobTab.getStyleClass().setAll("selected");
                filteroptions.setVisible(false);
                filteroptions.setManaged(false);
                disasterListView.setVisible(false);
                disasterListView.setManaged(false);
                jobsListView.setVisible(true);
                jobsListView.setManaged(true);
                humanitarianJobsDetails.setVisible(true);
                humanitarianJobsDetails.setManaged(true);
                break;
        }
    }

    public void showUserAccount(ActionEvent actionEvent) {
        try {
            MainApplication.showUserProfilePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGuides(ActionEvent actionEvent) throws IOException {
        MainApplication.showGuidePage();
    }
}
