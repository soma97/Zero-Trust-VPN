package org.unibl.etf.srs.accessgw.controller;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.srs.accessgw.command.CommandService;
import org.unibl.etf.srs.accessgw.command.LogsService;

@RestController
@RequestMapping("/")
public class CommandController {

	private final CommandService commandService;
	private final LogsService logsService;

	@Autowired
	public CommandController(CommandService commandService, LogsService logsService) {
		this.commandService = commandService;
		this.logsService = logsService;

		logsService.readLogsInLoop();
	}

	// it would be much better if these logs were exposed using websocket...
	@GetMapping("logs")
	public List<String> allLogs() {
		List<String> result = logsService.getLogs();
		logsService.setLogs(new LinkedList<>());
		return result;
	}

	@PostMapping("command")
	public ResponseEntity postCommands(@RequestBody List<String> commands) {
		commandService.executeCommands(commands);
		return ResponseEntity.noContent().build();
	}
	
}
