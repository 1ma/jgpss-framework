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

import java.util.ArrayList;
import java.util.PriorityQueue;
import lombok.Data;

/**
 * A class representing the ASSIGN GPSS block.
 *
 * @author Pau Fonseca i Casas
 * @author M.Dolores
 * @author Ezequiel Andujar
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
@Data
public abstract class Bloc {

    private int id;
    private String label;
    private String comentari;
    private int posx;
    private int posy;
    private Model model;
    private Proces proces;
    private int pos;
    private int entryCount;
    private int retry;

    /**
     * Match chain used for blocks MATCH, GATHER, ASSEMBLE
     */
    private final PriorityQueue<Xact> matchChain;

    /**
     * Creates a new instance of Bloc
     *
     */
    public Bloc(int id, String label, String comentari, Model model) {
        
        this(id,label,comentari);       
        this.model = model;
    }

    public Bloc(int id, String label, String comentari) {
        this.id = id;
        this.label = label;
        this.comentari = comentari;
        entryCount = 0;
        retry = 0;
        matchChain = new PriorityQueue<>();
    }

    public Bloc() {
        entryCount = 0;
        retry = 0;
        matchChain = new PriorityQueue<>();
    }

    /**
     * Increases the transaction that have entered the block
     */
    public void incTrans() {
        entryCount++;
    }
    
    public void incRetry() {
        retry++;
    }

    /**
     * To execute the block. Must be implemented in each block.
     *
     * @param tr the current XACT (that cross the block).
     * @return return NULL if no more blocs can be executed now. The next block
     * otherwise.
     * @throws java.lang.Exception if some error at runtime happens throws an
     * Exception
     */
    public abstract Bloc execute(Xact tr) throws Exception;

    /**
     * Returns true if the bloc admits the current, false in case the xact is
     * refuse by the block. Must be implemented in each block
     *
     * @param tr the current XACT (that attempts to cross the bloc)
     * @return
     */
    public abstract boolean test(Xact tr);

    /**
     * Allows to obtanin thenext block in the current process of the XACT.
     * Modifies the current block of the XACT.
     *
     * @param tr the XACT to update it's current block.
     * @return the next block to be executed.
     */
    public Bloc nextBloc(Xact tr) {
        if (tr != null) {
            Bloc nextBloc = tr.getBloc();
            Proces pr = tr.getProces();
            ArrayList<Bloc> bls = pr.getBlocs();
            int mida = bls.size();
            int posicio = nextBloc.pos + 1;
            if (posicio < mida) {
                nextBloc = bls.get(posicio);
            } else {
                nextBloc = null;
            }

            tr.setBloc(nextBloc);

            return nextBloc;
        } else {
            return null;
        }
    }
}
