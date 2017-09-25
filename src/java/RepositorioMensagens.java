
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class RepositorioMensagens {
    
    public static ArrayList<Mensagem> recuperaMensagens(ServletContext ctx)
    {
        ArrayList<Mensagem> listaMensagens = new ArrayList<>();
        String arq = "/WEB-INF/mensagens.txt";
        InputStream stream = ctx.getResourceAsStream(arq);
        InputStreamReader reader = new InputStreamReader(stream);
        
        BufferedReader in = new BufferedReader(reader);
        
        String linha = "";
        
        try {
            while((linha = in.readLine()) != null)
            {
                Mensagem m = new Mensagem(linha,in.readLine());
                listaMensagens.add(m);
            }
            reader.close();
        } catch (IOException ex) {
            return null;
        }
        
        return listaMensagens;
        /*ArrayList<Mensagem> listaMensagens = null;

        listaMensagens  = (ArrayList<Mensagem>) req.getAttribute("mensagens");
        if (listaMensagens == null)
        {
            listaMensagens = new ArrayList<>();
            req.setAttribute("mensagens",listaMensagens);
        }
        return listaMensagens;*/
    }
    public static void salvaMensagens(ServletContext ctx,Mensagem msg)
    {
        String arq = "/WEB-INF/mensagens.txt";
        arq = ctx.getRealPath(arq);
        
        File f = new File(arq);
        try {
            FileOutputStream fos = new FileOutputStream(f,true);
            String toWrite = System.lineSeparator() + msg.getMensagem() +System.lineSeparator()+ msg.getRemetente();
            fos.write(toWrite.getBytes());
            fos.close();
        } catch (Exception ex) {
            Logger.getLogger(RepositorioMensagens.class.getName()).log(Level.SEVERE, null, ex);  
        }
        
        /*ArrayList<Mensagem> listaMensagens = null;
            listaMensagens  = (ArrayList<Mensagem>) req.getAttribute("mensagens");
            if (listaMensagens == null)
            {
            listaMensagens = new ArrayList<>();
           
            }
            listaMensagens.add(msg);
            req.setAttribute("mensagens",listaMensagens);*/
    }
}
