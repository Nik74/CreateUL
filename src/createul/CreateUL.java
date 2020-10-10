/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createul;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author ngerasimov
 */

public class CreateUL
{       
    public static final String pathFile = "./XML.csv";
    public static final String pathZip = "./Package.zip";
    public static final String pathJrxml = "./jasper/UL.jrxml";
    public static final String pathPdf = "./";
    public static final String pathWorkingDirectory = "./";
    public static final String pathCpverify = "./cpverify.exe";
    
    private static DataBean produce(String grcNumber, String grcName,
            String grcVersion, String grcNumberUL, String grcFileNameDoc, 
            String grcCheckSumDoc, String grcFileNameXml, String grcCheckSumXml)
    {
        DataBean dataBean = new DataBean();
        
        dataBean.setFileNameDoc(grcFileNameDoc);
        dataBean.setName(grcName);
        dataBean.setNumber(grcNumber);
        dataBean.setNumberUL(grcNumberUL);
        dataBean.setVersion(grcVersion);
        dataBean.setCheckSumDoc(grcCheckSumDoc);
        dataBean.setFileNameXml(grcFileNameXml);
        dataBean.setCheckSumXml(grcCheckSumXml);
        
        return dataBean;
    }
    
    public static void main(String[] args) throws JRException
    {
        /*
        * grcNumber - номер документа
        * grcName - наименование документа
        * grcVersion - версия документа
        * grcCheckSumDoc - контрольная сумма документа Word
        * grcNumberUL - номер УЛ
        * grcFileNameDoc - наименование файла документа Word
        * grcCheckSumXml - контрольная сумма документа Xml
        * grcFileNameXml - наименование файла документа Xml
        */
         String grcNumber = null, grcName = null, grcVersion = null,
                 grcCheckSumDoc = null, grcFileNameDoc = null, grcNumberUL = null,
                 grcCheckSumXml = null, grcFileNameXml = null;
        
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(pathZip)))
        {
            ZipEntry entry;
            String name;
            byte[] arrayOfByte = new byte[1024];
            
            while((entry = zip.getNextEntry()) != null)
            {        
                name = entry.getName();

                if (name.contains(".xml"))
                {    
                    grcFileNameXml = name;
                    
                    FileOutputStream fOut = new FileOutputStream(pathFile);

                    int i = -1;
    
                    while ((i = zip.read(arrayOfByte)) != -1)
                        fOut.write(arrayOfByte, 0, i);
                    
                    fOut.close();
                    
                    List<String> cmds = new ArrayList<>();
                    cmds.add(pathCpverify);
                    cmds.add("-mk");
                    cmds.add("-alg");
                    cmds.add("GR3411_2012_256");
                    cmds.add(pathFile);
                    
                    ProcessBuilder processBuilder = new ProcessBuilder(cmds);

                    Process process = processBuilder.start();
                    
                    InputStream inputStream = process.getInputStream();
                    
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    
                    String line;
                    
                    while((line = bufferedReader.readLine()) != null)
                        grcCheckSumXml = line;
                    
                    InputStreamReader fIn = new InputStreamReader(new FileInputStream(pathFile), "UTF-8");
            
                    int numberName = 0;
                    i = -1;

                    String result="";
            
                    while((i = fIn.read()) != -1)
                    {
                        if (String.valueOf((char)i).contains("\n"))
                        {     
                            if (result.contains("<number>"))
                            {
                                if (result.contains("№"))
                                    grcNumber = result.substring(result.indexOf('>', 0) + 3, 
                                        result.indexOf('<', result.indexOf('>', 0)));   
                                else
                                    grcNumber = result.substring(result.indexOf('>', 0) + 1, 
                                        result.indexOf('<', result.indexOf('>', 0)));
                                
                                grcNumberUL = grcNumber + "-УЛ";
                            }
                            else if (result.contains("<name>") && numberName == 0)
                            {
                                grcName = result.substring(result.indexOf('>', 0) + 1, 
                                    result.indexOf('<', result.indexOf('>', 0)));
                                
                                numberName++;
                            }
                            else if(result.contains("<versionId>"))
                                grcVersion = result.substring(result.indexOf('>', 0) + 1, 
                                    result.indexOf('<', result.indexOf('>', 0)));
                            else if(result.contains("<iterationId>"))
                            {
                                grcVersion = grcVersion + ".";
                                grcVersion = grcVersion + result.substring(result.indexOf('>', 0) + 1, 
                                    result.indexOf('<', result.indexOf('>', 0)));
                            }
                            else if(result.contains("<fileName>") && (result.contains(".doc")))
                            {
                                
                                if (result.contains(".doc"))
                                    grcFileNameDoc = result.substring(result.indexOf('>', 0) + 1, 
                                        result.indexOf('<', result.indexOf('>', 0)));
                                else if (result.contains(".docx"))
                                    grcFileNameDoc = result.substring(result.indexOf('>', 0) + 1, 
                                        result.indexOf('<', result.indexOf('>', 0)));
                            }
                            
                            result = "";
                        }
                        else
                            result += (char)i;
                    }

                    fIn.close();
                    
                    File delFile = new File(pathFile);
                    delFile.delete();
                }
                
                if (name.contains(grcFileNameDoc))
                {                    
                    try (FileOutputStream fout = new FileOutputStream(pathWorkingDirectory + name.substring(name.indexOf("/") + 1))) 
                    {
                        for (int c = zip.read(); c!=-1; c=zip.read())
                            fout.write(c);
                        
                        fout.flush();
                    }
                    
                    List<String> cmds = new ArrayList<>();
                    cmds.add(pathCpverify);
                    cmds.add("-mk");
                    cmds.add("-alg");
                    cmds.add("GR3411_2012_256");
                    cmds.add(pathWorkingDirectory + name.substring(name.indexOf("/") + 1));
                    
                    ProcessBuilder processBuilder = new ProcessBuilder(cmds);

                    Process process = processBuilder.start();
                    
                    InputStream inputStream = process.getInputStream();
                    
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    
                    String line;
                    
                    while((line = bufferedReader.readLine()) != null)
                        grcCheckSumDoc = line;
                    
                    File delFile = new File(pathWorkingDirectory + name.substring(name.indexOf("/") + 1));
                    delFile.delete();
                }
                
                ArrayList<DataBean> dataBeanList = new ArrayList<>();
        
                dataBeanList.add(produce(grcNumber, grcName, grcVersion,
                        grcNumberUL, grcFileNameDoc, grcCheckSumDoc,
                        grcFileNameXml, grcCheckSumXml));
        
                JasperReport jasperReport = JasperCompileManager.compileReport(pathJrxml);
        
                Map parameters = new HashMap();
                
                parameters.put("grcNumber", grcNumber);
                parameters.put("grcName", grcName);
                parameters.put("grcVersion", grcVersion);
                parameters.put("grcNumberUL", grcNumberUL);
                parameters.put("grcFileNameDoc", grcFileNameDoc);
                parameters.put("grcCheckSumDoc", grcCheckSumDoc);
                parameters.put("grcFileNameXml", grcFileNameXml);
                parameters.put("grcCheckSumXml", grcCheckSumXml);
        
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                        parameters, new JRBeanCollectionDataSource(dataBeanList));
        
                JasperExportManager.exportReportToPdfFile(jasperPrint, pathPdf + 
                        grcFileNameDoc.substring(0, grcFileNameDoc.indexOf(".")) + ".pdf");
            }
            
            zip.close();
  
        } catch (IOException ex)
        {
            Logger.getLogger(CreateUL.class.getName()).log(Level.SEVERE, null, ex);
        }  
    } 
}