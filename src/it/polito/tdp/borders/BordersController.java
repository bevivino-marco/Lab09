/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

	Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader
    @FXML
    private ComboBox<Country> boxCountries;
    private boolean grafoCreato=false;

	@FXML
	
	void doCalcolaConfini(ActionEvent event) {
       /*  txtResult.clear();
         try {
        	 m.creaGrafo(Integer.parseInt(txtAnno.getText().trim()));
        	 txtResult.appendText(m.getCountryCounts()+"\n"+m.getNumberOfConnectedComponents());
        	 
        			 
         }catch(NumberFormatException e) {
        	 txtResult.setText("inserire un anno corretto!!");
         }*/
		txtResult.clear();

		int anno;

		try {
			anno = Integer.parseInt(txtAnno.getText());
			if ((anno < 1816) || (anno > 2016)) {
				txtResult.setText("Inserire un anno nell'intervallo 1816 - 2016");
				return;
			}
			
		} catch (NumberFormatException e) {
			txtResult.setText("Inserire un anno nell'intervallo 1816 - 2016");
			return;
		}

		try {
			model.creaGrafo(anno);
			grafoCreato=true;
			List<Country> countries = model.getListaPaesi();
			//cmbNazione.getItems().addAll(countries);
			
			txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents()));

			Map<Country, Integer> stats = model.getCountryCounts();
			for (Country country : stats.keySet())
				txtResult.appendText(String.format("%s %d\n", country, stats.get(country)));

		} catch (RuntimeException e) {
			txtResult.setText("Errore: " + e.getMessage() + "\n");
			return;
		}

	    
         
		
	}
	@FXML
	void doTrovaVicini(ActionEvent event) {
		if (boxCountries.getValue()==null ) {
			txtResult.appendText("selezionare uno stato");
		}else if(grafoCreato==false) {
			txtResult.appendText("selezionare un anno corretto e calcolare i confini prima...");
	    }else {
			txtResult.appendText(model.getViciniRicorsione(boxCountries.getValue()).toString());
		}
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
		assert boxCountries != null : "fx:id=\"boxCountries\" was not injected: check your FXML file 'Borders.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
	    
	}

	public void setModel(Model m2) {
		this.model=m2;
		boxCountries.getItems().addAll(model.getListaPaesi());
	}
}
