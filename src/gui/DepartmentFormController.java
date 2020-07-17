package gui;

import java.awt.geom.IllegalPathStateException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	// Estabelecendo uma dependência
	private Department entity;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	// instância do Department
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@FXML 
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalPathStateException("A entidade está vazia.");
		}
		if (service == null) {
			throw new IllegalPathStateException("O serviço está vazio.");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} 
		catch (Exception e) {
			Alerts.showAlert("Erro ao salvar objet", null, e.getMessage(), AlertType.ERROR);
		} 
		
	}
	
	private void notifyDataChangeListeners() {
		
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
		
	}

	// Pega os valores informados no formulário e coloca no objeto
	private Department getFormData() {
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML 
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if (entity ==null) {
			throw new IllegalStateException("A entidade está nula.");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}


