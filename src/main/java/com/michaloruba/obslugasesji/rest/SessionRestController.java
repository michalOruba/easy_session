package com.michaloruba.obslugasesji.rest;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.service.SessionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SessionRestController {
    private SessionService sessionService;

    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessions")
    public List<Session> findAll(){
        return sessionService.findAll();
    }

    @GetMapping("/sessions/{sessionId}")
    public Session findById(@PathVariable("sessionId") int sessionId){
        return sessionService.findById(sessionId);
    }

    @PostMapping("/sessions")
    public Session save(@RequestBody Session session){
        session.setId(0);
        sessionService.save(session);
        return session;
    }

    @PutMapping("/sessions")
    public Session update(@RequestBody Session session){
        sessionService.save(session);
        return session;
    }

    @DeleteMapping("/sessions/{sessionId}")
    public String delete(@PathVariable("sessionId") int sessionId){
        sessionService.deleteById(sessionId);
        return "Deleted session with id - " + sessionId;
    }
}
