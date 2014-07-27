package com.xcoders.ws;

import com.xcoders.controller.GameController;
import com.xcoders.gameitems.Card;
import com.xcoders.gameitems.Player;
import com.xcoders.gameitems.Table;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


@WebService
@SOAPBinding(style = Style.RPC)
public class PokerWS {

    @Resource
    private WebServiceContext webServiceContext;

    
    private HttpSession session;
    private HttpSession getSession(){        
        //check if session is null and return a session
        if(session == null){
            session = ((HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST)).getSession();
        }
        return session;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "joinGame")
    public Integer joinGame(@WebParam(name = "playerId") Long playerId) {
        //passes the session and player id to the game controller and return the table id
       return new GameController().joinGame(getSession(), playerId);
    }
    
    @WebMethod(operationName = "register")
    public void register(@WebParam(name = "userName") String userName,@WebParam(name = "password") String password) throws PokerException{
        new GameController().register(userName, password);
    }
    
    @WebMethod(operationName = "login")
    public Long login(@WebParam(name = "userName") String userName,@WebParam(name = "password") String password) throws PokerException{
        return new GameController().login(userName, password);
    }
    
    @WebMethod(operationName = "getTable")
    public Table getTable(@WebParam(name = "tableId") Integer tableId) throws PokerException{
        return new GameController().getTable(getSession(), tableId);
    }
    
    @WebMethod(operationName = "placeSmallBind")
    public void placeSmallBind(@WebParam(name = "tableId") Integer tableId,@WebParam(name = "userId")Long userId,@WebParam(name = "amount") Integer amount) throws PokerException{
        new GameController().placeSmallBind(getSession(), tableId, userId, amount);
    }
    
    @WebMethod(operationName = "placeBigBind")
    public void placeBigBind(@WebParam(name = "tableId") Integer tableId,@WebParam(name = "userId")Long userId,@WebParam(name = "amount") Integer amount) throws PokerException{
        new GameController().placeBigBind(getSession(), tableId, userId, amount);
    }
    
    @WebMethod(operationName = "discardCard")
    public void discardCard(@WebParam(name = "tableId") Integer tableId, @WebParam(name = "playerId") Long playerId,@WebParam(name = "card") Card card) throws PokerException{
        new GameController().discardCard(getSession(), tableId, playerId, card);
    }
    
    @WebMethod(operationName = "bet")
    public void bet(@WebParam(name = "tableId") Integer tableId,@WebParam(name = "playerId") Long playerId,@WebParam(name = "amount") Integer amount) throws PokerException{
        new GameController().bet(session, tableId, playerId, amount);
    }
    
    @WebMethod(operationName = "fold")
    public void fold(@WebParam(name = "tableId") Integer tableId,@WebParam(name = "playerId") Long playerId) throws PokerException{
        new GameController().fold(getSession(),tableId, playerId);
    }
    
    /*
    @WebMethod(operationName = "check")
    public void check(@WebParam(name = "tableId") Integer tableId,@WebParam(name = "playerId") Long playerId,@WebParam(name = "amount") Integer amount) throws PokerException{
        new GameController().check(getSession(),tableId, playerId, amount);
    }*/
    
}
