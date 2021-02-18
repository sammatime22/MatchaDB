package com.matchadb.surface;

import com.matchadb.models.request.MatchaDbRawRequestObject;
import com.matchadb.models.response.MatchaDbResponseObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class acts as the REST interface for the MatchaDB application.
 */
@Controller
public class MatchaDbInterface {

    MatchaDbRequestParser requestParser;

    // Contstructor
    public MatchaDbInterface() {
        
    }

    // Generic GET
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MatchaDbResponseObject get(MatchaDbRawRequestObject request) {
        return null;
    }

    // Generic POST
    @RequestMapping(value = "", method = RequestMethod.POST)
    public MatchaDbResponseObject post(MatchaDbRawRequestObject request) {
        return null;
    }

    // Generic UPDATE
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public MatchaDbResponseObject update(MatchaDbRawRequestObject request) {
        return null;
    }

    // Generic DELETE
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public MatchaDbResponseObject delete(MatchaDbRawRequestObject request) {
        return null;
    }
}