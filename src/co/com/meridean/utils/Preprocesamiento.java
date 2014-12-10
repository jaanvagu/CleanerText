package co.com.meridean.utils;

import java.util.*;
import java.util.regex.*;

public class Preprocesamiento {


    private static ArrayList<String> listaMensajesProcesados;

    private static Set<String> setCleanWords;

    private static final String[] listaPalabrasVacias = {
            "a","acuerdo","adelante","ademas","adrede","ahi","ahora","alla","alli","alrededor", "antano","antaño","ante","antes",
            "apenas","aproximadamente","aquel","aquella", "aquellas","aquello","aquellos","aqui","arriba","abajo","asi",
            "aun","aunque","b","bajo","bastante","bien","breve","c","casi","cerca","claro","como","con", "conmigo","contigo","contra",
            "cual","cuales","cuando","cuanta","cuantas","cuanto","cuantos","d","de","debajo","del","delante","demasiado",
            "dentro","deprisa","desde","despacio","despues","detras","dia","dias", "donde","dos","durante","e","el","ella",
            "ellas","ellos","en","encima","enfrente", "enseguida","entre","email","es","esa","esas","ese","eso","esos","esta",
            "estado","estados","estas","este","esto","estos","ex", "excepto","f","final","fue","fuera","fueron","g",
            "general","gran","h","ha","habia","habla", "hablan","hace","hacia","han","hasta","hay","hola","horas","hoy","i","incluso","informo",
            "j","junto", "k","l","la","lado","las","le","lejos","lo","los","luego","m","mal","mail","mas","mayor","me","medio", "mejor","menos",
            "menudo","mi","mia","mias","mientras","mio","mios","mis", "mismo","mucho","muy","n","nada","nadie","ninguna","no",
            "nos","nosotras","nosotros","nuestra","nuestras", "nuestro","nuestros","nueva","nuevo","nunca","ñ","o","os","otra","otros","p",
            "pais","para","parte", "pasado","peor","pero","poco","por","porque","pronto","proximo","puede","pues","q","qeu","que", "quien",
            "quienes","quiza","quizas","r","raras","repente","s","salvo", "se","segun","ser","sera","si","sido","siempre",
            "sin","sobre","solamente", "solo","son","soyos","su","supuesto","sus","suya","suyas","suyo","t","tal","tambien", "tampoco",
            "tarde","te","temprano","ti","tiene","todavia","todo","todos","tras","tu", "tus","tuya","tuyas","tuyo","tuyos","u","un","una",
            "unas","uno","unos","usted","ustedes","v","veces", "vez","vosotras","vosotros","vuestra","vuestras","vuestro","vuestros","w","x","y",
            "ya","yo","z"
    };

    private static final String[] listaInicialesRedesSociales = {"@", "#", "rt", "twitter", "tweet", "face", "facebook", "fb"};

    private static final String[] listaPatronesOnomatopeyas = {
            "[ja]{2,}", "[je]{2,}", "[ji]{2,}", "[ha]{2,}", "[he]{2,}", "[hi]{2,}", "[wo]{2,}", "[oh]{2,}", "[ah]{2,}", "[o]{2,}",
            "[a]{2,}", "[e]{2,}", "[u]{2,}", "[i]{2,}", "[m]{2,}", "[uf]{2,}", "[uy]{2,}", "[bu]{2,}", "[ura]{3,}", "[mucho]{5,}", "[hola]{4,}"
    };


    //Método que recorre la lista de mensajes y ejecuta una función según el tipo que le ingrese como parámetro
    //Se apoya en métodos auxiliares definidos posteriormente. 
    public static ArrayList<String> ejecutarPreprocesamiento(ArrayList<String> wordsList){

        listaMensajesProcesados = new ArrayList<String>();
        setCleanWords = new HashSet<String>();

        for(int i=0; i<wordsList.size(); i++){

            String palabra = wordsList.get(i);

            if ( !esRuidoTuiter(palabra) && !esURL(palabra) && !esOnomatopeya(palabra) ){
                palabra = convertirAMinusculas(palabra);
                palabra = quitarAcentos(palabra);
                palabra = eliminarCaracteresDiferentesALetras(palabra);
                if(!esPalabraVacia(palabra) && !palabra.isEmpty()){
                    setCleanWords.add(palabra);
                }
            }
        }

        Iterator<String> iterator = setCleanWords.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            listaMensajesProcesados.add(temp);
        }

