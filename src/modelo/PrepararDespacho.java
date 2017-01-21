package modelo;

public class PrepararDespacho {
    
    public static String[] getColumnNames(){
        return new String[]{
            "ITEM",
            "DESPACHO",
            "REMISION",
            "N° EMPRESA",
            "SELECC.",
            "N° SEIRE",
            "MARCA",
            "FASE",
            "KVA ENT.",
            "KVA SAL.",            
            "TENSION ENT.",
            "TENSION SAL.",
            "SERV. ENT.",
            "SERV. SAL.",
            "TIPO ENT.",
            "TIPO SAL.",
            "OBSERV. ENT.",
            "OBSERV. SAL.",            
            "PESO",
            "ACEITE"
        };
    }
    
    public static Boolean[] getColumnEditables(){
        return new Boolean[]{
            false,//"ITEM°",
            true,//"DESPACHO",1
            false,//"REMISION",2
            false,//"N° EMPRESA",3
            true,//SELECCIONE 4
            false,//"N° SEIRE",5
            false,//"MARCA",6
            false,//"FASE.",7
            false,//"KVA ENT"8
            true,//"KVA SAL",9
            false,//"TENSION ENT",10
            true,//"TENSION SAL",11
            false,//"SERV. ENT",12
            true,//"SERV. SAL",13
            false,//"TIPO ENT",14
            true,//"TIPO SAL",15
            false,//"OBSERV. ENT",16
            true,//"OBSERV. SAL",17           
            false,//"PESO",18
            false,//"ACEITE",19
        };
    }
    
    public static Class[] getColumnClass(){
        return new Class[]{
            Integer.class,//"ITEM",
            Object.class,//"DESPACHO",
            Object.class,//"REMISION",
            Object.class,//"N° EMPRESA",
            Boolean.class,//"N° EMPRESA",
            Object.class,//"N° SEIRE",
            Object.class,//"MARCA",
            Integer.class,//"FASE",
            Double.class,//"KVA ENT",
            Double.class,//"KVA SAL",            
            String.class,//"TENSION ENT",
            String.class,//"TENSION SAL",
            String.class,//"SERV. ENT",
            String.class,//"SERV. SAL",
            String.class,//"TIPO ENT",
            String.class,//"TIPO SAL",
            String.class,//"OBSERV. ENT",
            String.class,//"OBSERV. SAL",            
            Integer.class,//"PESO",
            Integer.class,//"ACEITE",            
        };
    }    
    
}