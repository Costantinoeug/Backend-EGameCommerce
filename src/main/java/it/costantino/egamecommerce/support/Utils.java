package it.costantino.egamecommerce.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.PrintWriter;
import java.io.StringWriter;


@UtilityClass
@Log4j2
public class Utils {

    public void stampaEccezioni(RuntimeException re){
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter( writer );
        re.printStackTrace( printWriter );
        printWriter.flush();

        String stackTrace = writer.toString();
        System.out.println("Exception: "+stackTrace);
    }
    public Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getAuthServerId() {
        return getTokenNode().get("subject").asText();
    }

    public String getName() {
        return getTokenNode().get("sub").asText();
    }

    public String getEmail() {
        return getTokenNode().get("claims").get("preferred_username").asText();
    }

    private JsonNode getTokenNode() {
        Jwt jwt = getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtAsString;
        JsonNode jsonNode;
        try {
            jwtAsString = objectMapper.writeValueAsString(jwt);
            jsonNode = objectMapper.readTree(jwtAsString);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to retrieve user's info!");
        }
        return jsonNode;
    }


}
