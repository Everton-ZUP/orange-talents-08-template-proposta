package br.com.zupacademy.propostas.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReturnError {

    private List<String> global = new ArrayList<>();
    private List<HashMap> fields = new ArrayList<>();

  //  private List<FieldErro> fields = new ArrayList<>();

    public void AddError (String errorMessage){
        global.add(errorMessage);
    }

    public void addErrorField (String campo, Object valor, String mensagem){

        HashMap<String,Object> map = new HashMap<>();
        map.put("Field",campo);
        map.put("Value",valor);
        map.put("Message",mensagem);
        map.put("Time", LocalDateTime.now());

        fields.add(map);
    }

    public List<?> getGlobal() {
        return global;
    }

    public List<?> getFields() {
        return fields;
    }
}
