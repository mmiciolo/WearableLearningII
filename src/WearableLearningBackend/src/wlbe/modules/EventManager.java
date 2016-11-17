package wlbe.modules;

import java.util.ArrayList;

import wlbe.event.Event;
import wlbe.module.Module;
import wlbe.module.ModuleManager;
import wlbe.module.ModuleManager.Modules;
import wlbe.task.Task;

public class EventManager extends Module {
	
	ArrayList<Event> events = new ArrayList<Event>();
	
	public EventManager(Modules moduleId) {
		super(moduleId);
	}
	
	public void BroadcastEvent(Event e) {
		TaskManager taskManager = (TaskManager) ModuleManager.getModule(ModuleManager.Modules.TASK_MANAGER);
		for(Task task : taskManager.tasks) {
			task.eventHandler(e);
		}
		for(Module module : ModuleManager.getInstance().getModules()) {
			module.eventHandler(e);
		}
	}
	
	public void setup() {
		
	}
	
	public void update() {

	}
	
	public void cleanup() {
		
	}
}