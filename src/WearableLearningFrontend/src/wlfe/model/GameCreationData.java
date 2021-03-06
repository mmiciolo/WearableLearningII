package wlfe.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.context.RequestContext;

import wlfe.common.DataTableColumn;

public class GameCreationData {

	private String title;
	private String teamCount;
	private String playersPerTeam;
	private int id;
	private int teamId;
	private int playerId;
	private String text;
	private String ledColor;
	private String ledDuration;
	private String buzzerOn;
	private String buzzerDuration;
	private String responseTo;
	private String responseType;
	private List<DataTableColumn> dataTable = new ArrayList<DataTableColumn>();
	private int columnCount;
	private List<String> colorValues = new ArrayList<String>();
	private String[] buttonColors = {"red", "green", "blue", "black"};

	public GameCreationData() {
		
	}
	
	public GameCreationData(int id) {
		this.id = id;
	}
	
	public GameCreationData(int id, int teamId, int playerId, String text, String ledColor, String ledDuration, String buzzerOn, String buzzerDuration, String responseType) {
		this.id = id - 1;
		this.teamId = teamId;
		this.playerId = playerId;
		this.text = text;
		this.ledColor = ledColor;
		this.ledDuration = ledDuration;
		this.buzzerOn = buzzerOn;
		this.buzzerDuration = buzzerDuration;
		this.responseType = responseType;
		if(teamId == 0 && playerId == 0) { responseTo = "Game Wide"; }
		else if(teamId > 0 ) { responseTo = "Team " + teamId; }
		else if(playerId > 0) { responseTo = "--Player " + playerId; }
		else {}
	}
	
	public void updateGeneralSetup(String title, String teamCount, String playersPerTeam) {
		this.title = title;
		this.teamCount = teamCount;
		this.playersPerTeam = playersPerTeam;
	}
	
	public List<String> fillDropDown(String query) {
		List<String> list = new ArrayList<String>();
		list.add("Game Wide");
		int teamC = Integer.parseInt(teamCount);
		int playersPerTeamC = Integer.parseInt(playersPerTeam);
		List<String> tempCollection = new ArrayList<String>();
		tempCollection.add("Game Wide");
		for(int i = 1; i < teamC + 1; i++) {
			tempCollection.add("Team " + i);
			for(int n = 1; n < playersPerTeamC + 1; n++) {
				tempCollection.add("--Player " + n);
			}
		}
		return tempCollection;
	}
	
	public void testLoad(ArrayList<String> items) {
		for(DataTableColumn column : dataTable) {
			if(column.getHeader().equals("SelectOne")) {
				column.setProperty(items.get(0));
				items.remove(0);
			}
		}
	}
	
	public void responseToChanged() {
		if(responseType != null) {
			dataTable.clear();
			colorValues.clear();
			if(responseType.equals("Single")) {
				if(responseTo.equals("Game Wide")) {
					columnCount = 5;
					dataTable.add(new DataTableColumn("Text", "Button Color"));
					for(int i = 0; i < 4; i++) {
						dataTable.add(new DataTableColumn("Color", buttonColors[i]));
					}
					for(int i = 0; i < Integer.parseInt(teamCount); i++) {
						for(int n = 0; n < columnCount; n++) {
							if(n == 0) {
								dataTable.add(new DataTableColumn("Text", "Team " + (i + 1)));
							} else {
								dataTable.add(new DataTableColumn("SelectOne", ""));
							}
						}
					}
				} else if(responseTo.contains("Team")) { 
					columnCount = 5;
					dataTable.add(new DataTableColumn("Text", "Button Color"));
					for(int i = 0; i < 4; i++) {
						dataTable.add(new DataTableColumn("Color", buttonColors[i]));
					}
					for(int i = 0; i < Integer.parseInt(playersPerTeam); i++) {
						for(int n = 0; n < columnCount; n++) {
							if(n == 0) {
								dataTable.add(new DataTableColumn("Text", "Player " + (i + 1)));
							} else {
								dataTable.add(new DataTableColumn("SelectOne", ""));
							}
						}
					}
				} else if(!responseTo.equals("")) {
					columnCount = 5;
					dataTable.add(new DataTableColumn("Text", "Button Color"));
					String columnOut = responseTo.replace("-", "");
					for(int i = 0; i < 4; i++) {
						dataTable.add(new DataTableColumn("Color", buttonColors[i]));
					}
					for(int i = 0; i < columnCount; i++) {
						if(i == 0) {
							dataTable.add(new DataTableColumn("Text", columnOut));
						} else {
							dataTable.add(new DataTableColumn("SelectOne", ""));
						}
					}
				}
			} else if(responseType.equals("Sequence")) {
				columnCount = Integer.parseInt(teamCount);
//				dataTable.add("Hi");
//				dataTable.add("Hi");
//				dataTable.add("Hi");
//				dataTable.add("Hi");
//				colorValues.add("Hi");
//				colorValues.add("Hi");
//				colorValues.add("Hi");
//				colorValues.add("Hi");
			}
		}
	}

