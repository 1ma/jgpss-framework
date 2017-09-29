/**
 * Software end-user license agreement.
 *
 * The LICENSE.TXT containing the license is located in the JGPSS project.
 * License.txt can be downloaded here:
 * href="http://www-eio.upc.es/~Pau/index.php?q=node/28
 *
 * NOTICE TO THE USER: BY COPYING, INSTALLING OR USING THIS SOFTWARE OR PART OF
 * THIS SOFTWARE, YOU AGREE TO THE   TERMS AND CONDITIONS OF THE LICENSE AGREEMENT
 * AS IF IT WERE A WRITTEN AGREEMENT NEGOTIATED AND SIGNED BY YOU. THE LICENSE
 * AGREEMENT IS ENFORCEABLE AGAINST YOU AND ANY OTHER LEGAL PERSON ACTING ON YOUR
 * BEHALF.
 * IF, AFTER READING THE TERMS AND CONDITIONS HEREIN, YOU DO NOT AGREE TO THEM,
 * YOU MAY NOT INSTALL THIS SOFTWARE ON YOUR COMPUTER.
 * UPC IS THE OWNER OF ALL THE INTELLECTUAL PROPERTY OF THE SOFTWARE AND ONLY
 * AUTHORIZES YOU TO USE THE SOFTWARE IN ACCORDANCE WITH THE TERMS SET OUT IN
 * THE LICENSE AGREEMENT.
 */
package model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A class representing the XACT's.
 *
 * @author Pau Fonseca i Casas
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
public class Xact implements Serializable, Cloneable {

    static final long serialVersionUID = 42L;
    
    //Proces al que pertany la XACT.
    private Proces proces;
    //Bloc en el que es troba la XACT.
    private Bloc bloc;
    //Identificador de la XACT.
    private int ID;
    //Temps en el que s'ha creat
    private float creatTime;
    //Temps en el que es pot moure
    private float moveTime;
    //Prioritat
    private float priority;
    // Transaction parameters
    private final HashMap<String, Object> transactionParameters;    
    // Delay indicator (Used in Simultaneous mode in a TRANSFER block)    
    private boolean delay;    
    // Restore indicator (Used to indicate if the Xact must be restored to the FEC)
    private boolean restore;    
    // Used to set the nextBloc the Xact must be routed (Used for FUNAVAIL blocks)
    private Bloc blockRoute;
    // Used to grant ownership to the transaction on a facility unavailable period
    private boolean ownershipGranted;
    // Used to indentify the transaction current assembly set 
    private int assemblySet;
    // Used in Assemble block
    private int counter;
    
    /**
     * Constructor
     */
    public Xact() {
        transactionParameters = new HashMap<>();
        delay = false;
        restore = false;
        blockRoute = null;
        ownershipGranted = false;
        counter = 0;
    }
    
    /**
     * Returns the Block the Xact must be routed to
     * @return 
     */
    public Bloc getBlocRoute() {
        return blockRoute;
    }
    
    /**
     * Sets the Block the Xact must be route to
     * @param blockRoute 
     */
    public void setBlockRoute(Bloc blockRoute) {
        this.blockRoute = blockRoute;
    }
    
    /**
     * Return true if the Xact is allowed to own a facility even in the 
     * unavailable facility perdiod
     * @return 
     */
    public boolean ownershipGranted() {
        return ownershipGranted;
    }
    
    /**
     * Sets the ownershipGranted flag
     * @param ownershipGranted 
     */
    public void setOwnershipGranted(boolean ownershipGranted) {
        this.ownershipGranted = ownershipGranted;
    }
    
    
    /**
     * Return true if the Xact must be restored to the FEC, false otherwise
     * @return 
     */
    public boolean restoreToFEC() {
        return restore;
    }
    
    /**
     * Sets the Xact restore indicator to value
     * @param value
     */
    public void restore(boolean value) {
        this.restore = value;
    }

    /**
     * Returns the Transaction parameters
     * @return 
     */
    public HashMap<String, Object> getTransactionParameters() {
        return this.transactionParameters;
    }

    public Object getParameter(String paramName) {
        return transactionParameters.getOrDefault(paramName, null);
    }
    
    /**
     * To obtain the indentifier of the XACT.
     *
     * @return the identifier of the XACT.
     */
    public int getID() {
        return ID;
    }

    /**
     * To set the indentifier of the XACT.
     *
     * @param id the enw identifier.
     */
    public void setID(int id) {
        ID = id;
    }

    /**
     * To set the block that contains the XACT.
     *
     * @param b the new block.
     */
    void setBloc(Bloc b) {
        bloc = b;
    }

    /**
     * To set the process that contains the XACT.
     *
     * @param p the new process.
     */
    void setProces(Proces p) {
        proces = p;
    }

    /**
     * To obtain the move time of the XACT.
     *
     * @return the moveTime.
     */
    public float getMoveTime() {
        return moveTime;
    }

    /**
     * To set the move time of the XACT.
     *
     * @param moveTime the moveTime to set
     */
    public void setMoveTime(float moveTime) {
        this.moveTime = moveTime;
    }

    /**
     * To obtain the creation time of the XACT.
     *
     * @return the creatTime
     */
    public float getCreatTime() {
        return creatTime;
    }

    /**
     * To modify the creation time of the XACT.
     *
     * @param creatTime the creatTime to set
     */
    public void setCreatTime(float creatTime) {
        this.creatTime = creatTime;
    }

    /**
     * To get the process that contains the XACT.
     *
     * @return the proces
     */
    public Proces getProces() {
        return proces;
    }

    /**
     * To get the block that contains the XACT.
     *
     * @return the bloc.
     */
    public Bloc getBloc() {
        return bloc;
    }
    
    /**
     * To get the delay indicator
     * @return 
     */
    public boolean getDelay() {
        return delay;
    }
    
    /**
     * To set the delay indicator
     * @param delay 
     */    
    public void setDelay(boolean delay) {
        this.delay = delay;
    }

    /**
     * To get the priority of the XACT.
     *
     * @return the priority
     */
    public float getPriority() {
        return priority;
    }

    /**
     * To set the priority of the XACT.
     *
     * @param priority the priority to set
     */
    public void setPriority(float priority) {
        this.priority = priority;
    }
    
    /**
     * To get the transaction current assembly set
     * @return 
     */
    public int getAssemblySet() {
        return assemblySet;
    }
    
    /**
     * To set the transaction current assembly set
     * @param assemblySet 
     */
    public void setAssemblySet(int assemblySet) {        
        this.assemblySet = assemblySet;              
    }
    
    /**
     * To get the current counter
     * @return 
     */
    public int getCounter() {
        return counter;                
    }
    
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    /**
     * Decrements the current counter and returns it
     * @return 
     */
    public int decCounter() {
        counter--;
        return counter;
    }
    
    /**
     * Allows to obtain a copy of the object
     * @return
     * @throws CloneNotSupportedException 
     */
    @Override
    protected Xact clone() throws CloneNotSupportedException {
        return (Xact) super.clone();
    }
}
