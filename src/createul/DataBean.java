/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createul;

/**
 *
 * @author ngerasimov
 */
public class DataBean
{
    public static String grcNumber, grcName, grcVersion = null, 
            grcCheckSumDoc = null, grcFileNameDoc = null, grcNumberUL,
            grcCheckSumXml = null, grcFileNameXml = null;
    
    public String getNumber()
    {
        return grcNumber;
    }
    
    public void setNumber(String grcNumber)
    {
        this.grcNumber = grcNumber;
    }
    
    public String getNumberUL()
    {
        return grcNumberUL;
    }
    
    public void setNumberUL(String grcNumberUL)
    {
        this.grcNumberUL = grcNumberUL;
    }
    
    public String getName()
    {
        return grcName;
    }
    
    public void setName(String grcName)
    {
        this.grcName = grcName;
    }
    
    public String getVersion()
    {
        return grcVersion;
    }
    
    public void setVersion(String grcVersion)
    {
        this.grcVersion = grcVersion;
    }
          
    public String getFileNameDoc()
    {
        return grcFileNameDoc;
    }
    
    public void setFileNameDoc(String grcFileNameDoc)
    {
        this.grcFileNameDoc = grcFileNameDoc;
    }
    
    public String getCheckSumDoc()
    {
        return grcCheckSumDoc;
    }
    
    public void setCheckSumDoc(String grcCheckSumDoc)
    {
        this.grcCheckSumDoc = grcCheckSumDoc;
    }
    
    public String getFileNameXml()
    {
        return grcFileNameXml;
    }
    
    public void setFileNameXml(String grcFileNameXml)
    {
        this.grcFileNameXml = grcFileNameXml;
    }
    
    public String getCheckSumXml()
    {
        return grcCheckSumXml;
    }
    
    public void setCheckSumXml(String grcCheckSumXml)
    {
        this.grcCheckSumXml = grcCheckSumXml;
    }
}