        return listaMensajesProcesados;
    }

    //Identidica si una palabra contiene ruido generado por la red social tuiter.
    private static boolean esRuidoTuiter(String palabra){
        for(int i=0; i<listaInicialesRedesSociales.length; i++){
            if(palabra.contains(listaInicialesRedesSociales[i])){
                return true;
            }
        }
        return false;
    }

    //Identifica si una palabra es una direccion web o URL
    private static boolean esURL(String palabra){
        String recorteDosCaracteres, recorteTresCaracteres;
        recorteTresCaracteres = "#";
        recorteDosCaracteres = "#";
        if(palabra.length()>=4){
            if(palabra.length()>=5){
                recorteTresCaracteres = palabra.substring(palabra.length()-4,palabra.length()-3);
            }
            recorteDosCaracteres = palabra.substring(palabra.length()-3,palabra.length()-2);
        }

        if(palabra.startsWith("http") || palabra.startsWith("www") || recorteDosCaracteres.equals(".") || recorteTresCaracteres.equals(".")){
            return true;
        }
        else{
            return false;
        }
    }

    //Identifica si una palabra es onomatopeya
    private static boolean esOnomatopeya(String palabra){
        for(int i=0; i<listaPatronesOnomatopeyas.length; i++){
            Pattern patron = Pattern.compile(listaPatronesOnomatopeyas[i]);
            Matcher verificador = patron.matcher(palabra);
            if(verificador.matches()){
                return true;
            }
        }
        return false;
    }

    //Recibe una palabra y la retorna con cada una de sus letras en minuscula.
    private static String convertirAMinusculas(String mensaje){
        return mensaje.toLowerCase();
    }

    //Elimina los acentos o tildes que encuentre en una palabra. Es estático para permitir su uso en la clase Lematizar. Y en clase
    //DiccionarioEspanolIngles.
    public static String quitarAcentos(String palabra) {
        palabra = palabra.replaceAll("á","a");
        palabra = palabra.replaceAll("é","e");
        palabra = palabra.replaceAll("í","i");
        palabra = palabra.replaceAll("ó","o");
        palabra = palabra.replaceAll("ú","u");
        palabra = palabra.replaceAll("à","a");
        palabra = palabra.replaceAll("è","e");
        palabra = palabra.replaceAll("ì","i");
        palabra = palabra.replaceAll("ò","o");
        palabra = palabra.replaceAll("ù","u");
        return palabra;
    }

    //Identifica si una palabra es "Palabra Vacia" (Las palabras vacias estan definidas en una lista, que es un atributo de clase).
    private static boolean esPalabraVacia(String palabra){
        for(int i=0; i<listaPalabrasVacias.length; i++){
            if(palabra.equals(listaPalabrasVacias[i])){
                return true;
            }
        }
        return false;
    }

    //Filtra caracteres a través de código ASCII, elimina los que no estén contenidos entre la a....z, los que no sean espacios
    //y lo diferente a "ñ"
    private static String eliminarCaracteresDiferentesALetras(String palabra){
        StringBuilder tempPalabra = new StringBuilder();
        for (int i=0; i<palabra.length(); i++){
            int codigoASCIILetra = palabra.codePointAt(i);
            if(codigoASCIILetra>=97 && codigoASCIILetra<=122 || codigoASCIILetra==241 || codigoASCIILetra==32){
                tempPalabra.append(palabra.charAt(i));
            }
        }
        return tempPalabra.toString();
    }

}