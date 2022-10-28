package application.finalproject_ist311;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.sql.*;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    TableView<EmployeeRecords> table;

    @Override
    public void start(Stage primaryStage) {
        try {

            primaryStage.setTitle("XYZ Company");

            //Title label:
            Label label = new Label("Employee Records");
            label.setFont(new Font("Arial", 26));
            label.setPadding(new Insets(10,10,10,10));


            //Home Menu and Items
            Menu fileMenu = new Menu("_Add"); //File menu
            MenuItem newEntry = new MenuItem("New"); //'New' button within 'File' menu
            fileMenu.getItems().add(newEntry);


            Menu editMenu = new Menu("_Edit"); //Edit menu
            MenuItem updateEntry = new MenuItem("Update"); //'Update' button within 'Edit' menu
            MenuItem deleteEntry = new MenuItem("Delete"); //'Delete' button within 'Edit' menu
            editMenu.getItems().addAll(updateEntry, deleteEntry);


            Menu filterMenu = new Menu("Filter"); //Filter menu
            MenuItem searchEntry = new MenuItem("Search");
            filterMenu.getItems().add(searchEntry);


            //Overall Menu Bar:
            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().addAll(fileMenu, editMenu, filterMenu);


            //Table
            table = new TableView<>();

            TableColumn<EmployeeRecords, String> idColumn = new TableColumn<>("Employee ID");
            idColumn.setMinWidth(75);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

            TableColumn<EmployeeRecords, String> firstNameColumn = new TableColumn<>("First Name");
            firstNameColumn.setMinWidth(75);
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFirstName"));

            TableColumn<EmployeeRecords, String> lastNameColumn = new TableColumn<>("Last Name");
            lastNameColumn.setMinWidth(75);
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeLastName"));

            TableColumn<EmployeeRecords, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setMinWidth(75);
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));

            TableColumn<EmployeeRecords, Double> salaryColumn = new TableColumn<>("Salary");
            salaryColumn.setMinWidth(75);
            salaryColumn.setCellValueFactory(new PropertyValueFactory<>("employeeSalary"));

            TableColumn<EmployeeRecords, String> departmentColumn = new TableColumn<>("Department");
            departmentColumn.setMinWidth(75);
            departmentColumn.setCellValueFactory(new PropertyValueFactory<>("employeeDepartment"));

            //Populate table
            table.setItems(getData());
            table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, salaryColumn, departmentColumn);
            table.setPadding(new Insets(10,10,10,10));

            //VBox (Overall layout)
            VBox homeLayout = new VBox();

            homeLayout.getChildren().addAll(menuBar, label, table);

            //Display Home Window
            Scene homeScene = new Scene(homeLayout, 600, 600);
            primaryStage.setScene(homeScene);
            primaryStage.show(); //Only need to call show() once, just user set scene for further scene changes


            //Button functionality

            //File
            newEntry.setOnAction(e -> { //'New' button in 'File' menu
                try {
                    addData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            //Edit
            updateEntry.setOnAction(e -> {
                EmployeeRecords updatingEmployee = new EmployeeRecords();

                try {
                    updatingEmployee = table.getSelectionModel().getSelectedItem();
                    updateData(updatingEmployee);
                } catch (SQLException exc) {
                    exc.printStackTrace();
                } catch (NullPointerException exc) { //thrown when no entry is selected
                    //error popup
                    Stage errorPopupStage = new Stage();

                    //Text and 'ok' button
                    Text warningText = new Text("No Entry Selected");
                    warningText.setFont(new Font("Arial", 18));

                    VBox errorText = new VBox();
                    errorText.getChildren().addAll(warningText);
                    errorText.setPadding(new Insets(20,20,20,20));

                    Button okButton = new Button("OK");
                    okButton.setMinWidth(70);

                    HBox okButtonHBox = new HBox();
                    okButtonHBox.getChildren().add(okButton);
                    okButtonHBox.setPadding(new Insets(10,20,20,200));

                    BorderPane errorPopupBorderPane = new BorderPane();
                    errorPopupBorderPane.setCenter(warningText);
                    errorPopupBorderPane.setBottom(okButtonHBox);

                    Scene errorPopupScene = new Scene(errorPopupBorderPane, 300, 300);
                    errorPopupStage.setScene(errorPopupScene);
                    errorPopupStage.show();

                    okButton.setOnAction(ok -> {
                        errorPopupStage.close();
                    });
                }
            });

            deleteEntry.setOnAction(e -> {
                EmployeeRecords deletingEmployee = new EmployeeRecords();

                try {
                    deletingEmployee = table.getSelectionModel().getSelectedItem();
                    deleteData(deletingEmployee);
                } catch (SQLException exc) {
                    exc.printStackTrace();
                } catch (NullPointerException exc) {
                    //error popup
                    Stage errorPopupStage = new Stage();

                    //Text and 'ok' button
                    Text warningText = new Text("No Entry Selected");
                    warningText.setFont(new Font("Arial", 18));

                    VBox errorText = new VBox();
                    errorText.getChildren().addAll(warningText);
                    errorText.setPadding(new Insets(20,20,20,20));

                    Button okButton = new Button("OK");
                    okButton.setMinWidth(70);

                    HBox okButtonHBox = new HBox();
                    okButtonHBox.getChildren().add(okButton);
                    okButtonHBox.setPadding(new Insets(10,20,20,200));

                    BorderPane errorPopupBorderPane = new BorderPane();
                    errorPopupBorderPane.setCenter(warningText);
                    errorPopupBorderPane.setBottom(okButtonHBox);

                    Scene errorPopupScene = new Scene(errorPopupBorderPane, 300, 300);
                    errorPopupStage.setScene(errorPopupScene);
                    errorPopupStage.show();

                    okButton.setOnAction(ok -> {
                        errorPopupStage.close();
                    });
                }
            });

            searchEntry.setOnAction(e -> {
                try {
                    searchData();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            });


        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }



    //-----------------------------------------------CONTROLLER-----------------------------------------------------

    public ObservableList<EmployeeRecords> getData() throws SQLException { //This will obviously need to be changed to get data from mySQL

        ObservableList<EmployeeRecords> data_list = FXCollections.observableArrayList();

        //If table is populated, delete entries
        if (!table.getItems().isEmpty()) {
            table.getItems().removeAll();
        }

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            myConn = getConnection(); //get connection to DB

            myStmt = myConn.createStatement(); //Create statement

            myRs = myStmt.executeQuery("select * from employees"); //use ResultSet to store query results

            //Iterate through myRs and create an EmployeeRecords object for each index
            while (myRs.next()) {
                int ID = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                double salary = myRs.getDouble("salary");
                String department = myRs.getString("department");

                data_list.add(new EmployeeRecords(ID, firstName, lastName, email, salary, department));
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            close(myConn, myStmt, myRs);
        }

        return data_list;
    }


    public Connection getConnection() throws SQLException {
        Connection myConn;

        String dbUrl = "jdbc:mysql://localhost:3306/finalProjectDB";
        String user = "student";
        String pass = "student";

        myConn = DriverManager.getConnection(dbUrl, user, pass);

        return myConn;
    }

    public void close(Connection myConn, Statement myStmt, ResultSet myRs) throws SQLException {
        if (myConn != null) {
            myConn.close();
        }
        if (myStmt != null) {
            myStmt.close();
        }
        if (myRs != null) {
            myRs.close();
        }
    }


    public void addData() throws SQLException {

        //-------------------------------Add Scene---------------------------------
        //Text
        Label addText = new Label("Enter a New Employee Entry:");
        addText.setPadding(new Insets(20,20,20,50));
        addText.setFont(new Font("Arial", 18));

        //Text boxes for input

        Label addFirstNameText = new Label("First Name: ");
        TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setPrefWidth(150);

        HBox addFirstNameHBox = new HBox(20);
        addFirstNameHBox.getChildren().addAll(addFirstNameText, addFirstName);
        addFirstNameHBox.setPadding(new Insets(10,10,10,50));


        Label addLastNameText = new Label("Last Name: ");
        TextField addLastName = new TextField();
        addLastName.setPromptText("Last Name");
        addLastName.setPrefWidth(150);

        HBox addLastNameHBox = new HBox(20);
        addLastNameHBox.getChildren().addAll(addLastNameText, addLastName);
        addLastNameHBox.setPadding(new Insets(10,10,10,50));


        Label addDepartmentLabel = new Label("Department: ");
        ChoiceBox<String> addDepartment = new ChoiceBox<>();
        addDepartment.getItems().addAll("IT", "Accounting", "Marketing");

        HBox addDepartmentHBox = new HBox(20);
        addDepartmentHBox.getChildren().addAll(addDepartmentLabel, addDepartment);
        addDepartmentHBox.setPadding(new Insets(10,10,10,50));


        Label addSalaryLabel = new Label("Salary: ");
        addSalaryLabel.setPadding(new Insets(0, 15, 0, 0));
        TextField addSalary = new TextField();
        addSalary.setPromptText("Salary");

        HBox addSalaryHBox = new HBox(20);
        addSalaryHBox.getChildren().addAll(addSalaryLabel, addSalary);
        addSalaryHBox.setPadding(new Insets(10, 10, 10, 50));

        //Add button
        Button addButton = new Button("Add");
        addButton.setMinWidth(60);

        //cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(60);

        HBox addButtonsHBox = new HBox(10);
        addButtonsHBox.setPadding(new Insets(20,20,20,250));
        addButtonsHBox.getChildren().addAll(cancelButton, addButton);

        //Layouts for text boxes and buttons
        VBox addVBox = new VBox();
        addVBox.getChildren().addAll(addFirstNameHBox, addLastNameHBox, addDepartmentHBox, addSalaryHBox);

        BorderPane addLayout = new BorderPane();
        addLayout.setTop(addText);
        addLayout.setBottom(addButtonsHBox);
        addLayout.setCenter(addVBox);

        Scene addScene = new Scene(addLayout, 400,400);

        //Show the Add window, create add stage
        Stage addStage = new Stage();
        addStage.setScene(addScene);
        addStage.show();

        //Add Button logic
        addButton.setOnAction(e -> {
            Connection myConn = null;
            PreparedStatement myStmt = null;

            try {
                myConn = getConnection();

                //Input validation (first name and last name should not contain an integer
                String newFirstName = addFirstName.getText();
                newFirstName = newFirstName.replaceAll("\\s", ""); //Removes potential whitespace
                String newLastName = addLastName.getText();
                newLastName = newLastName.replaceAll("\\s", ""); //whitespace

                boolean valid = true;

                char[] firstNameArray = newFirstName.toCharArray(); //Convert string to array of chars to iterate thru to find any int/doubles
                for (int i = 0; i < firstNameArray.length; i++) {
                    if (Character.isDigit(firstNameArray[i])) {
                        System.out.println("Invalid entry");
                        valid = false;
                        break;
                    }
                }

                char[] lastNameArray = newLastName.toCharArray(); //Convert string to array of chars to iterate thru to find any int/doubles
                for (int i = 0; i < lastNameArray.length; i++) {
                    if (Character.isDigit(lastNameArray[i])) {
                        System.out.println("Invalid entry");
                        valid = false;

                        break;
                    }
                }

                //Make sure name fields are not empty
                if (firstNameArray.length == 0) {
                    valid = false;
                }
                if (lastNameArray.length == 0) {
                    valid = false;
                }

                //Validate department
                String newDepartment = "";
                try {
                    if (!addDepartment.getValue().isBlank()) {
                        newDepartment = addDepartment.getValue();
                    }
                } catch(NullPointerException depExc) {
                    valid = false;
                }

                //Validate salary
                double newSalary = 0;
                try {
                    newSalary = Double.parseDouble(addSalary.getText());
                } catch (Exception numExc) {
                    valid = false;
                }

                //Auto-generate email
                String emailFirst = newFirstName.toLowerCase(); //Converts first and last name to lowercase
                String emailLast = newLastName.toLowerCase();
                String newEmail = emailFirst + "." + emailLast + "@xyz.com";

                //If valid, create new entry
                if (valid) {
                    myStmt = myConn.prepareStatement("insert into employees (id, first_name, last_name, email, department, salary) values (null, ?, ?, ?, ?, ?)");
                    myStmt.setString(1, newFirstName);
                    myStmt.setString(2, newLastName);
                    myStmt.setString(3, newEmail);
                    myStmt.setString(4, newDepartment);
                    myStmt.setDouble(5, newSalary);

                    myStmt.executeUpdate();

                    table.setItems(getData());

                    addStage.close();
                }

                if (!valid) {
                    //error popup
                    Stage errorPopupStage = new Stage();

                    //Text and 'ok' button
                    Text warningText = new Text("Invalid Entry");
                    warningText.setFont(new Font("Arial", 18));

                    VBox errorText = new VBox();
                    errorText.getChildren().addAll(warningText);
                    errorText.setPadding(new Insets(20,20,20,20));

                    Button okButton = new Button("OK");
                    okButton.setMinWidth(70);

                    HBox okButtonHBox = new HBox();
                    okButtonHBox.getChildren().add(okButton);
                    okButtonHBox.setPadding(new Insets(10,20,20,200));

                    BorderPane errorPopupBorderPane = new BorderPane();
                    errorPopupBorderPane.setCenter(warningText);
                    errorPopupBorderPane.setBottom(okButtonHBox);

                    Scene errorPopupScene = new Scene(errorPopupBorderPane, 300, 300);
                    errorPopupStage.setScene(errorPopupScene);
                    errorPopupStage.show();

                    okButton.setOnAction(ok -> {
                        errorPopupStage.close();
                        try {
                            addStage.close();
                            addData();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    });
                }

            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                try {
                    close(myConn, myStmt, null);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });

        //Cancel Button logic
        cancelButton.setOnAction(e -> addStage.close());

    }

    public void updateData(EmployeeRecords updatingEmployee) throws SQLException {
        //-----------------------------Update data window-------------------------------------

        //Text boxes that autofill the current data, include an update and cancel button

        //text, text fields, and buttons
        Label updateText = new Label("Update this employee: '" + updatingEmployee.getEmployeeFirstName() + " " + updatingEmployee.getEmployeeLastName() + "'");
        updateText.setPadding(new Insets(20,20,20,50));
        updateText.setFont(new Font("Arial", 18));

        Text firstNameText = new Text("First Name: "); //First Name text
        firstNameText.setWrappingWidth(80);
        TextField updateFirstName = new TextField(); //First Name Text Field
        updateFirstName.setText(updatingEmployee.getEmployeeFirstName());
        updateFirstName.setPrefWidth(150);

        Text lastNameText = new Text("Last Name: ");
        lastNameText.setWrappingWidth(80);
        TextField updateLastName = new TextField(); //Last Name Text Field
        updateLastName.setText(updatingEmployee.getEmployeeLastName());
        updateLastName.setPrefWidth(150);

        //Email first half text field, '@xyz.com' right after as just text
        Text emailText = new Text("Email: ");
        emailText.setWrappingWidth(80);
        TextField updateEmail = new TextField(); //Email text field
        String emailPrompt = updatingEmployee.getEmployeeEmail().replaceAll("@xyz.com", ""); //only prompt with the first half of email address, since @xyz.com should not be changed
        updateEmail.setText(emailPrompt);
        updateEmail.setPrefWidth(90);

        Text emailEnd = new Text("@xyz.com");

        //Dropdown box for department
        Text departmentText = new Text("Department: ");
        departmentText.setWrappingWidth(80);
        ChoiceBox<String> updateDepartment = new ChoiceBox<>();
        updateDepartment.getItems().addAll("IT", "Accounting", "Marketing");
        updateDepartment.getSelectionModel().select(updatingEmployee.getEmployeeDepartment());


        //TextField for salary
        Text salaryText = new Text("Salary");
        salaryText.setWrappingWidth(80);
        TextField updateSalary = new TextField();
        updateSalary.setText(String.valueOf(updatingEmployee.getEmployeeSalary()));
        updateSalary.setPrefWidth(150);

        //Buttons
        Button updateButton = new Button("Update");

        Button cancelButton = new Button("Cancel");

        //Layout
        HBox updateFirstNameHBox = new HBox();
        updateFirstNameHBox.getChildren().addAll(firstNameText, updateFirstName);

        HBox updateLastNameHBox = new HBox();
        updateLastNameHBox.getChildren().addAll(lastNameText, updateLastName);

        HBox updateEmailHBox = new HBox();
        updateEmailHBox.getChildren().addAll(emailText, updateEmail, emailEnd);

        HBox updateDepartmentHBox = new HBox();
        updateDepartmentHBox.getChildren().addAll(departmentText, updateDepartment);

        HBox updateSalaryHBox = new HBox();
        updateSalaryHBox.getChildren().addAll(salaryText, updateSalary);


        HBox updateButtonHBox = new HBox(10);
        updateButtonHBox.setPadding(new Insets(20,20,20,250));
        updateButtonHBox.getChildren().addAll(cancelButton, updateButton);

        VBox updateVBox = new VBox(10);
        updateVBox.setPadding(new Insets(50,20,20,50));
        updateVBox.getChildren().addAll(updateFirstNameHBox, updateLastNameHBox, updateEmailHBox, updateDepartmentHBox, updateSalaryHBox);

        BorderPane updateBorderPane = new BorderPane();
        updateBorderPane.setTop(updateText);
        updateBorderPane.setCenter(updateVBox);
        updateBorderPane.setBottom(updateButtonHBox);

        //Stage and scene
        Stage updatePopUpStage = new Stage();
        Scene updatePopUpScene = new Scene(updateBorderPane, 400, 400);

        updatePopUpStage.setScene(updatePopUpScene);
        updatePopUpStage.show();

        //Button functionality
        updateButton.setOnAction(e -> {
            Connection myConn = null;
            PreparedStatement myStmt = null;

            try {
                myConn = getConnection();

                String newFirstName = updateFirstName.getText();
                newFirstName = newFirstName.replaceAll("\\s", ""); //Removes whitespace

                String newLastName = updateLastName.getText();
                newLastName = newLastName.replaceAll("\\s", ""); //whitespace

                String newEmail = updateEmail.getText();
                newEmail = newEmail.replaceAll("\\s", "");

                //Validate
                boolean valid = true;

                //create char arrays for new entries
                char[] firstNameArray = newFirstName.toCharArray();
                //iterate through to find digit, set valid=false if found
                for (int i = 0; i < firstNameArray.length; i++) {
                    if (Character.isDigit(firstNameArray[i])) {
                        System.out.println("Invalid entry");
                        valid = false;
                        break;
                    }
                }

                char[] lastNameArray = newLastName.toCharArray();
                //iterate through to find digit, set valid=false if found
                for (int i = 0; i < lastNameArray.length; i++) {
                    if (Character.isDigit(lastNameArray[i])) {
                        System.out.println("Invalid entry");
                        valid = false;
                        break;
                    }
                }

                double newSalary = updatingEmployee.getEmployeeSalary();
                try {
                    newSalary = Double.parseDouble(updateSalary.getText());
                } catch (Exception numExc) {
                    valid = false;
                }

                if (valid) {
                    myStmt = myConn.prepareStatement("update employees set first_name=?, last_name=?, email=?, department=?, salary=? where id=?");
                    myStmt.setString(1, newFirstName);
                    myStmt.setString(2, newLastName);
                    myStmt.setString(3, newEmail + "@xyz.com");
                    myStmt.setString(4, updateDepartment.getValue());
                    myStmt.setDouble(5, newSalary);

                    myStmt.setInt(6, updatingEmployee.getEmployeeID());

                    myStmt.executeUpdate();

                    table.setItems(getData());

                    updatePopUpStage.close();
                }

                if (!valid) {
                    //error popup
                    Stage errorPopupStage = new Stage();

                    //Text and 'ok' button
                    Text warningText = new Text("Invalid Entry");
                    warningText.setFont(new Font("Arial", 18));

                    VBox errorText = new VBox();
                    errorText.getChildren().addAll(warningText);
                    errorText.setPadding(new Insets(20,20,20,20));

                    Button okButton = new Button("OK");
                    okButton.setMinWidth(70);

                    HBox okButtonHBox = new HBox();
                    okButtonHBox.getChildren().add(okButton);
                    okButtonHBox.setPadding(new Insets(10,20,20,200));

                    BorderPane errorPopupBorderPane = new BorderPane();
                    errorPopupBorderPane.setCenter(warningText);
                    errorPopupBorderPane.setBottom(okButtonHBox);

                    Scene errorPopupScene = new Scene(errorPopupBorderPane, 300, 300);
                    errorPopupStage.setScene(errorPopupScene);
                    errorPopupStage.show();

                    okButton.setOnAction(ok -> {
                        errorPopupStage.close();
                        try {
                            updatePopUpStage.close();
                            updateData(updatingEmployee);
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    });
                }

            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                try {
                    close(myConn, myStmt, null);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }

        });

        cancelButton.setOnAction(e -> updatePopUpStage.close());

    }

    public void deleteData(EmployeeRecords deletingEmployee) throws SQLException {
        //---------------------------Delete Confirmation Pop-Up Window----------------------

        //Text and buttons
        Label deleteConfirmationText1 = new Label("Are you sure you want to delete the selected");
        deleteConfirmationText1.setPadding(new Insets(30,20,0,50));
        deleteConfirmationText1.setFont(new Font("Arial", 14));

        Label deleteConfirmationText2 = new Label("employee?");
        deleteConfirmationText2.setPadding(new Insets(10,20,80,50));
        deleteConfirmationText2.setFont(new Font("Arial", 14));

        Label deletingEmployeeLabel = new Label("'" + deletingEmployee.getEmployeeFirstName() + " " + deletingEmployee.getEmployeeLastName() + "'");
        deletingEmployeeLabel.setFont(new Font("Arial", 18));
        deletingEmployeeLabel.setMaxWidth(Double.MAX_VALUE);
        deletingEmployeeLabel.setAlignment(Pos.CENTER); //Aligns the label to the exact center of the window width

        Button cancelButton = new Button("Cancel");

        Button confirmButton = new Button("Delete");

        //HBox for buttons
        HBox buttonHBox = new HBox(10);
        buttonHBox.setPadding(new Insets(20,20,20,250));
        buttonHBox.getChildren().addAll(cancelButton, confirmButton);

        //VBox layout
        VBox deleteVBox = new VBox();
        deleteVBox.getChildren().addAll(deleteConfirmationText1, deleteConfirmationText2, deletingEmployeeLabel);

        //BorderPane
        BorderPane deleteLayout = new BorderPane();
        deleteLayout.setCenter(deleteVBox);
        deleteLayout.setBottom(buttonHBox);

        //Stage and scene
        Stage deletePopUpStage = new Stage();
        Scene deletePopUpScene = new Scene(deleteLayout, 400, 400);

        deletePopUpStage.setScene(deletePopUpScene);
        deletePopUpStage.show();

        //Button functionality
        confirmButton.setOnAction(e -> {
            Connection myConn = null;
            PreparedStatement myStmt = null;

            try {
                myConn = getConnection();

                myStmt = myConn.prepareStatement("delete from employees where id=?");
                myStmt.setInt(1, deletingEmployee.getEmployeeID());

                myStmt.executeUpdate();

                table.setItems(getData());

            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                try {
                    close(myConn, myStmt, null);
                } catch (Exception exc) {
                    exc.printStackTrace();
                } finally {
                    deletePopUpStage.close();
                }
            }
        });

        cancelButton.setOnAction(e -> deletePopUpStage.close());


    }

    public void searchData() {

        Stage searchStage = new Stage();
        searchStage.setTitle("Search XYZ Employees");

        Label searchLabel = new Label("Search XYZ Employee Records:");
        searchLabel.setFont(new Font("Arial", 18));
        searchLabel.setPadding(new Insets(20,20,20,20));

        Label searchNameLabel = new Label("Name: ");
        TextField searchName = new TextField();
        searchName.setPromptText("Name");

        HBox searchNameHBox = new HBox(20);
        searchNameHBox.getChildren().addAll(searchNameLabel, searchName);
        searchNameHBox.setPadding(new Insets(20,20,20,20));


        Label salaryLabel = new Label("Salary: ");

        TextField minSalary = new TextField();
        minSalary.setPromptText("Min Salary");

        Label dashLabel = new Label("-");

        TextField maxSalary = new TextField();
        maxSalary.setPromptText("Max Salary");

        HBox salaryHBox = new HBox(20);
        salaryHBox.getChildren().addAll(salaryLabel, minSalary, dashLabel, maxSalary);
        salaryHBox.setPadding(new Insets(20,20,20,20));


        //Check boxes? Multi-choice drop-down for department?
        CheckBox checkITDepartment = new CheckBox("IT");
        checkITDepartment.setSelected(true);

        CheckBox checkAccountingDepartment = new CheckBox("Accounting");
        checkAccountingDepartment.setSelected(true);

        CheckBox checkMarketingDepartment = new CheckBox("Marketing");
        checkMarketingDepartment.setSelected(true);

        HBox departmentCheckHBox = new HBox(20);
        departmentCheckHBox.getChildren().addAll(checkITDepartment, checkAccountingDepartment, checkMarketingDepartment);
        departmentCheckHBox.setPadding(new Insets(20,20,20,20));

        //Buttons
        Button searchButton = new Button("Search");

        Button cancelButton = new Button("Cancel");

        Button resetButton = new Button("Reset Search");
        HBox clearButtonHBox = new HBox();
        clearButtonHBox.getChildren().add(resetButton);
        clearButtonHBox.setPadding(new Insets(20,20,20,20));
        clearButtonHBox.setMaxWidth(Double.MAX_VALUE);
        clearButtonHBox.setAlignment(Pos.CENTER);

        HBox buttonHBox = new HBox(10);
        buttonHBox.setPadding(new Insets(20, 20, 20, 400));
        buttonHBox.getChildren().addAll(cancelButton, searchButton);

        //Overall layout
        VBox searchBoxLayout = new VBox();
        searchBoxLayout.getChildren().addAll(searchNameHBox, salaryHBox, departmentCheckHBox, clearButtonHBox);

        BorderPane searchLayout = new BorderPane();
        searchLayout.setTop(searchLabel);
        searchLayout.setCenter(searchBoxLayout);
        searchLayout.setBottom(buttonHBox);

        Scene searchScene = new Scene(searchLayout, 600, 600);
        searchStage.setScene(searchScene);
        searchStage.show();

        //Button functionality
        cancelButton.setOnAction(e -> searchStage.close());

        resetButton.setOnAction(e -> {
            searchName.clear();
            minSalary.clear();
            maxSalary.clear();
            checkITDepartment.setSelected(true);
            checkAccountingDepartment.setSelected(true);
            checkMarketingDepartment.setSelected(true);
        });

        searchButton.setOnAction(e -> {
            Connection myConn = null;
            PreparedStatement myStmt = null;
            ResultSet myRs = null;

            ObservableList<EmployeeRecords> search_data_list = FXCollections.observableArrayList();

            TableView<EmployeeRecords> searchTable = new TableView<>();

            TableColumn<EmployeeRecords, String> searchIdColumn = new TableColumn<>("Employee ID");
            searchIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

            TableColumn<EmployeeRecords, String> searchFirstNameColumn = new TableColumn<>("First Name");
            searchFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFirstName"));

            TableColumn<EmployeeRecords, String> searchLastNameColumn = new TableColumn<>("Last Name");
            searchLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeLastName"));

            TableColumn<EmployeeRecords, String> searchEmailColumn = new TableColumn("Email");
            searchEmailColumn.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));

            TableColumn<EmployeeRecords, Double> searchSalaryColumn = new TableColumn<>("Salary");
            searchSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("employeeSalary"));

            TableColumn<EmployeeRecords, String> searchDepartmentColumn = new TableColumn<>("Department");
            searchDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("employeeDepartment"));

            searchTable.getColumns().addAll(searchIdColumn, searchFirstNameColumn, searchLastNameColumn, searchEmailColumn, searchSalaryColumn, searchDepartmentColumn);


            try {
                myConn = getConnection();

                myStmt = myConn.prepareStatement("select * from finalProjectDB.employees where (first_name like ? or last_name like ?) and (department like ? or department like ? or department like ?) and (salary >= ? and salary <= ?);");

                //First and last name will have to be split up
                //Department can be either selected or null, will not require a separate sql statement for each
                //if-else to check if salary is null? then replace with 0 or very high number


                //Set name

                try { //If searching by two names

                    String[] firstAndLastName = searchName.getText().split(" ", 2); //Creates array containing an element for both first and last name, separated by the space

                    String firstNameSearch = firstAndLastName[0];
                    firstNameSearch = firstNameSearch.replaceAll(" ", ""); //remove any potential spaces

                    String lastNameSearch = firstAndLastName[1];
                    lastNameSearch = lastNameSearch.replaceAll(" ", "");

                    myStmt.setString(1, firstNameSearch);
                    myStmt.setString(2, lastNameSearch);

                } catch (Exception nameExc) { //if searching by only one

                    String name = searchName.getText();
                    name = name.replaceAll(" ", "");

                    myStmt.setString(1, "%" + name + "%");
                    myStmt.setString(2, "%" + name + "%");

                }

                //Set departments
                myStmt.setString(3, null); //Base cases
                myStmt.setString(4, null);
                myStmt.setString(5, null);

                if (checkITDepartment.isSelected()) {
                    myStmt.setString(3, "IT");
                }
                if (checkAccountingDepartment.isSelected()) {
                    myStmt.setString(4, "Accounting");
                }
                if (checkMarketingDepartment.isSelected()) {
                    myStmt.setString(5, "Marketing");
                }

                //Set salary
                myStmt.setDouble(6, 0.0); //Base cases
                myStmt.setDouble(7, 9999999999.0);

                //If searching for special salary range:
                if (!minSalary.getText().isBlank()) {
                    try {
                        String newMinSalString = minSalary.getText();
                        double newMinSal = Double.parseDouble(newMinSalString);

                        myStmt.setDouble(6, newMinSal);
                    } catch (Exception minSalExc) {
                        System.out.println("invalid min sal search");
                    }
                }
                if (!maxSalary.getText().isBlank()) {
                    try {
                        String newMaxSalString = maxSalary.getText();
                        double newMaxSal = Double.parseDouble(newMaxSalString);

                        myStmt.setDouble(7, newMaxSal);
                    } catch (Exception maxSalExc) {
                        System.out.println("invalid max sal search");
                    }
                }


                //Read Rs
                myRs = myStmt.executeQuery();

                while (myRs.next()) {
                    int ID = myRs.getInt("id");
                    String firstName = myRs.getString("first_name");
                    String lastName = myRs.getString("last_name");
                    String email = myRs.getString("email");
                    double salary = myRs.getDouble("salary");
                    String department = myRs.getString("department");

                    search_data_list.add(new EmployeeRecords(ID, firstName, lastName, email, salary, department));
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {

                //Show result table
                searchTable.setItems(search_data_list);

                //Make new scene to display table
                BorderPane resultPane = new BorderPane();

                Label resultLabel = new Label("Search results: ");
                resultLabel.setFont(new Font("Arial", 18));
                resultLabel.setPadding(new Insets(20,20,20,20));

                Button backButton = new Button("Back to Search");

                HBox backButtonHBox = new HBox();
                backButtonHBox.getChildren().add(backButton);
                backButtonHBox.setPadding(new Insets(20,20,20,475));

                resultPane.setTop(resultLabel);
                resultPane.setCenter(searchTable);
                resultPane.setBottom(backButtonHBox);

                Scene searchResultScene = new Scene(resultPane, 600, 600);
                searchStage.setScene(searchResultScene);

                backButton.setOnAction(b -> searchStage.setScene(searchScene));
            }

            //Close
            try {
                close(myConn, myStmt, myRs);
            } catch(Exception exc) {
                exc.printStackTrace();
            }

        });
    }

}
