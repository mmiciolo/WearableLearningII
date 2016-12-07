package wlbe.tasks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import wl.shared.json.packets.ButtonPacket;
import wl.shared.json.packets.DisplayPacket;
import wl.shared.json.packets.GameStartPacket;
import wl.shared.json.packets.GameStatePacket;
import wl.shared.json.packets.data.ButtonColor;
import wl.shared.json.packets.data.ButtonData;
import wl.shared.model.Button;
import wlbe.event.IEvent;
import wlbe.events.PacketRecieved;
import wlbe.model.PlayerData;
import wlbe.module.ModuleManager;
import wlbe.modules.Logger;
import wlbe.modules.Server;
import wlbe.modules.TaskManager;
import wlbe.packets.ConnectPacket;
import wlbe.packets.DisconnectPacket;
import wlbe.packets.JSONPacket;
import wlbe.task.Task;

public class GameInstance extends Task {
	
	private int gameInstanceId;
	private int gameId;
	private TaskManager taskManager;
	private MySQLDaemon mySQLDaemon;
	private ArrayList<PlayerData> players = new ArrayList<PlayerData>();
	Logger logger;
	
	public GameInstance(int gameInstanceId, int gameId) {
		setName("Game Instance " + gameInstanceId);
		this.gameInstanceId = gameInstanceId;
		this.gameId = gameId;
		mySQLDaemon = new MySQLDaemon();
		taskManager = (TaskManager) ModuleManager.getModule(ModuleManager.Modules.TASK_MANAGER);
		taskManager.addTask(mySQLDaemon);
		logger = (Logger) ModuleManager.getModule(ModuleManager.Modules.LOGGER);
	}
	
	public void update() {
		for(PlayerData player : players) {
			
		}
	}
	
	public void cleanup() {
		for(PlayerData player : players) {
			removePlayer(player);
		}
		taskManager.removeTask(mySQLDaemon);
	}
	
	public void endInstance() {
		taskManager.removeTask(this);
	}
	
	public void eventHandler(IEvent e) {
		if(e instanceof PacketRecieved) {
			PacketRecieved packetRecieved = (PacketRecieved) e;
			switch(packetRecieved.getPacket().getType()) {
				case PLAYER_CONNECT:
					playerConnect((ConnectPacket)packetRecieved.getPacket());
					break;
				case PLAYER_DISCONNECT:
					playerDisconnect((DisconnectPacket)packetRecieved.getPacket());
					break;
				case JSON_PACKET:
					handleJSONPacket((JSONPacket)packetRecieved.getPacket());
					break;
				default:
					break;
			}
		}
	}
	
	public void playerConnect(ConnectPacket packet) {
		if(packet.getGameInstanceId() == gameInstanceId) {
			for(PlayerData player : players) {
				if(player.getPlayerName().equals(packet.getStudentName())) {
					logger.write("Client Reconnected...");
					return;
				}
			}
			PlayerData player = new PlayerData(packet.getStudentName(), packet.getClientData());
			players.add(player);
			setupNewPlayer(player);
		}
	}
	
	public void playerDisconnect(DisconnectPacket packet) {
		if(packet.getGameInstanceId() == gameInstanceId) {
			for(int i = 0; i < players.size(); i++) {
				PlayerData player = (PlayerData) players.toArray()[i];
				if(player.getPlayerName().equals(packet.getStudentName())) {
					removePlayer(player);
					players.remove(player);
					Logger logger = (Logger) ModuleManager.getModule(ModuleManager.Modules.LOGGER);
					logger.write("Client Disconnected...");
				}
			}
		}
	}
	
	private void setupNewPlayer(PlayerData player) {
		try {
			String names[] = player.getPlayerName().split(",");
			Statement statement = mySQLDaemon.getConnection().createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM student WHERE lastName=" + "'" + names[0] + "'" + " AND firstName=" + "'" + names[1].replace(" ", "") + "'");
			if(results.next()) {
				String returnId[] = {"playerId"};
				PreparedStatement preparedStatement = mySQLDaemon.getConnection().prepareStatement("INSERT INTO players (gameInstanceId, studentId, currentGameState) VALUES (?, ?, ?)", returnId);
				preparedStatement.setInt(1, gameInstanceId);
				preparedStatement.setInt(2, results.getInt("studentId"));
				preparedStatement.setInt(3, player.getCurrentGameState());
				preparedStatement.execute();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if(rs.next()) {
					player.setPlayerId((int)rs.getLong(1));
				}
				rs.close();
				preparedStatement.close();
				results.close();
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sendGameStart(player);
		sendGameState(player);
	}
	
	private void sendGameStart(PlayerData player) {
		Server server = (Server) ModuleManager.getModule(ModuleManager.Modules.SERVER);
		GameStartPacket startGamePacket = new GameStartPacket();
		JSONPacket jsonPacket = new JSONPacket();
		jsonPacket.setJSONPacket(startGamePacket);
		server.write(player.getClientData(), jsonPacket);
	}
	
	private void sendGameState(PlayerData player) {
		String text = "";
		try {
			Statement statement = mySQLDaemon.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM gameState WHERE gameId=" + gameId + " AND gameStateCount=" + player.getCurrentGameState());
			if(resultSet.next()) {
				text = resultSet.getString("textContent");
				player.setGameStateId(resultSet.getInt("gameStateId"));
			}
		} catch (Exception e) {
			
		}
		
		Server server = (Server) ModuleManager.getModule(ModuleManager.Modules.SERVER);
		GameStatePacket gameStatePacket = new GameStatePacket();
		gameStatePacket.getGameStatePacketData().getDisplayData().text = text;
		JSONPacket jsonPacket = new JSONPacket();
		jsonPacket.setJSONPacket(gameStatePacket);
		server.write(player.getClientData(), jsonPacket);
	}
	
	private boolean removePlayer(PlayerData playerData) {
		try {
			PreparedStatement statement = mySQLDaemon.getConnection().prepareStatement("DELETE FROM players WHERE playerId=" + playerData.getPlayerId());
			statement.execute();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void handleJSONPacket(JSONPacket packet) {
		switch(packet.getJSONPacket().getType()) {
			case BUTTON:
				ButtonPacket buttonPacket = (ButtonPacket) packet.getJSONPacket();
				handleButtonPress(buttonPacket);
				break;
			default:
				break;
		}
	}
	
	private void handleButtonPress(ButtonPacket buttonPacket) {
		ButtonColor buttonColor = ButtonColor.values()[buttonPacket.getButtonData().getButtonNumber()];
		PlayerData player = null;
		for(PlayerData playerp : players) {
			if(playerp.getPlayerId() == buttonPacket.getButtonData().getplayerId()) {
				player = playerp;
			}
		}
		setNextGameStateForPlayer(buttonColor, player);
		sendGameState(player);
	}
	
	private void setNextGameStateForPlayer(ButtonColor buttonColor, PlayerData player) {		
		try {
			Statement statement = mySQLDaemon.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM gameStateTransitions WHERE gameStateId=" + player.getGameStateId() +" AND singlePushButtonColor=" + buttonColor.ordinal());
			if(resultSet.next()) {
				int nextGameState = resultSet.getInt("nextGameStateTransition");
				if(nextGameState != 0) {
					player.setCurrentGameState(nextGameState);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getGameId() {
		return this.gameId;
	}
	
	public int getGameInstanceId() {
		return this.gameInstanceId;
	}
}
