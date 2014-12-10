package co.com.meridean.utils;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class Util {

    private JFileChooser selectorArchivo;
    private FileNameExtensionFilter filtroExtensionArchivo;
    private File file;
    private FileWriter writer;
    private FileReader reader;
    private BufferedWriter bufferWriter;
    private BufferedReader bufferedReader;

    public String getPathFile(String tipo) {
        int opcionSeleccionada;
        String rutaArchivo = "";
        selectorArchivo = new JFileChooser("C:/Users/Meridean HP/Desktop");
        filtroExtensionArchivo = new FileNameExtensionFilter("Archivos de texto (."+tipo+")", tipo);
        selectorArchivo.setFileFilter(filtroExtensionArchivo);
        opcionSeleccionada = selectorArchivo.showOpenDialog(selectorArchivo);

        if(opcionSeleccionada == JFileChooser.APPROVE_OPTION) {
            rutaArchivo = selectorArchivo.getSelectedFile().getAbsolutePath();
        }
        else {
            System.exit(0);
        }

        return rutaArchivo;
    }

    public String loadFileText() {
        String textLoad = "";
        try{
            String path = getPathFile("txt");
            file = new File(path);
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);
            StringBuilder sbText = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                sbText.append(line);
                sbText.append(" ");
            }
            textLoad = sbText.toString();
        } catch (Exception e){
            System.err.println("Problema cargando archivo\n" + e.getMessage());
        }

        return textLoad;
    }

    public void createTextFile(){
        try{
            file = new File("C:/Users/Meridean HP/Desktop/cleanText.txt");
            writer = new FileWriter(file);
            bufferWriter = new BufferedWriter(writer);
        } catch (Exception e){
            System.err.println("Problema creando file" + e.getMessage());
        }
    }

    public void closeTextFile(){
        try{
            bufferWriter.close();
        }
        catch(Exception e){
            System.err.println("Error al cerrar Archivo texto: " + e.getMessage());
        }
    }

    public void writeWordInFile(String line){
        try{
            bufferWriter.write(line);
        }
        catch(Exception e){
            System.err.println("Error al escribir linea Archivo texto: " + e.getMessage());
        }
    }

    public void writeLineInFile(String line){
        try{
            bufferWriter.write(line);
            bufferWriter.newLine();
        }
        catch(Exception e){
            System.err.println("Error al escribir linea Archivo texto: " + e.getMessage());
        }
    }

    public ArrayList<String> textToWordsList(String text){
        ArrayList<String> wordsList =  new ArrayList<String>();
        Set<String> setWords = new HashSet<String>();
        StringTokenizer tokensText = new StringTokenizer(text);
        System.out.println("tam: "+tokensText.countTokens());
        while(tokensText.hasMoreTokens()){
            String word = tokensText.nextToken().trim();
            if(!word.isEmpty()){
                setWords.add(word);
            }
        }

        Iterator<String> iterator = setWords.iterator();
        while (iterator.hasNext()) {
            wordsList.add(iterator.next());
        }

        return wordsList;
    }

    public  ArrayList<String> processText(){
        String text = loadFileText();
        ArrayList<String> wordsList = textToWordsList(text);

        ArrayList<String> cleanWordsList;
        cleanWordsList = new ArrayList<String>(Preprocesamiento.ejecutarPreprocesamiento(wordsList));

        return cleanWordsList;
    }

    public void cleanText(){
        ArrayList<String> cleanWordsList = processText();
        createTextFile();
        for(int i=0; i<cleanWordsList.size(); i++){
            String word = cleanWordsList.get(i);
            writeWordInFile(word + " ");
        }
        closeTextFile();
    }

    public void deleteUrlsDuplicated(){
        String text = loadFileText();
        ArrayList<String> wordsList = textToWordsList(text);

        createTextFile();

        for(int i=0; i<wordsList.size(); i++){
            writeLineInFile(wordsList.get(i));
        }

        closeTextFile();

        System.out.println("new: "+wordsList.size());
    }
}
