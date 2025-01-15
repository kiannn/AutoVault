
package com.example.CarDealerShip.Models;

public class DocumentDTO {
    
    String name;
    
    FileExtension extension;

    String caseSensitive ;
    
    public String getName() {
        System.out.println(" DocumentDTO.getName() name = "+name);
        return name;
    }

    public void setName(String name) {
        System.out.println(" DocumentDTO.setName() name = "+name);
        this.name = name;
    }

    public FileExtension getExtension() {
        System.out.println(" DocumentDTO.getExtension() extension = "+extension);
        return extension;
    }

    public void setExtension(FileExtension extension) {
        System.out.println(" DocumentDTO.setExtension() extension = "+extension);
        this.extension = extension;
    }

    public String getCaseSensitive() {
        System.out.println("DocumentDTO.isIsSensitive()  isSensitive = "+caseSensitive);
        return caseSensitive;
    }

    public void setCaseSensitive(String caseSensitive) {
        System.out.println("DocumentDTO.setIsSensitive()  isSensitive = "+caseSensitive);
        this.caseSensitive = caseSensitive;
    }
    
    
}
