package wlbe.modules;

import java.util.ArrayList;

import wlbe.module.Module;
import wlbe.module.ModuleManager;
import wlbe.module.ModuleManager.Modules;
import wlbe.task.Task;

public class TaskManager extends Module {
	
	ArrayList<Task> tasks = new ArrayList<Task>();
	
	public TaskManager(Modules moduleId) {
		super(moduleId);
	}
	
	public void addTask(Task task) {
		try {
			new Thread(task).start();
			release();
			tasks.add(task);
			release();
			Logger logger = (Logger) ModuleManager.getModule(ModuleManager.Modules.LOGGER);
			logger.write("Task " + task.getName() + " Added...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeTask(Task task) {
		for(Task t : tasks) {
			if(t.equals(task)) {
				t.shutdown();
				break;
			}
		}
		try {
			accquire();
			tasks.remove(task);
			release();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger logger = (Logger) ModuleManager.getModule(ModuleManager.Modules.LOGGER);
		logger.write("Task " + task.getName() + " Removed...");
	}
	
	public void setup() {
		
	}
	
	public void update() {

	}
	
	public void cleanup() {
		for(Task t : tasks) {
			t.shutdown();
		}
	}
	
	public ArrayList<Task> getTasks() {
		return this.tasks;
	}
}