	public List<String> responseToList() {
//		List<String> responses = new ArrayList<String>();
//		List<String> returnValue = new ArrayList<String>();
//		for(int i = 0; i < dataTable.size(); i++) {
//			if(dataTable.get(i).getHeader().equals("SelectOne")) {
//				responses.add(dataTable.get(i).getProperty());
//			}
//		}
//		for(int i = 0; i < Integer.parseInt(teamCount); i++) {
//			for(int n = 0; n < responses.size(); n++) {
//				if((n % (Integer.parseInt(teamCount) + i)) == 0) {
//					returnValue.add(responses.get(i));
//				}
//			}
//		}		
//		return returnValue;
		return null;
	}
	
	public void onDrop() {
		
	}
	
	public int responseToTeamId() {
		if(responseTo.contains("Team")) {
			return Integer.parseInt(responseTo.replace("Team ", ""));
		}
		return 0;
	}
	
	public int responseToPlayerId() {
		if(responseTo.contains("Player")) {
			return Integer.parseInt(responseTo.replace("--Player ", ""));
		}
		return 0;	
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setLedColor(String ledColor) {
		this.ledColor = ledColor;
	}
	
	public void setLedDuration(String ledDuration) {
		this.ledDuration = ledDuration;
	}
	
	public void setBuzzerOn(String buzzerOn) {
		this.buzzerOn = buzzerOn;
	}
	
	public void setBuzzerDuration(String buzzerDuration) {
		this.buzzerDuration = buzzerDuration;
	}
	
	public void setResponseTo(String responseTo) {
		this.responseTo = responseTo;
	}
	
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	
	public void setDataTable(List<DataTableColumn> dataTable) {
		this.dataTable = dataTable;
	}
	
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	
	public void setColorValues(List<String> colorValues) {
		this.colorValues = colorValues;
	}
	
	public int getId() {
		return this.id + 1;
	}
	
	public int getTeamId() {
		return this.teamId;
	}
	
	public int getPlayerId() {
		return this.playerId;
	}
	
	public String getText() {
		return this.text;
	}	
	
	public String getLedColor() {
		return this.ledColor;
	}
	
	public String getLedDuration() {
		return this.ledDuration;
	}
	
	public String getBuzzerOn() {
		return this.buzzerOn;
	}
	
	public String getBuzzerDuration() {
		return this.buzzerDuration;
	}
	
	public String getResponseTo() {
		return this.responseTo;
	}
	
	public String getResponseType() {
		return this.responseType;
	}
	
	public List<DataTableColumn> getDataTable() {
		return this.dataTable;
	}
	
	public int getColumnCount() {
		return this.columnCount;
	}
	
	public List<String> getColorValues() {
		return this.colorValues;
	}
}
