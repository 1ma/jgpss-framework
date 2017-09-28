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

import java.util.HashMap;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

/**
 * A class representing the GENERATE block.
 *
 * @author Pau Fonseca i Casas
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
@NoArgsConstructor
public class Leave extends Bloc {

    @Getter
    @Setter
    private String A;
    
    @Getter
    @Setter
    private int B;

    /**
     * Creates a new instance of Leave
     *
     * @param comentari the comment of the block.
     * @param label the label of the block.
     * @param A the name of the STORAGE.
     * @param B the number of instances of the STORAGE to free.
     */
    public Leave(String comentari, String label, String A, int B) {

        super(Constants.idLeave, label, comentari);
        this.A = A;
        this.B = B;
    }    

    /**
     * The method that executes the Block
     *
     * @param tr tr the transaction that cross the block.
     * @return this method returns the next block of the transaction active
     */
    @Override
    public Bloc execute(Xact tr) {

        HashMap<String, Facility> facilities = getModel().getFacilities();

        if (B == 0) {
            B = 1;
        }

        // Realises B transactions from the server
        facilities.get(A).release(B, tr);
        PriorityQueue<Xact> BEC = getModel().getBEC().get(A);

        /**
         *
         * Retrieve a blocked transaction from the BEC and put it back to the
         * CEC or the FEC if the restore flag is set
         *
         */
        if (!BEC.isEmpty() && facilities.get(A).isAvailable()) {
            Xact trBlocked = getModel().getBEC().get(this.A).poll();

            if (trBlocked.restoreToFEC()) {

                getModel().getFEC().add(trBlocked);

            } else {
                trBlocked.setPriority(trBlocked.getPriority() + 1);
                getModel().getCEC().add(trBlocked);
            }
        }

        /*
        // Once the transaction releases the server, we have to update the transaction move times from the BEC        
        if (BEC != null) {
            
            Iterator<Xact> it = BEC.iterator();
            while (it.hasNext()) {
                Xact xact = it.next();
                xact.setMoveTime(xact.getMoveTime() + (getModel().relativeClock - tr.getMoveTime()));
            }            
            // Retrieve a blocked transaction from the BEC and put it back to the CEC
            if (!BEC.isEmpty() && facilities.get(A).isAvailable()) {
                Xact trBlocked = getModel().getBEC().get(this.A).poll();
                getModel().getCEC().add(trBlocked);
            }
        }*/
        return nextBloc(tr);
    }

    @Override
    public boolean test(Xact tr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
